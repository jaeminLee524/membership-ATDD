package com.study.membershipwithtdd.repository;

import static com.study.membershipwithtdd.domain.membership.Membership.MembershipType.KAKAO;
import static com.study.membershipwithtdd.domain.membership.Membership.MembershipType.NAVER;
import static org.assertj.core.api.Assertions.assertThat;

import com.study.membershipwithtdd.domain.membership.Membership;
import com.study.membershipwithtdd.domain.membership.Membership.MembershipType;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest //JPA Repository 에 대한 빈들을 등록하여 단위 테스트의 작성을 용이하게 함
public class MembershipRepositoryTest {

    @Autowired
    private MembershipRepository membershipRepository;

    @Test
    void 멤버십등록_테스트() {
        // given
        Membership membership = makeMembership("userId", NAVER, 10000);

        // when
        Membership savedMembership = membershipRepository.save(membership);

        // then
        assertThat(savedMembership.getId()).isNotNull();
        assertThat(savedMembership.getUserId()).isEqualTo("userId");
        assertThat(savedMembership.getMembershipType()).isEqualTo(NAVER);
        assertThat(savedMembership.getPoint()).isEqualTo(10000);
    }

    @Test
    void 멤버십조회_테스트() {
        // given
        Membership membership = makeMembership("userId", NAVER, 10000);

        // when
        membershipRepository.save(membership);
        Membership resultMembership = membershipRepository.findByUserIdAndMembershipType("userId", NAVER);

        // then
        assertThat(resultMembership).isNotNull();
        assertThat(resultMembership.getId()).isNotNull();
        assertThat(resultMembership.getUserId()).isEqualTo("userId");
        assertThat(resultMembership.getMembershipType()).isEqualTo(NAVER);
        assertThat(resultMembership.getPoint()).isEqualTo(10000);
    }

    @Test
    void 멤버십조회_사이즈_0() {
        // given
        // when
        List<Membership> membershipList = membershipRepository.findAllByUserId("userId");

        // then
        assertThat(membershipList.size()).isEqualTo(0);
    }

    @Test
    void 멤버십조회_사이즈_2() {
        // given
        final Membership naverMembership = makeMembership("userId", NAVER, 10000);
        final Membership kakaoMembership = makeMembership("userId", KAKAO, 10000);

        membershipRepository.saveAll(Arrays.asList(naverMembership, kakaoMembership));

        // when
        List<Membership> result = membershipRepository.findAllByUserId("userId");

        // then
        assertThat(result.size()).isEqualTo(2);
    }

    private Membership makeMembership(String userId, MembershipType membershipType, Integer point) {
        return Membership.builder()
            .userId(userId)
            .membershipType(membershipType)
            .point(point)
            .build();
    }

    @Test
    void 멤버십_추가후_삭제() {
        // given
        final Membership membership = Membership.builder()
            .userId("userId")
            .membershipType(NAVER)
            .point(10000)
            .build();

        final Membership savedMembership = membershipRepository.save(membership);

        membershipRepository.deleteById(savedMembership.getId());
    }
}
