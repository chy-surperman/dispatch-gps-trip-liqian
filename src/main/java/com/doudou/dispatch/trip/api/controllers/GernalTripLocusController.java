package com.doudou.dispatch.trip.api.controllers;

import com.dispatch.gps.commons.entities.Workplan;
import com.doudou.dispatch.trip.api.entities.QueryWorkplan;
import com.doudou.dispatch.trip.api.services.GernalTripLocusService;
import com.doudou.dispatch.trip.api.services.WorkplanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.ParseException;
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
public class GernalTripLocusController {

   @Autowired
    GernalTripLocusService gernalTripLocusService;

    @RequestMapping("/gpsCros")
    public void generateOperatingGpslocus(String date,String routeName,String vehicleId) throws ParseException {
        gernalTripLocusService.CreateGpsLocus(date,routeName,vehicleId);
    }
}
