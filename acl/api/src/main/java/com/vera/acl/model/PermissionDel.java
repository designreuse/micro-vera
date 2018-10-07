package com.vera.acl.model;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author ychost
 * @date 2018/10/5
 */
@Data
public class PermissionDel {
   @NotNull
   private String user;
   @NotNull
   private String[] permissions;
}
