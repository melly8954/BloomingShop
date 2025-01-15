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
    private String address;
    @ManyToOne
    @JoinColumn(name="role_id")
    private Role roleId;
    private StatusType status;
    @Column(name="created_date")
    private LocalDateTime createdDate;
    @Column(name="last_login")
    private LocalDateTime lastLogin;
    @Column(name="disabled_date")
    private LocalDateTime disabledDate;
    @Column(name="deleted_date")
    private LocalDateTime deletedDate;

    // 엔티티가 영속화되기 전에 현재 시간을 자동으로 설정하는 메서드
    @PrePersist
    public void prePersist() {
        if (this.createdDate == null) {
            this.createdDate = LocalDateTime.now();
        }
    }
}
