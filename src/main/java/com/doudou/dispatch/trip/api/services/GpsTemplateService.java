package com.doudou.dispatch.trip.api.services;


import com.dispatch.gps.commons.entities.Workplan;
import com.doudou.dispatch.trip.api.entities.GpsTemplate;

import java.util.List;

public interface GpsTemplateService {

    public List<GpsTemplate> getGpsTemplate(String routeName, int runLongTime);

    public List<GpsTemplate> getGpsTemplate(Workplan workplan);

}
