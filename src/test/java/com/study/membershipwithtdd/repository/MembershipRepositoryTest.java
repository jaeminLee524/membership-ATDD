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

    public static final String USER_ID = "userId";
    public static final int POINT = 10000;
    @Autowired
    private MembershipRepository membershipRepository;

    @Test
    void 멤버십등록_테스트() {
        // given
        final Membership membership = makeMembership(USER_ID, NAVER, POINT);

        // when
        final Membership savedMembership = membershipRepository.save(membership);

        // then
        assertThat(savedMembership.getId()).isNotNull();
        assertThat(savedMembership.getUserId()).isEqualTo(USER_ID);
        assertThat(savedMembership.getMembershipType()).isEqualTo(NAVER);
        assertThat(savedMembership.getPoint()).isEqualTo(POINT);
    }

    @Test
    void 멤버십조회_테스트() {
        // given
        Membership membership = makeMembership(USER_ID, NAVER, POINT);

        // when
        membershipRepository.save(membership);
        Membership resultMembership = membershipRepository.findByUserIdAndMembershipType(USER_ID, NAVER);

        // then
        assertThat(resultMembership).isNotNull();
        assertThat(resultMembership.getId()).isNotNull();
        assertThat(resultMembership.getUserId()).isEqualTo(USER_ID);
        assertThat(resultMembership.getMembershipType()).isEqualTo(NAVER);
        assertThat(resultMembership.getPoint()).isEqualTo(POINT);
    }

    @Test
    void 멤버십조회_사이즈_0() {
        // given
        // when
        List<Membership> membershipList = membershipRepository.findAllByUserId(USER_ID);

        // then
        assertThat(membershipList.size()).isEqualTo(0);
    }

    @Test
    void 멤버십조회_사이즈_2() {
        // given
        final Membership naverMembership = makeMembership(USER_ID, NAVER, POINT);
        final Membership kakaoMembership = makeMembership(USER_ID, KAKAO, POINT);

        membershipRepository.saveAll(Arrays.asList(naverMembership, kakaoMembership));

        // when
        List<Membership> result = membershipRepository.findAllByUserId(USER_ID);

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
            .userId(USER_ID)
            .membershipType(NAVER)
            .point(POINT)
            .build();

        final Membership savedMembership = membershipRepository.save(membership);

        membershipRepository.deleteById(savedMembership.getId());
    }
}
