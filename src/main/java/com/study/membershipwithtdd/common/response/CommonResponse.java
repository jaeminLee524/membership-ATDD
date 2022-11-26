package com.study.membershipwithtdd.common.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommonResponse<T> {
    private Result result;
    private T data;
    private String message;
    private String errorCode;

    public static CommonResponse fail(String message, String errorCode) {
        return CommonResponse.builder()
            .result(Result.FAIL)
            .message(message)
            .errorCode(errorCode)
            .build();
    }

    private enum Result {
        SUCCESS,
        FAIL,
        ;
    }
}
