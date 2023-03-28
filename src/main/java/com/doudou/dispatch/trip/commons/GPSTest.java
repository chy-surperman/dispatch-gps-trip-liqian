package com.doudou.dispatch.trip.commons;

import com.dispatch.gps.commons.utils.GPSUtil;

public class GPSTest {
    public static void main(String[] args) {
        double[] doubles = GPSUtil.gps84_To_Gcj02(28.2578, 113.101513);
        System.out.println(doubles[1] + "," + doubles[0]);
    }
}
