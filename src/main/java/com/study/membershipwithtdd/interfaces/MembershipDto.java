package com.study.membershipwithtdd.interfaces;

import com.study.membershipwithtdd.domain.membership.Membership.MembershipType;
import lombok.Builder;
import lombok.Getter;

public class MembershipDto {

    /**
     * response
     */
    @Builder
    @Getter
    public static class MembershipResponse {
        private Long id;
        private MembershipType membershipType;
    }
}
