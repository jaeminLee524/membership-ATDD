package com.study.membershipwithtdd.domain.membership;

import static com.study.membershipwithtdd.common.response.MembershipErrorResult.DUPLICATED_MEMBERSHIP_REGISTER;
import static com.study.membershipwithtdd.common.response.MembershipErrorResult.MEMBERSHIP_NOT_FOUND;
import static com.study.membershipwithtdd.common.response.MembershipErrorResult.NOT_MEMBERSHIP_OWNER;

import com.study.membershipwithtdd.common.exception.MembershipException;
import com.study.membershipwithtdd.common.response.MembershipErrorResult;
import com.study.membershipwithtdd.domain.membership.Membership.MembershipType;
import com.study.membershipwithtdd.interfaces.MembershipDto.MembershipAddResponse;
import com.study.membershipwithtdd.interfaces.MembershipDto.MembershipDetailResponse;
import com.study.membershipwithtdd.repository.MembershipRepository;
import com.study.membershipwithtdd.service.point.PointService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MembershipService {

    private final MembershipRepository membershipRepository;
    private final PointService ratePointService;

    public MembershipAddResponse addMembership(String userId, MembershipType membershipType, Integer point) {
        Membership findMembership = membershipRepository.findByUserIdAndMembershipType(userId, membershipType);
        if (!ObjectUtils.isEmpty(findMembership)) {
            throw new MembershipException(DUPLICATED_MEMBERSHIP_REGISTER);
        }

        Membership membership = Membership.builder()
            .userId(userId)
            .membershipType(membershipType)
            .point(point)
            .build();

        Membership savedMembership = membershipRepository.save(membership);

        return MembershipAddResponse.builder()
            .id(savedMembership.getId())
            .membershipType(savedMembership.getMembershipType())
            .build();
    }

    public List<MembershipDetailResponse> getMembershipList(String userId) {
        final List<Membership> findMembershipList = membershipRepository.findAllByUserId(userId);

        return findMembershipList.stream()
            .map(MembershipDetailResponse::of)
            .collect(Collectors.toList());
    }

    public MembershipDetailResponse getMembership(Long membershipId, String userId) {
        final Membership findMembership = membershipRepository.findById(membershipId).orElseThrow(
            () -> new MembershipException(MEMBERSHIP_NOT_FOUND)
        );

        if (!findMembership.getUserId().equals(userId)) {
            throw new MembershipException(MembershipErrorResult.NOT_MEMBERSHIP_OWNER);
        }

        return MembershipDetailResponse.of(findMembership);
    }

    public void removeMembership(final Long membershipId, final String userId) {
        final Membership findMembership = membershipRepository.findById(membershipId).orElseThrow(
            () -> new MembershipException(MEMBERSHIP_NOT_FOUND)
        );

        if (!findMembership.getUserId().equals(userId)) {
            throw new MembershipException(NOT_MEMBERSHIP_OWNER);
        }

        membershipRepository.deleteById(membershipId);
    }

    @Transactional
    public void accumulateMembershipPoint(Long membershipId, String userId, int point) {
        final Membership findMembership = membershipRepository.findById(membershipId).orElseThrow(
            () -> new MembershipException(MEMBERSHIP_NOT_FOUND));

        if (!findMembership.getUserId().equals(userId)) {
            throw new MembershipException(NOT_MEMBERSHIP_OWNER);
        }

        final int additionalAmount = ratePointService.calculateAmount(point);

        findMembership.setPoint(additionalAmount);
    }
}
