package com.doudou.dispatch.trip.api.services;

import com.dispatch.gps.commons.entities.Workplan;
import com.doudou.dispatch.trip.api.entities.QueryWorkplan;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;

/**
 * <b><code>GernalTripLocusService</code></b>
 * <p/>
 * Description
 * <p/>
 * <b>Creation Time:</b> 2024/7/10 15:55.
 *
 * @author xxx
 * @since dispatch-gps-trip Chy
 */

public interface GernalTripLocusService {

    public void CreateGpsLocus(String date,String routeName,String vehicleId) throws ParseException;

}
