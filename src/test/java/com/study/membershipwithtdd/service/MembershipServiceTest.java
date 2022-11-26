package com.study.membershipwithtdd.service;

import static com.study.membershipwithtdd.domain.membership.Membership.MembershipType.NAVER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.study.membershipwithtdd.domain.membership.Membership;
import com.study.membershipwithtdd.domain.membership.Membership.MembershipType;
import com.study.membershipwithtdd.domain.membership.MembershipService;
import com.study.membershipwithtdd.exception.MembershipErrorResult;
import com.study.membershipwithtdd.exception.MembershipException;
import com.study.membershipwithtdd.interfaces.MembershipDto;
import com.study.membershipwithtdd.interfaces.MembershipDto.MembershipResponse;
import com.study.membershipwithtdd.repository.MembershipRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MembershipServiceTest {

    @InjectMocks // 테스트 대상이므로 의존성이 주입되는 @InjectMocks
    private MembershipService target;
    @Mock // MembershipService는 MembershipRepository가 의존성이 있는 클래스이므로 가짜 객체를 주입해주는 @Mock
    private MembershipRepository membershipRepository;
    private final String userId = "userId";
    private final MembershipType membershipType = NAVER;
    private final Integer point = 10000;

    @Test
    void 멤버십등록_이미존재함_실패테스트() {
        // given
        doReturn(Membership.builder().build())
            .when(membershipRepository).findByUserIdAndMembershipType(userId, membershipType);

        // when
        MembershipException result = assertThrows(MembershipException.class, () -> target.addMembership(userId, membershipType, point));

        // then
        assertThat(result.getMembershipErrorResult()).isEqualTo(MembershipErrorResult.DUPLICATED_MEMBERSHIP_REGISTER);
    }



    @Test
    void 멤버십등록_성공_테스트() {
        // given
        doReturn(null).when(membershipRepository).findByUserIdAndMembershipType(userId, membershipType);
        doReturn(membership()).when(membershipRepository).save(any(Membership.class));

        // when
        MembershipResponse savedMembership = target.addMembership(userId, membershipType, point);

        // then
        Assertions.assertThat(savedMembership.getId()).isNotNull();
        Assertions.assertThat(savedMembership.getMembershipType()).isEqualTo(NAVER);

        // verify
        verify(membershipRepository, times(1)).findByUserIdAndMembershipType(userId, membershipType);
        verify(membershipRepository, times(1)).save(any(Membership.class));
    }

    private Membership membership() {
        return Membership.builder()
            .id(-1L)
            .userId(userId)
            .membershipType(membershipType)
            .point(point)
            .build();
    }
}
