package com.vera.acl.model;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * 角色下面删除用户
 *
 * @author ychost
 * @date 2018/10/8
 */
@Data
public class RoleDeleteUser {
    @NotEmpty
    private String role;
    @NotEmpty
    private String user;
}
