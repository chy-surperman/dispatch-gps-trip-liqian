package com.doudou.dispatch.trip.api.controllers;

import com.dispatch.gps.commons.bean.HttpApiConstans;
import com.dispatch.gps.commons.bean.JsonResult;
import com.doudou.dispatch.trip.api.entities.TUser;
import com.doudou.dispatch.trip.api.services.TUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private TUserService tUserService;

    @PostMapping("/login")
    public JsonResult login(@RequestBody TUser tUser){
        JsonResult jsonResult = new JsonResult();
        if(StringUtils.isEmpty(tUser.getUserName())){
            return jsonResult.setDefine("没有输入用户名");
        }
        if(StringUtils.isEmpty(tUser.getPassword())){
            return jsonResult.setDefine("没有输入密码");
        }

        TUser dbUser = tUserService.login(tUser);
        if(dbUser == null){
            return jsonResult.setDefine("用户名或密码错误");
        }
        tUserService.saveSessionTUser(dbUser);

        return jsonResult.setCode(HttpApiConstans.success)
                         .setDefine(HttpApiConstans.succe_define)
                         .setResult(dbUser);
    }
}
