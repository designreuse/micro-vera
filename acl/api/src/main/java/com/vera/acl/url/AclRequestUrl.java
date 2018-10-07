package com.vera.acl.url;

/**
 * Acl 框架所有对外的接口 Url
 * @author ychost
 * @date 2018/10/6
 */
public final class AclRequestUrl {
    // 添加权限到用户
    public static final String permissionAdd = "/permission/add";
    // 删除用户的权限
    public static final String permissionDel = "/permission/del";
    // 检查用户是否拥有某些权限
    public static final String permissionCheck="/permission/check";
    // 获取某个用户的所有权限
    public static final String permissionFetch="/permission/fetch/{user}";
    // 给某个角色添加用户
    public static final String roleAddUser = "/role/addUser";
    // 给某个角色添加权限
    public static final String roleAddPermission = "/role/addPermission";
    // 删除某个角色
    public static final String roleDelete = "/role/delete";
    // 获取某个角色下面的所有用户
    public static final String usersOfRole= "/role/users";
    // 获取某个用户的所有角色
    public static final String rolesOfUser = "/user/roles";
    // 删除某个角色下面的某个用户
    public static final String roleDeleteUser = "/role/user/delete";

}
