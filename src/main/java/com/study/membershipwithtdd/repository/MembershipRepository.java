package com.study.membershipwithtdd.repository;

import com.study.membershipwithtdd.domain.membership.Membership;
import com.study.membershipwithtdd.domain.membership.Membership.MembershipType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MembershipRepository extends JpaRepository<Membership, Long> {

    Membership findByUserIdAndMembershipType(String userId, MembershipType membershipType);
}
