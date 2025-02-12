package com.melly.bloomingshop.service;

import com.melly.bloomingshop.admin.service.FileUploadService;
import com.melly.bloomingshop.domain.Support;
import com.melly.bloomingshop.dto.request.SupportAnswer;
import com.melly.bloomingshop.dto.request.SupportRegister;
import com.melly.bloomingshop.repository.SupportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SupportService {
    private final SupportRepository supportRepository;
    private final FileUploadService fileUploadService;

    // 삭제되지 않은 게시글 목록 가져오기
    public Page<Support> findByDeletedFlagFalse(Pageable pageable, String title) {
        if (title != null && !title.isEmpty()) {
            return supportRepository.findByTitleContainingAndDeletedFlagFalse(title, pageable);
        } else {
            return supportRepository.findByDeletedFlagFalse(pageable);
        }
    }

    @Transactional
    // 게시글 등록
    public Support registerBoard(SupportRegister supportRegister, List<MultipartFile> attachments) {
        try {
            // 파일 첨부가 있으면 파일 저장
            if (attachments != null && !attachments.isEmpty()) {
                for (MultipartFile attachment : attachments) {
                    // 파일 저장 처리
                    String filePath = fileUploadService.saveSupportImage(attachment);
                    supportRegister.setImageUrl(filePath);
                }
            }
            Support support = Support.builder()
                    .title(supportRegister.getTitle())
                    .content(supportRegister.getContent())
                    .imageUrl(supportRegister.getImageUrl())
                    .viewQty(0)
                    .authorName(supportRegister.getAuthorName())
                    .isSecret(supportRegister.getIsSecret())
                    .password(supportRegister.getPassword())
                    .isAnswer(false)
                    .answerContent(null)
                    .build();

            // 게시글 저장
            Support save = supportRepository.save(support);
            return save;
        } catch (Exception e) {
            log.error("게시글 등록 실패", e);
            throw new RuntimeException("게시글 등록 중 오류가 발생했습니다.", e);
        }
    }

    public boolean checkBoardPassword(Long boardId, String password) {
        Support board = supportRepository.findById(boardId)
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

    public Support findByBoardId(Long boardId) {
        Optional<Support> find = this.supportRepository.findById(boardId);
        if(find.isPresent()) {
            return find.get();
        }
        return null;
    }
}
