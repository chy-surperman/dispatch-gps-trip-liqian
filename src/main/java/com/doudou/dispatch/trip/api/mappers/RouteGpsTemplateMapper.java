package com.doudou.dispatch.trip.api.mappers;

import com.doudou.dispatch.trip.api.entities.GpsTemplate;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RouteGpsTemplateMapper {

    public List<GpsTemplate> selectByRouteAndType(@Param("routeName") String routeName,
                                                  @Param("type") int type);

}
