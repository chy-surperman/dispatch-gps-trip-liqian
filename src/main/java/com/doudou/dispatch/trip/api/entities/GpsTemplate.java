package com.doudou.dispatch.trip.api.entities;

import lombok.Data;

import java.util.Date;

@Data
public class GpsTemplate {
    private int id;
    private float latitude;// float(10,6) DEFAULT NULL,
    private float longitude;// float(10,6) DEFAULT NULL,
    private float speed;// float(10,2) DEFAULT NULL,
    private int seq;
    private Date createTime;// datetime DEFAULT NULL,
    private String routeName;// varchar(20) DEFAULT NULL,
    private int type;// tinyint(4) DEFAULT NULL COMMENT '1平峰、2高峰',
}
