package com.example.signin.utils;


import com.example.signin.entity.SponsorLocation;
import com.example.signin.entity.UserSponsorLocation;

public class DistanceCalculatorUtil {

    // 地球半径（单位：千米）
    private static final double EARTH_RADIUS = 6371.0;

    // 计算两个经纬度之间的距离（单位：千米）
    public static double calculateDistance(UserSponsorLocation usersponsorLocation, SponsorLocation signInSponsorLocation) {
        // 将经纬度转换为弧度
        double lat1Rad = Math.toRadians(usersponsorLocation.getLatitude());
        double lon1Rad = Math.toRadians(usersponsorLocation.getLongitude());
        double lat2Rad = Math.toRadians(signInSponsorLocation.getLatitude());
        double lon2Rad = Math.toRadians(signInSponsorLocation.getLongitude());

        // 计算经纬度差值
        double dLat = lat2Rad - lat1Rad;
        double dLon = lon2Rad - lon1Rad;

        // 使用 Haversine 公式计算距离
        double a = Math.pow(Math.sin(dLat / 2), 2) + Math.cos(lat1Rad) * Math.cos(lat2Rad) * Math.pow(Math.sin(dLon / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c;
    }
}
