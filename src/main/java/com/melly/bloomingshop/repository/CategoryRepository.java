package com.melly.bloomingshop.repository;

import com.melly.bloomingshop.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    // 카테고리명으로 카테고리 찾기
    Category findByName(String name);
}
