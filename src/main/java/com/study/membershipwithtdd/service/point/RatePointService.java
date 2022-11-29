package com.study.membershipwithtdd.service.point;

public class RatePointService implements PointService{

    public static final int POINT_RATE = 1;

    @Override
    public int calculateAmount(int point) {
        return point * POINT_RATE / 100;
    }
}
