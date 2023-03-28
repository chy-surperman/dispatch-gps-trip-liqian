package com.doudou.dispatch.trip.api.entities;

import com.dispatch.gps.commons.entities.Workplan;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class WorkplanChangeRecode implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int id;
	private String modifyId;
	private String sign;
	private long workplanId;
	private String reasonType;
	private String reasonDetail;
	private Date modifyTime;
	private String modifyTimeStr;
	private String modifyUser;
	
	private CreateWorkplan associationWorkplan;
	
	private List<Workplan> workplans;

}
