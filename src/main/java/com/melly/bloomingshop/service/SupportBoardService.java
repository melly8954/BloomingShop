package com.melly.bloomingshop.service;

import com.melly.bloomingshop.domain.SupportBoard;
import com.melly.bloomingshop.repository.SupportBoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SupportBoardService {
    private final SupportBoardRepository supportBoardRepository;

    // 모든 게시글 목록 가져오기
    public List<SupportBoard> getAllBoards() {
        return supportBoardRepository.findAll();
    }
}
