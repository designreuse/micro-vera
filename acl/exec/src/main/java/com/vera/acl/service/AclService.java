package com.vera.acl.service;

import com.vera.shared.model.Function2;
import lombok.var;
import org.casbin.adapter.JDBCAdapter;
import org.casbin.jcasbin.main.Enforcer;
import org.casbin.jcasbin.model.Model;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * 权限管理的核心类，导入了 Casbin 一个优秀的 RBAC 框架
 *
 * @author ychost
 * @date 2018-10-05
 */
@Service
public class AclService {

    private Enforcer enforcer;
    private static final String rolePrefix = "_role_";

    @Value("${jdbc.driver}")
    String jdbcDriver;
    @Value("${jdbc.url}")
    String jdbcUrl;
    @Value("${jdbc.user}")
    String jdbcUser;
    @Value("${jdbc.pwd}")
    String jdbcPwd;
    @Value("${acl.model}")
    String aclModel;

    public Enforcer getEnforcer(){
        if(enforcer == null){
            synchronized (AclService.class){
                if(enforcer == null){
                    JDBCAdapter adapter = new JDBCAdapter(jdbcDriver, jdbcUrl,
                            jdbcUser, jdbcPwd,true);
                    Model model = new Model();
                    System.out.println(aclModel);
//                    String path = this.getClass().getClassLoader().getResource("rbac.model.conf").getPath();
                    model.loadModelFromText(aclModel);
                    enforcer= new Enforcer(model,adapter);
                    enforcer.loadPolicy();
                }
            }
        }
        return enforcer;
    }


    /**
     * 批量删除权限，如果一个权限删除发生异常或者持久化的时候发生异常则全部回滚
     * @param user 用户名
     * @param permissions 待删权限列表
     * @return 删除结果
     */
    public boolean delPermissions(String user, String... permissions){
      return execBatch(user,permissions,
              (a,b)->!getEnforcer().hasPermissionForUser(a,b),
              getEnforcer()::deletePermissionForUser,
              getEnforcer()::addPermissionForUser);
    }


    /**
     * 批量添加权限，如果其中有一个权限添加失败则全部回滚
     * @param user 添加权限的用户
     * @param permissions 权限名称
     * @return 添加结果
     */
    public boolean addPermissions(String user,String... permissions){
        return execBatch(user,permissions,
                getEnforcer()::hasPermissionForUser,
                getEnforcer()::addPermissionForUser,
                getEnforcer()::deletePermissionForUser);
    }

    /**
     * 给角色添加权限，角色和用户其实是一个盖帘
     * @param role 角色名
     * @param permissions 权限
     * @return 添加结果
     */
    public boolean addPermissionsForRole(String role,String... permissions){
        role = rolePrefix+role;
        return addPermissions(role,permissions);
    }

    /**
     * 给某个角色添加用户
     * @param role 角色
     * @param user 用户
     * @return 添加结果
     */
    public boolean addUserForRole(String role,String user){
        // 如果已经含有该用户了直接返回
        // 如果用户没有属于任何角色则会抛出异常
        role = rolePrefix+role;
        try {
            if (getEnforcer().hasRoleForUser(user, role)) {
                return true;
            }
        }catch (Exception e){
           // 正常
        }
       // 只有持久化成功才算成功，否则回滚
       if(getEnforcer().addRoleForUser(user,role)){
           try{
               getEnforcer().savePolicy();
               return true;
           }catch (Exception e){
               getEnforcer().deleteRoleForUser(user,role);
               return false;
           }
       }
       return false;
    }

    /**
     * 检查某个用户是否拥有全部权限
     * @param user 用户名
     * @param permissions 权限列表
     * @return 检查结果
     */
    public boolean checkPermissions(String user,String[] permissions){
        for (String permission : permissions) {
            if(!getEnforcer().enforce(user,permission)){
               return false;
            }
        }
        return true;
    }

    /**
     * 获取用户的所有权限，api 里面只提供了获取专属用户的权限但是继承的角色权限没有获取，这里实现这个功能
     * @param user 用户名
     * @return 用户所有的权限
     */
    public List<String> getAllPermissions(String user){
        var pList = getEnforcer().getPermissionsForUser(user);
        List<String> permissions = new ArrayList<>();
        for (List<String> p : pList) {
            permissions.add(p.get(1));
        }
        var roles = getEnforcer().getRolesForUser(user);
        // 递归获取所有的权限
        if(roles != null && roles.size() > 0){
            for (String role : roles) {
                permissions.addAll(getAllPermissions(role));
            }
        }
        return permissions;
    }

    /**
     * 删除角色下面的某个用户，如果删除失败则直接回滚，如果角色没有包含该用户则直接返回 true
     * @param role 角色名称
     * @param user 用户名称
     * @return 删除结果
     */
    public boolean roleDeleteUser(String role,String user){
        role = rolePrefix + role;
        if(getEnforcer().hasRoleForUser(user,role)) {
            if (!getEnforcer().deleteRoleForUser(user, role)) {
                return  false;
            }
            try {
                getEnforcer().savePolicy();
                return true;
            } catch (Exception e) {
                getEnforcer().addRoleForUser(user,role);
                return false;
            }
        }
        return  true;
    }

    /**
     * 删除某个用户
     * @param user 用户名
     * @return 删除结果
     */
    public boolean deleteUser(String user){
        return deleteRole(user);
    }

    /**
     * 删除某个角色
     * @param role
     * @return
     */
    public boolean deleteRole(String role){
       role = rolePrefix+role;
       return deleteNode(role);
    }

    /**
     * 删除某个节点，可以是用户/角色
     * @param user
     * @return
     */
     boolean deleteNode(String user){
        var pList =  getEnforcer().getPermissionsForUser(user);
        var roles = getEnforcer().getRolesForUser(user);
        List<String> sons = null;
        // 如果删除的是角色那么还得查出角色的子节点
        if(isRole(user)){
            sons = getEnforcer().getUsersForRole(user);
        }
        if(getEnforcer().deleteUser(user)){
            try{
                getEnforcer().savePolicy();
                return true;
                // 发生了异常，执行回滚恢复内存中的用户数据
            }catch (Exception e){
                List<String> permissions = new ArrayList<>();
                // 映射权限
                if(pList != null) {
                    for (List<String> ps : pList) {
                        permissions.add(ps.get(1));
                    }
                }
                // 恢复权限
                for (String permission : permissions) {
                    getEnforcer().addPermissionForUser(user,permission);
                }
                // 恢复父节点
                if(roles != null) {
                    for (String role : roles) {
                        getEnforcer().addRoleForUser(user,role);
                    }
                }
                // 恢复子节点
                if(sons != null){
                    for (String son : sons) {
                        getEnforcer().addRoleForUser(son,user);
                    }
                }
                return false;
            }
        }
        return false;
    }

    /**
     * 判断某个名称是否为角色
     * @param node
     * @return
     */
    public boolean isRole(String node){
        if(node == null){
            return false;
        }
        return node.startsWith(rolePrefix);
    }

    /**
     * 批量权限操作公共接口，一个地方发生了异常则全部回滚
     * @param user 用户
     * @param permissions 权限
     * @param conFunc 权限已经存在/不存在，然后跳过无效遍历
     * @param execFunc 添加/删除权限
     * @param rollbackFunc 回滚 添加/删除 操作
     * @return 批量操作结果
     */
    boolean execBatch(String user, String[] permissions, Function2<String,String,Boolean> conFunc,
                      Function2<String,String,Boolean> execFunc,
                      Function2<String,String,Boolean> rollbackFunc){
        if(permissions == null || permissions.length == 0){
            return false;
        }
        List<String> successList = new ArrayList<>();
        boolean errorsHappened = false;
        for (String permission : permissions) {
            if(conFunc.apply(user,permission)){
                continue;
            }
            if(!execFunc.apply(user,permission)){
                errorsHappened = true;
                break;
            }
            successList.add(permission);
        }
        // 发生错误全部回滚
        if(errorsHappened){
            for (String permission : successList) {
                rollbackFunc.apply(user,permission);
            }
            return false;
        }
        // 持久化过程中发生异常也全部回滚
        try{
            getEnforcer().savePolicy();
        }catch (Exception e){
            for (String permission : successList) {
                rollbackFunc.apply(user,permission);
            }
            return false;
        }
        return true;
    }

}


