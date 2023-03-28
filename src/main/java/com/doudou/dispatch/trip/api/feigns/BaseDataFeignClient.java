package com.doudou.dispatch.trip.api.feigns;

import com.dispatch.gps.commons.entities.Driver;
import com.doudou.dispatch.trip.api.entities.TVehicle;
import com.doudou.dispatch.trip.commons.HttpRespResult;
import com.example.openfeign.annotation.Host;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

import java.util.List;

@Host("http://139.159.210.121/workplan")
public interface BaseDataFeignClient {

    @RequestLine("GET /routeMsg/company?company={company}")
    @Headers({
            "AccessSource: wechat"
    })
    public HttpRespResult<List<String>> getCompanyRoute(@Param("company") String company);

    @RequestLine("GET /vehicle/company?company={company}")
    @Headers({
            "AccessSource: wechat"
    })
    public HttpRespResult<List<TVehicle>> getCompanyVehicle(@Param("company") String company);

    @RequestLine("GET /driver/company?company={company}")
    @Headers({
            "AccessSource: wechat"
    })
    public HttpRespResult<List<Driver>> getCompanyDriver(@Param("company") String company);

    @RequestLine("GET /vehicle/routeName?routeName={routeName}")
    @Headers({
            "AccessSource: wechat"
    })
    public HttpRespResult<List<TVehicle>> getRouteVehicle(@Param("routeName") String routeName);

    @RequestLine("GET /driver/routeName?routeName={routeName}")
    @Headers({
            "AccessSource: wechat"
    })
    public HttpRespResult<List<Driver>> getRouteDriver(@Param("routeName") String routeName);

}
