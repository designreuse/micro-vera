package com.vera.acl.model;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 给某个角色添加权限
 * @author ychost
 * @date 2018/10/6
 */
@Data
public class RoleAddPermission {
    @NotNull
    private String role;
    @NotNull
    private String[] permissions;

}
