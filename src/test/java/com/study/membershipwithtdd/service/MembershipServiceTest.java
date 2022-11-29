package com.study.membershipwithtdd.service;

import static com.study.membershipwithtdd.common.response.MembershipErrorResult.MEMBERSHIP_NOT_FOUND;
import static com.study.membershipwithtdd.common.response.MembershipErrorResult.NOT_MEMBERSHIP_OWNER;
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
import com.study.membershipwithtdd.common.response.MembershipErrorResult;
import com.study.membershipwithtdd.common.exception.MembershipException;
import com.study.membershipwithtdd.interfaces.MembershipDto.MembershipAddResponse;
import com.study.membershipwithtdd.interfaces.MembershipDto.MembershipDetailResponse;
import com.study.membershipwithtdd.repository.MembershipRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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
    private final Long membershipId = -1L;

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
        MembershipAddResponse savedMembership = target.addMembership(userId, membershipType, point);

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

    @Test
    void 멤버십목록조회() {
        // given
        doReturn(Arrays.asList(
            Membership.builder().build(),
            Membership.builder().build(),
            Membership.builder().build()
        )).when(membershipRepository).findAllByUserId("userId");

        // when
        final List<MembershipDetailResponse> result = target.getMembershipList(userId);

        // then
        assertThat(result.size()).isEqualTo(3);
    }

    @Test
    void 멤버십상제조회_실패_존재하지않음() {
        // given
        doReturn(Optional.empty()).when(membershipRepository).findById(membershipId);

        // when
        final MembershipException result = assertThrows(MembershipException.class, () -> target.getMembership(membershipId, userId));

        // then
        assertThat(result.getMembershipErrorResult()).isEqualTo(MEMBERSHIP_NOT_FOUND);
    }

    @Test
    void 멤버십상세조회_실패_본인이아님() {
        // given
        doReturn(Optional.empty()).when(membershipRepository).findById(membershipId);

        // when
        final MembershipException result = assertThrows(MembershipException.class, () -> target.getMembership(membershipId, "notOwner"));

        // then
        assertThat(result.getMembershipErrorResult()).isEqualTo(MEMBERSHIP_NOT_FOUND);
    }

    @Test
    void 멤버십상세조회_성공() {
        // given
        doReturn(Optional.of(membership())).when(membershipRepository).findById(membershipId);

        // when
        final MembershipDetailResponse result = target.getMembership(membershipId, userId);

        // then
        assertThat(result.getMembershipType()).isEqualTo(NAVER);
        assertThat(result.getPoint()).isEqualTo(point);
    }

    @Test
    void 멤버십삭제_실패_존재하지않음() {
        // given
        doReturn(Optional.empty()).when(membershipRepository).findById(membershipId);

        // when
        final MembershipException result = assertThrows(MembershipException.class, () -> target.removeMembership(membershipId, userId));

        // then
        assertThat(result.getMembershipErrorResult()).isEqualTo(MEMBERSHIP_NOT_FOUND);
    }

    @Test
    void 멤버십삭제_실패_본인이아님() {
        // given
        final Membership membership = membership();
        doReturn(Optional.of(membership)).when(membershipRepository).findById(membershipId);

        // when
        final MembershipException result = assertThrows(MembershipException.class, () -> target.removeMembership(membershipId, "notOwner"));

        // then
        assertThat(result.getMembershipErrorResult()).isEqualTo(NOT_MEMBERSHIP_OWNER);
    }

    @Test
    void 멤버십삭제_성공() {
        // given
        Membership membership = membership();
        doReturn(Optional.of(membership)).when(membershipRepository).findById(membershipId);

        // when
        target.removeMembership(membershipId, userId);

        // then
    }


}
