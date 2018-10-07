package com.vera.acl.model;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 检查用户是否有全部权限
 *
 * @author ychost
 * @date 2018/10/7
 */
@Data
public class PermissionCheck {
   @NotNull
   private String user;
   @NotNull
   private String[] permissions;
}
