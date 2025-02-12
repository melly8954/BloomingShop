package com.melly.bloomingshop.service;

import com.melly.bloomingshop.admin.service.FileUploadService;
import com.melly.bloomingshop.domain.SupportBoard;
import com.melly.bloomingshop.dto.request.SupportBoardRegister;
import com.melly.bloomingshop.repository.SupportBoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SupportBoardService {
    private final SupportBoardRepository supportBoardRepository;
    private final FileUploadService fileUploadService;

    // 모든 게시글 목록 가져오기
    public Page<SupportBoard> getAllBoards(Pageable pageable, String title) {
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
                    .viewQty(0)
                    .authorName(supportBoardRegister.getAuthorName())
                    .isSecret(supportBoardRegister.getIsSecret())
                    .password(supportBoardRegister.getPassword())
                    .isAnswer(false)
                    .answerContent(null)
                    .build();

            // 게시글 저장
            SupportBoard save = supportBoardRepository.save(supportBoard);
            return save;
        } catch (Exception e) {
            log.error("게시글 등록 실패", e);
            throw new RuntimeException("게시글 등록 중 오류가 발생했습니다.", e);
        }
    }

    public boolean checkBoardPassword(Long boardId, String password) {
        SupportBoard board = supportBoardRepository.findById(boardId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."));

        if (!board.getIsSecret()) {
            return true; // 비밀글이 아니면 바로 성공 반환
        }

        else if (board.getPassword() != null && board.getPassword().equals(password)) {
            return true; // 비밀번호 일치
        } else {
            return false; // 비밀번호 불일치
        }
    }

    public SupportBoard findByBoardId(Long boardId) {
        Optional<SupportBoard> find = this.supportBoardRepository.findById(boardId);
        if(find.isPresent()) {
            return find.get();
        }
        return null;
    }
}
