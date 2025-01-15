package com.melly.bloomingshop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
    private String status;
    @Column(name="created_date")
    private LocalDateTime createdDate;
    @Column(name="last_login")
    private LocalDateTime lastLogin;
    @Column(name="disabled_date")
    private LocalDateTime disabledDate;
    @Column(name="deleted_date")
    private LocalDateTime deletedDate;

}
