package com.melly.bloomingshop.repository;

import com.melly.bloomingshop.domain.SupportBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupportBoardRepository extends JpaRepository<SupportBoard, Long> {
}
