package com.study.membershipwithtdd.interfaces;

import com.study.membershipwithtdd.domain.membership.Membership;
import com.study.membershipwithtdd.domain.membership.Membership.MembershipType;
import java.time.LocalDateTime;
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
    public static class MembershipAddRequest {
        @NotNull
        @Min(0) // 양수를 의미
        private final Integer point;
        @NotNull
        private final MembershipType membershipType;
    }

    @Getter
    @Builder
    @RequiredArgsConstructor
    @NoArgsConstructor(force = true)
    public static class PointAccumulateRequest {
        @NotNull
        private final Integer point;
    }

    /**
     * response
     */
    @Builder
    @Getter
    public static class MembershipAddResponse {
        private Long id;
        private MembershipType membershipType;
    }

    @Builder
    @Getter
    public static class MembershipDetailResponse {
        private Long id;
        private String userId;
        private MembershipType membershipType;
        private Integer point;
        private LocalDateTime createdAt;

        public static MembershipDetailResponse of(Membership membership) {
            return MembershipDetailResponse.builder()
                .id(membership.getId())
                .userId(membership.getUserId())
                .membershipType(membership.getMembershipType())
                .point(membership.getPoint())
                .createdAt(membership.getCreatedAt())
                .build();
        }
    }
}
