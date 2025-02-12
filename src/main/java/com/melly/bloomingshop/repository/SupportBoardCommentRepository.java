package com.melly.bloomingshop.repository;

import com.melly.bloomingshop.domain.SupportBoardComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupportBoardCommentRepository extends JpaRepository<SupportBoardComment, Long> {
    // 게시판 ID에 해당하는 댓글 목록을 페이징 처리하여 반환
    Page<SupportBoardComment> findByBoardId(Long boardId, Pageable pageable);
}
