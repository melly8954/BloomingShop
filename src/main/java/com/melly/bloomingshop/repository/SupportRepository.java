package com.melly.bloomingshop.repository;

import com.melly.bloomingshop.domain.Support;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupportRepository extends JpaRepository<Support, Long> {
    Page<Support> findByDeletedFlagFalse(Pageable pageable);

    // 글제목과 deletedFlag 가 false인 경우
    Page<Support> findByTitleContainingAndDeletedFlagFalse(String title, Pageable pageable);

    // 게시글 id로 
}
