package com.melly.bloomingshop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Entity
@Table(name = "product_tbl")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

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

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "product_category", // 연결 테이블 이름
            joinColumns = @JoinColumn(name = "product_id"), // product_id 외래키
            inverseJoinColumns = @JoinColumn(name = "category_id") // category_id 외래키
    )
    private Set<Category> categories;
}

