package com.melly.bloomingshop.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
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
    @Column(name="role_id")
    private int roleId;
    private String status;
    @Column(name="create_date")
    private LocalDate createdDate;
    @Column(name="last_login")
    private LocalDate lastLogin;
    @Column(name="disabled_date")
    private LocalDate disabledDate;
    @Column(name="deleted_date")
    private LocalDate deletedDate;

}
