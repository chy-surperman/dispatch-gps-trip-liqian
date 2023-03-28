package com.doudou.dispatch.trip.api.mappers;

import com.doudou.dispatch.trip.api.entities.TUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TUserMapper {
    public List<TUser> selectByUserName(@Param("userName") String userName);
}
