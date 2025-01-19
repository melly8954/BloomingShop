package com.melly.bloomingshop.domain;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "product_tbl")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "size", nullable = false, length = 10)
    private String size;

    @Column(name = "image_url", length = 255)
    private String imageUrl;

    @Column(name = "description")
    private String description;

    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @Column(name = "updated_date", nullable = false)
    private LocalDateTime updatedDate;

    // 엔티티가 영속화되기 전에 현재 시간을 자동으로 설정하는 메서드
    @PrePersist
    public void prePersist() {
        if (this.createdDate == null) {
            this.createdDate = LocalDateTime.now();
        }
    }
}

