package com.study.membershipwithtdd.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.study.membershipwithtdd.domain.membership.Membership;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest //JPA Repository 에 대한 빈들을 등록하여 단위 테스트의 작성을 용이하게 함
public class MembershipRepositoryTest {

    @Autowired
    private MembershipRepository membershipRepository;

    @Test
    void 멤버십등록() {
        //given
        Membership membership = Membership.builder()
            .userId("userId")
            .membershipName("네이버")
            .point(10000)
            .build();

        // when
        Membership savedMembership = membershipRepository.save(membership);

        // then
        assertThat(savedMembership.getId()).isNotNull();
        assertThat(savedMembership.getUserId()).isEqualTo("userId");
        assertThat(savedMembership.getMembershipName()).isEqualTo("네이버");
        assertThat(savedMembership.getPoint()).isEqualTo(10000);
    }
}
