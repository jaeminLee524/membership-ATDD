package com.study.membershipwithtdd.common.response;

import com.study.membershipwithtdd.common.exception.MembershipException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class CommonControllerAdvice {

    @ExceptionHandler(value = MembershipException.class)
    public CommonResponse onMembershipException(MembershipException ex) {
        log.error("MembershipException occur: ", ex);
        return CommonResponse.fail(ex.getMessage(), ex.getMembershipErrorResult().name());
    }

    @ExceptionHandler(value = Exception.class)
    public CommonResponse onException(Exception ex) {
        log.error("Exception occur: ", ex.getMessage());
        return CommonResponse.fail(ErrorCode.COMMON_SYSTEM_ERROR);
    }
}
