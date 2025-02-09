package com.melly.bloomingshop.service;

import com.melly.bloomingshop.domain.SupportBoard;
import com.melly.bloomingshop.repository.SupportBoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SupportBoardService {
    private final SupportBoardRepository supportBoardRepository;

    // 모든 게시글 목록 가져오기
    public Page<SupportBoard> getAllBoards(Pageable pageable, String title) {
        Page<SupportBoard> boards;
        if (title != null && !title.isEmpty()) {
            return supportBoardRepository.findByTitleContainingAndDeletedFlagFalse(title, pageable);
        } else {
            return supportBoardRepository.findAll(pageable);
        }
    }
}
