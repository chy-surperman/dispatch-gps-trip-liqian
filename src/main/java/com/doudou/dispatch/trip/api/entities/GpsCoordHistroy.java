package com.doudou.dispatch.trip.api.entities;

import lombok.Data;

import java.util.Date;

@Data
public class GpsCoordHistroy {
    private long Id;
    private int GprsId;         //线路号
    private int OnboardId;      //车号
    private String plateNum;    //车牌
    private Date OccurTime;     //上报时间
    private float Latitude;     //纬度
    private float Longitude;    //经度
    private int Angle;          //方向
    private int Height;         //海拔
    private int Velocity;       //速度
    private Date ServerTime;    //接收时间
}
