package com.doudou.dispatch.trip.api.controllers;

import com.dispatch.gps.commons.entities.Workplan;
import com.doudou.dispatch.trip.api.entities.QueryWorkplan;
import com.doudou.dispatch.trip.api.services.WorkplanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * <b><code>GernalTrip</code></b>
 * <p/>
 * Description
 * <p/>
 * <b>Creation Time:</b> 2024/7/10 12:58.
 *
 * @author xxx
 * @since dispatch-gps-trip Chy
 */
@Controller
@RequestMapping("/operation/gpsLocus")
public class GernalTrip {

    @Autowired
    public WorkplanService workplanService;


    @RequestMapping("/gpsCros")
    public void generateOperatingGpslocus() {
        QueryWorkplan queryWorkplan = new QueryWorkplan("2023-01-01","园区E线","21717");
        List<Workplan> workplans = workplanService.queryWorkplans(queryWorkplan);
        if (workplans!=null){
        for (Workplan s:workplans){
            System.out.println(s.toString());
         }
        }
    }
}
