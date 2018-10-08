package com.vera.acl.model;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 删除角色
 * @author ychost
 * @date 2018/10/8
 */
@Data
public class RoleDelete {
    @NotNull
    private String role;
}
