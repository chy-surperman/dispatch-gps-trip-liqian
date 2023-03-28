package com.doudou.dispatch.trip.api.feigns;

import com.dispatch.gps.commons.bean.JsonResult;
import com.doudou.dispatch.trip.api.entities.QueryWorkplan;
import com.example.openfeign.annotation.Host;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

@Host("http://139.159.210.121/workplan")
public interface WorkplanFeignClient {

    @RequestLine("GET /report/daily/bigPrint?startDate={startDate}&routeName={routeName}")
    public JsonResult getBigDailyWorkplan(@Param("routeName") String routeName,
                                          @Param("startDate") String startDate);

    @RequestLine("POST /workplan/list")
    @Headers({
            "Content-Type: application/json;charset=UTF-8",
            "Authorization: 1",
            "AccessSource: wechat"
    })
    public JsonResult getWorkplanList(QueryWorkplan queryWorkplan);
}
