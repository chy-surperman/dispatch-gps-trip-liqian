package com.doudou.dispatch.trip.api.entities;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@ToString
public class Gpsreport implements Serializable{
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String vehicleId;
	private Double latitude;
	private Double longitude;
	private Date createTime;
	private String sign;
	private String code;
	private String define;
	private String siteName;
	private String notice;
	private Integer intervalDistance;	//与上一次上报的行驶距离
	private double distance;
	private String altitude;
	private String direction;
	private String speed;
	private String selfNum;
	private String reportTime;
	private long timestamp;
	private int isSupplement;
	private String isAuthGps;	//nidonde
	private String terminal;
	private String workModel;
}
