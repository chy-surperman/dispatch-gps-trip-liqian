package com.doudou.dispatch.trip.api.mappers;

import com.doudou.dispatch.trip.api.entities.RouteWorkplanInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RouteWorkplanInfoMapper {

    public List<RouteWorkplanInfo> selectRouteWorkplanInfo(@Param("freq") int freq);

}
