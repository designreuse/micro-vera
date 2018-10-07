package com.vera.acl.model;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 添加某个用户到角色下面
 * @author ychost
 * @date 2018/10/6
 */
@Data
public class RoleAddUser {
    @NotNull
    private String role;
    @NotNull
    private String user;
}
