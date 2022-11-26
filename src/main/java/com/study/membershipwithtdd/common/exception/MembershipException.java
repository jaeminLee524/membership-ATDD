package com.study.membershipwithtdd.common.exception;

import com.study.membershipwithtdd.common.response.MembershipErrorResult;
import lombok.Getter;

@Getter
public class MembershipException extends RuntimeException{

    private MembershipErrorResult membershipErrorResult;

    public MembershipException() {
    }

    public MembershipException(MembershipErrorResult membershipErrorResult) {
        super(membershipErrorResult.getErrorMsg());
        this.membershipErrorResult = membershipErrorResult;
    }
}
