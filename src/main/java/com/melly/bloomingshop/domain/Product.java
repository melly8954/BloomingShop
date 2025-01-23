package com.melly.bloomingshop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.melly.bloomingshop.admin.dto.ProductModifyRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Entity
@AllArgsConstructor
@Builder
@Table(name = "product_tbl")
public class Product {
    public Product() {
    }
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

    @Column(name = "deleted_flag")
    private Boolean deletedFlag = false;

    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @Column(name = "updated_date", nullable = false)
    private LocalDateTime updatedDate;

    @Column(name = "deleted_Date", nullable = false)
    private LocalDateTime deletedDate;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "product_category", // 연결 테이블 이름
            joinColumns = @JoinColumn(name = "product_id"), // product_id 외래키
            inverseJoinColumns = @JoinColumn(name = "category_id") // category_id 외래키
    )
    private Set<Category> categories;



    // 엔티티가 영속화되기 전에 현재 시간을 자동으로 설정하는 메서드
    @PrePersist
    public void prePersist() {
        if (this.createdDate == null) {
            this.createdDate = LocalDateTime.now();
        }
    }

    // 다음은 setter 역할의 메서드
    public void copyFields(ProductModifyRequest dto) {
        this.name = dto.getName();
        this.price = dto.getPrice();
        this.size = dto.getSize();
        this.description = dto.getDescription();
        // categories는 여기서 제외
    }

    public void setCategories(Set<Category> categories) {
        this.categories.clear(); // 기존 데이터를 클리어
        if (categories != null) {
            this.categories.addAll(categories); // 새 데이터 추가
        }
    }

    public void modifyImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void modifyUpdatedDate(LocalDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }

    public void modifyDeletedDate(LocalDateTime deletedDate) {
        this.deletedDate = deletedDate;
    }

    public void changeDeletedFlag(Boolean deletedFlag) {
        this.deletedFlag = deletedFlag;
    }

}

