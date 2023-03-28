package com.doudou.dispatch.trip.api.services.impl;

import com.doudou.dispatch.trip.api.entities.TUser;
import com.doudou.dispatch.trip.api.mappers.TUserMapper;
import com.doudou.dispatch.trip.api.services.TUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service("tUserService")
public class TUserServiceImpl implements TUserService {

    @Autowired
    private TUserMapper tUserMapper;

    @Autowired
    private RedisTemplate<String,TUser> redisTemplate;

    private String login_session_key = "login_user_";

    @Override
    public TUser login(TUser tUser) {
        List<TUser> tUsers = tUserMapper.selectByUserName(tUser.getUserName());
        if(null == tUsers || tUsers.size() != 1){
            return null;
        }
        TUser dbUser = tUsers.get(0);
        if(dbUser.getPassword().equals(tUser.getPassword())){
            return dbUser;
        }
        return null;
    }

    @Override
    public void extensionLoginTime(String userName) {
        String loginKey = login_session_key + userName;
        redisTemplate.expire(loginKey,120,TimeUnit.MINUTES);
    }

    @Override
    public void saveSessionTUser(TUser tUser) {
        String loginKey = login_session_key + tUser.getUserName();
        redisTemplate.opsForValue().set(loginKey,tUser,120, TimeUnit.MINUTES);
    }

    @Override
    public TUser getSessionTUser(String userName) {
        String loginKey = login_session_key + userName;
        return redisTemplate.opsForValue().get(loginKey);
    }
}
