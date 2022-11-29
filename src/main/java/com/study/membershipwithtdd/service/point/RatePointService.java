package com.study.membershipwithtdd.service.point;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RatePointService implements PointService {

    public static final int POINT_RATE = 1;

    @Override
    public int calculateAmount(int point) {
        return point * POINT_RATE / 100;
    }
}
