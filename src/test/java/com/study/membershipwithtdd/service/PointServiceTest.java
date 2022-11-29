package com.study.membershipwithtdd.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.study.membershipwithtdd.service.point.PointService;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * 해당 테스트는 PointService를 interface 추상화 이전의 테스트 코드입니다.
 */
@ExtendWith(MockitoExtension.class)
public class PointServiceTest {

    @InjectMocks
    private PointService pointService;

    @Test
    void 포인트적립_10000원의_적립은_100원() {
        // given
        final int point = 10000;

        // when
        final int result = pointService.calculateAmount(point);

        // then
        assertThat(result).isEqualTo(100);
    }

    @Test
    void 포인트적립_20000원의_적립은_200원() {
        // given
        final int point = 20000;

        // when
        final int result = pointService.calculateAmount(point);

        // then
        assertThat(result).isEqualTo(200);
    }

    @Test
    void 포인트적립_30000원의_적립은_300원() {
        // given
        final int point = 30000;

        // when
        final int result = pointService.calculateAmount(point);

        // then
        assertThat(result).isEqualTo(300);
    }

    @ParameterizedTest
    @MethodSource("calculatePointParameter")
    public void 포인트적립_테스트(int point) {
        // given

        // when
        final int result = pointService.calculateAmount(point);

        // then
        assertThat(result).isNotZero();
    }

    private static Stream<Arguments> calculatePointParameter() {
        return Stream.of(
            Arguments.of(10000),
            Arguments.of(20000),
            Arguments.of(30000)
        );
    }
}
