package com.aistlab.rank.conntroller;

import com.aistlab.rank.service.RankService;
import com.vera.acl.model.PermissionAdd;
import com.vera.acl.open.AclApi;
import com.vera.shared.model.Rest;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RankController {
    @Autowired
    RankService rankService;

    @Autowired
    AclApi aclApi;

    @GetMapping("/say")
    public Rest<Boolean> sayHello(){
        var user = "ychost";
        var permission =new String[]{ "vera-read-book","test"};
        var pa = new PermissionAdd(user,permission);
        var addRest = aclApi.addPermissionForUser(pa);
        return addRest;
    }

}
