package com.vera.acl.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author ychost
 * @date 2018-10-05
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PermissionAdd {
   @NotNull
   private String user;
   @NotNull
   private String[] permissions;
}
