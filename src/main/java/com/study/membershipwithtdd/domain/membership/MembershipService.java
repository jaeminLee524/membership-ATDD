package com.study.membershipwithtdd.domain.membership;

import static com.study.membershipwithtdd.exception.MembershipErrorResult.DUPLICATED_MEMBERSHIP_REGISTER;

import com.study.membershipwithtdd.domain.membership.Membership.MembershipType;
import com.study.membershipwithtdd.exception.MembershipErrorResult;
import com.study.membershipwithtdd.exception.MembershipException;
import com.study.membershipwithtdd.repository.MembershipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
@RequiredArgsConstructor
public class MembershipService {

    private final MembershipRepository membershipRepository;

    public Membership addMembership(String userId, MembershipType membershipType, Integer point) {
        Membership findMembership = membershipRepository.findByUserIdAndMembershipType(userId, membershipType);
        if (!ObjectUtils.isEmpty(findMembership)) {
            throw new MembershipException(DUPLICATED_MEMBERSHIP_REGISTER);
        }

        return null;
    }
}
