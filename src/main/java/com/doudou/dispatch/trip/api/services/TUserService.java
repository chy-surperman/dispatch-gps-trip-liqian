package com.doudou.dispatch.trip.api.services;

import com.doudou.dispatch.trip.api.entities.TUser;

public interface TUserService {

    public TUser login(TUser tUser);

    public void extensionLoginTime(String userName);

    public void saveSessionTUser(TUser tUser);

    public TUser getSessionTUser(String userName);
}
