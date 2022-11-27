package com.study.membershipwithtdd.domain.membership;

import static com.study.membershipwithtdd.common.response.MembershipErrorResult.DUPLICATED_MEMBERSHIP_REGISTER;
import static com.study.membershipwithtdd.common.response.MembershipErrorResult.MEMBERSHIP_NOT_FOUND;

import com.study.membershipwithtdd.common.exception.MembershipException;
import com.study.membershipwithtdd.common.response.MembershipErrorResult;
import com.study.membershipwithtdd.domain.membership.Membership.MembershipType;
import com.study.membershipwithtdd.interfaces.MembershipDto.MembershipAddResponse;
import com.study.membershipwithtdd.interfaces.MembershipDto.MembershipDetailResponse;
import com.study.membershipwithtdd.repository.MembershipRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
@RequiredArgsConstructor
public class MembershipService {

    private final MembershipRepository membershipRepository;

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
}
