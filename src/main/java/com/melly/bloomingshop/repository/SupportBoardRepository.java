package com.melly.bloomingshop.repository;

import com.melly.bloomingshop.domain.Product;
import com.melly.bloomingshop.domain.SupportBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public interface SupportBoardRepository extends JpaRepository<SupportBoard, Long> {
    @NonNull
    Page<SupportBoard> findAll(Pageable pageable);

    // 글제목과 deletedFlag 가 false인 경우
    Page<SupportBoard> findByTitleContainingAndDeletedFlagFalse(String title, Pageable pageable);
}
