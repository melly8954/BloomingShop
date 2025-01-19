package com.melly.bloomingshop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name="user_tbl")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="login_id")
    private String loginId;
    private String password;
    private String name;
    private String gender;
    private LocalDate birthdate;
    private String email;
    @Column(name="phone_number")
    private String phoneNumber;
    @ManyToOne
    @JoinColumn(name="role_id")
    private Role roleId;
    @Enumerated(EnumType.STRING)    // JPA는 enum 타입을 기본적으로 정수(ordinal)로 매핑하므로 해당 어노테이션 사용하지 않으면 0,1,2 등으로 db에 저장됨
    private StatusType status;
    @Column(name="created_date")
    private LocalDateTime createdDate;
    @Column(name="last_login")
    private LocalDateTime lastLogin;
    @Column(name="disabled_date")
    private LocalDateTime disabledDate;
    @Column(name="deleted_date")
    private LocalDateTime deletedDate;

    // OAuth를 위해 구성한 추가 필드 2개
    private String provider;
    @Column(name="provider_id")
    private String providerId;

    // 엔티티가 영속화되기 전에 현재 시간을 자동으로 설정하는 메서드
    @PrePersist
    public void prePersist() {
        if (this.createdDate == null) {
            this.createdDate = LocalDateTime.now();
        }
    }
}
