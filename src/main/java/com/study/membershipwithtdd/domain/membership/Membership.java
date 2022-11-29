package com.study.membershipwithtdd.domain.membership;

import static javax.persistence.EnumType.*;
import static javax.persistence.GenerationType.IDENTITY;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Membership {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String userId;

    @Column(nullable = false)
    @Enumerated(STRING)
    private MembershipType membershipType;

    @Column(nullable = false)
    @ColumnDefault("0")
    @Setter
    private Integer point;

    @CreationTimestamp
    @Column(nullable = false, length = 20, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(length = 20)
    private LocalDateTime updatedAt;

    /**
     * 멤버십의 타입을 정의합니다.
     * 네이버, 라인, 카카오 의 타입을 갖고 있습니다.
     */
    @Getter
    @AllArgsConstructor
    public enum MembershipType {
        NAVER("네이버"),
        LINE("라인"),
        KAKAO("카카오"),
        ;
        private String name;
    }
}


