package com.doudou.dispatch.trip.api.entities;

import lombok.Data;

@Data
public class TUser {
    private int id;
    private String userName;
    private String password;
    private String nickName;
    private String company;

}
