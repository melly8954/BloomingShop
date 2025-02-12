package com.melly.bloomingshop.repository;

import com.melly.bloomingshop.domain.Support;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupportRepository extends JpaRepository<Support, Long> {
    // 모든 검색
    Page<Support> findAll(Pageable pageable);

    // 글제목 검색
    Page<Support> findByTitleContaining(String title, Pageable pageable);

    // deletedFlag 가 false인 경우
    Page<Support> findByDeletedFlagFalse(Pageable pageable);

    // 글제목 검색과 deletedFlag 가 false인 경우
    Page<Support> findByTitleContainingAndDeletedFlagFalse(String title, Pageable pageable);
}
