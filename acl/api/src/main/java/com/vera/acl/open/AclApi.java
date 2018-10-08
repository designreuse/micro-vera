package com.vera.acl.open;

import com.vera.acl.model.PermissionAdd;
import com.vera.acl.model.PermissionCheck;
import com.vera.acl.url.AclRequestUrl;
import com.vera.shared.model.Rest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * @author ychost
 * @date 2018/10/7
 */
@FeignClient("acl")
public interface  AclApi {
    @GetMapping(AclRequestUrl.permissionCheck)
    Rest<Boolean> hasPermission(@RequestBody @Valid PermissionCheck permissionCheck);

    @PostMapping(AclRequestUrl.permissionAdd)
    Rest<Boolean> addPermissionForUser(@RequestBody @Valid PermissionAdd permission);
}
