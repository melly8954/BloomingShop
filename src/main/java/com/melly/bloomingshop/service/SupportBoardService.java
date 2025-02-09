package com.melly.bloomingshop.service;

import com.melly.bloomingshop.admin.service.FileUploadService;
import com.melly.bloomingshop.domain.SupportBoard;
import com.melly.bloomingshop.dto.request.SupportBoardRegister;
import com.melly.bloomingshop.repository.SupportBoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SupportBoardService {
    private final SupportBoardRepository supportBoardRepository;
    private final FileUploadService fileUploadService;

    // 모든 게시글 목록 가져오기
    public Page<SupportBoard> getAllBoards(Pageable pageable, String title) {
        Page<SupportBoard> boards;
        if (title != null && !title.isEmpty()) {
            return supportBoardRepository.findByTitleContainingAndDeletedFlagFalse(title, pageable);
        } else {
            return supportBoardRepository.findByDeletedFlagFalse(pageable);
        }
    }

    @Transactional
    // 게시글 등록
    public SupportBoard registerBoard(SupportBoardRegister supportBoardRegister, List<MultipartFile> attachments) {
        try {
            // 파일 첨부가 있으면 파일 저장
            if (attachments != null && !attachments.isEmpty()) {
                for (MultipartFile attachment : attachments) {
                    // 파일 저장 처리
                    String filePath = fileUploadService.saveSupportImage(attachment);
                    supportBoardRegister.setImageUrl(filePath);
                }
            }
            SupportBoard supportBoard = SupportBoard.builder()
                    .title(supportBoardRegister.getTitle())
                    .content(supportBoardRegister.getContent())
                    .imageUrl(supportBoardRegister.getImageUrl())
                    .isSecret(supportBoardRegister.getIsSecret())
                    .password(supportBoardRegister.getPassword())
                    .authorName(supportBoardRegister.getAuthorName()).build();

            // 게시글 저장
            SupportBoard save = supportBoardRepository.save(supportBoard);
            return save;
        } catch (Exception e) {
            log.error("게시글 등록 실패", e);
            throw new RuntimeException("게시글 등록 중 오류가 발생했습니다.", e);
        }
    }
}
