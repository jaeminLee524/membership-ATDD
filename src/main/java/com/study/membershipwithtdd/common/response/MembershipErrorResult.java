package com.study.membershipwithtdd.common.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MembershipErrorResult {
    DUPLICATED_MEMBERSHIP_REGISTER(HttpStatus.BAD_REQUEST, "Duplicated Membership Register Request"),
    MEMBERSHIP_NOT_FOUND(HttpStatus.NOT_FOUND, "membership Not Found"),
    NOT_MEMBERSHIP_OWNER(HttpStatus.BAD_REQUEST, "not membership owner"),
    ;

    private final HttpStatus httpStatus;
    private final String errorMsg;
}
