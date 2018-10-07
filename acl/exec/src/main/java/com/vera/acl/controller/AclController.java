package com.vera.acl.controller;

import com.vera.acl.model.*;
import com.vera.acl.service.AclService;
import com.vera.acl.url.AclRequestUrl;
import com.vera.shared.model.Rest;
import com.vera.shared.model.RestCode;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * 权限控制的对外接口
 *
 * @author ychost
 * @date 2018-10-05
 */
@RestController
public class AclController {
    @Autowired
    AclService aclService;

    @ApiOperation(value = "给用户添加权限",notes = "一个权限添加失败，则全部回滚")
    @ApiImplicitParam(name = "permissionAdd",value = "添加权限模型",required = true,dataType = "PermissionAdd")
    @PostMapping(value = AclRequestUrl.permissionAdd)
    public Rest<Boolean> addPermissionForUser(@RequestBody @Valid PermissionAdd permissionAdd, BindingResult bindingResult){
        var rest = Rest.<Boolean>create();
        if(bindingResult.hasErrors()){
            return rest.withMessages(bindingResult.getAllErrors()).with(RestCode.reqParamError);
        }
        if(aclService.addPermissions(permissionAdd.getUser(),permissionAdd.getPermissions())){
           rest.with(RestCode.success,"添加权限成功",true);
        }else{
            rest.with(RestCode.failed,"添加权限失败",false);
        }
        return rest;
    }

    @ApiOperation(value = "删除用户权限",notes="一个删除失败则全部回滚")
    @ApiImplicitParam(name = "permissionDel",value = "删除权限模型",required = true,dataType = "PermissionDel")
    @PostMapping(AclRequestUrl.permissionDel)
    public Rest<Boolean> delPermissionFromUser(@RequestBody @Valid PermissionDel permissionDel,BindingResult bindingResult){
       var rest = Rest.<Boolean>create();
       if(bindingResult.hasErrors()){
            return rest.withMessages(bindingResult.getAllErrors());
       }
       if(aclService.delPermissions(permissionDel.getUser(),permissionDel.getPermissions())){
           rest.with(RestCode.success,"删除权限成功",true);
       }else{
           rest.with(RestCode.failed,"删除权限失败",false);
       }
       return rest;
    }


    @ApiOperation(value = "查询用户权限",notes = "该接口也能查接口权限 role:=_role_user, 如果有一个权限不存在则返回 false")
    @ApiImplicitParam(name = "permissionCheck",value = "待查权限",required = true,dataType = "PermissionCheck")
    @PostMapping(AclRequestUrl.permissionCheck)
    public Rest<Boolean> hasPermission(@RequestBody @Valid  PermissionCheck permissionCheck,BindingResult bindingResult){
        var rest = Rest.<Boolean>create();
        if(bindingResult.hasErrors()){
            return rest.withMessages(bindingResult.getAllErrors()).setCode(RestCode.reqParamError);
        }
        if(aclService.checkPermissions(permissionCheck.getUser(),permissionCheck.getPermissions())){
           rest.with(RestCode.success,"含有权限",true);
        }else{
           rest.with(RestCode.failed,"不含权限",false);
        }
        return rest;
    }


    @ApiOperation(value = "查询用户所有的权限列表",notes = "每一项都是一个权限")
    @ApiImplicitParam(name = "user",value = "用户名",required = true)
    @GetMapping(AclRequestUrl.permissionFetch)
    public Rest<List<String>> getAllPermissions(@PathVariable String user){
        var rest = Rest.<List<String>>create();
        var permissions = aclService.getAllPermissions(user);
        rest.with(RestCode.success,"获取成功",permissions);
        return rest;
    }

    @ApiOperation(value = "给某个角色添加用户",notes = "系统中角色和用户的模型是一样的")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleAddUser",value = "角色模型",required = true,dataType = "RoleAddUser")
    })
    @PostMapping(AclRequestUrl.roleAddUser)
    public Rest<Boolean> addUserForRole(@RequestBody @Valid RoleAddUser roleAddUser,BindingResult bindingResult){
        var rest = Rest.<Boolean>create();
        if(bindingResult.hasErrors()){
            return rest.withMessages(bindingResult.getAllErrors()).setCode(RestCode.reqParamError);
        }
        if(aclService.addUserForRole(roleAddUser.getRole(),roleAddUser.getUser())){
            rest.with(RestCode.success,"添加用户到角色成功",true);
        }else{
            rest.with(RestCode.failed,"添加用户到角色失败",false);
        }
        return rest;
    }


    @ApiOperation(value = "给某个角色分配权限")
    @ApiImplicitParam(name = "roleAddPermission",value = "添加模型",required = true,dataType = "RoleAddPermission")
    @PostMapping(AclRequestUrl.roleAddPermission)
    public Rest<Boolean> addPermissionsForRole(@RequestBody @Valid RoleAddPermission roleAddPermission,BindingResult bindingResult){
        var rest = Rest.<Boolean>create();
        if(bindingResult.hasErrors()){
            return rest.withMessages(bindingResult.getAllErrors()).setCode(RestCode.reqParamError);
        }
        if(aclService.addPermissionsForRole(roleAddPermission.getRole(),roleAddPermission.getPermissions())){
            rest.with(RestCode.success,"给角色添加权限成功",true);
        }else{
            rest.with(RestCode.failed,"给角色添加权限失败",false);
        }
        return rest;
    }

}
