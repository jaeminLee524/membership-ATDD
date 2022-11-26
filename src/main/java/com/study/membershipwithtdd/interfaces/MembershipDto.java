package com.study.membershipwithtdd.interfaces;

import com.study.membershipwithtdd.domain.membership.Membership.MembershipType;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

public class MembershipDto {

    /**
     * request
     */
    @Builder
    @Getter
    @RequiredArgsConstructor
    @NoArgsConstructor(force = true)
    public static class MembershipRequest {
        @NotNull
        @Min(0) // 양수를 의미
        private final Integer point;
        @NotNull
        private final MembershipType membershipType;
    }

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
