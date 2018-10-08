package com.vera.acl.model;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 删除某个用户
 * @author ychost
 * @date 2018/10/8
 */
@Data
public class UserDelete {
    @NotNull
    private String user;
}
