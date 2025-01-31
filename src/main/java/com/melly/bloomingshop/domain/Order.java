package com.melly.bloomingshop.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "order_tbl")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")  // 컬럼명 명시
    private Long orderId;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = true)
    private User userId;  // 회원 주문을 위한 유저 정보

    @Column(name="guest_id")
    private String guestId;

    @ManyToOne
    @JoinColumn(name = "address_id", referencedColumnName = "address_id")
    private Address addressId;  // 주소 테이블과의 관계

    @Column(name = "total_price", nullable = false)
    private BigDecimal totalPrice;

    @Column(name = "order_date")
    private
    LocalDateTime orderDate;

    // 엔티티가 영속화되기 전에 현재 시간을 자동으로 설정하는 메서드
    @PrePersist
    public void prePersist() {
        if (this.orderDate == null) {
            this.orderDate = LocalDateTime.now();
        }
    }

    @Column(name = "delivery_status")
    private String deliveryStatus;

    @Column(name = "payment_status")
    private String paymentStatus;

    @Column(name = "shipping_address_non_member")
    private String shippingAddressNonMember;  // 비회원 주소

    @Column(name = "payment_method", nullable = false)
    private String paymentMethod;

    @Column(name = "delivery_status_updated")
    private LocalDateTime deliveryStatusUpdated;

    @Column(name = "payment_status_updated")
    private LocalDateTime paymentStatusUpdated;

    @Column(name = "deleted_flag")
    private boolean deletedFlag;

    @Column(name = "deleted_date")
    private LocalDateTime deletedDate;

    public void changeAddress(Address address) {
        this.addressId = address;
    }

    public void changeShippingAddressNonMember(String shippingAddressNonMember){
        this.shippingAddressNonMember = shippingAddressNonMember;
    }

    public void changeTotalPrice(BigDecimal totalPrice){
        this.totalPrice = totalPrice;
    }

    public void changeUser (User user) {
        this.userId = user;
    }

    public void changeGuest(String guestId) {
        this.guestId = guestId;
    }

    public void changePaymentStatus(String newStatus){
        this.paymentStatus = newStatus;
    }

    // 엔티티가 업데이트되기 전에 호출되어 paymentStatusUpdated 시간을 자동으로 설정
    @PreUpdate
    public void preUpdate() {
        if (this.paymentStatus != null) {
            this.paymentStatusUpdated = LocalDateTime.now();
        }
    }

    // 주문 취소 (soft deleted)
    public void changeDeletedFlag(boolean deletedFlag) {
        this.deletedFlag = deletedFlag;
    }
    public void changeDeletedDate(LocalDateTime deletedDate) {
        this.deletedDate = deletedDate;
    }

}
