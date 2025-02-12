package com.melly.bloomingshop.admin.service;

import com.melly.bloomingshop.domain.Support;
import com.melly.bloomingshop.dto.request.SupportAnswer;
import com.melly.bloomingshop.repository.SupportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SupportManageService {
    private final SupportRepository supportRepository;

    // 문의글 답변 저장 비즈니스 로직
    public void insertAnswer(Long boardId, SupportAnswer answer) {
        Optional<Support> board = this.supportRepository.findById(boardId);
        board.ifPresent(b -> {
            b.changeIsAnswer(true);
            b.changeAnswerDate(LocalDateTime.now());
            b.changeAnswerContent(answer.getAnswer());
            supportRepository.save(b);
        });

    }

    // 게시글 상태 변경(논리삭제 및 복구)
    public void updateDeleteFlag(Long boardId) {
        Optional<Support> board = this.supportRepository.findById(boardId);
        board.ifPresent(b->{
            if(b.isDeletedFlag() == false){
                b.changeDeletedFlag(true);
                b.changeDeleted_date(LocalDateTime.now());
            } else {
                b.changeDeletedFlag(false);
                b.changeDeleted_date(null);
            }
            supportRepository.save(b);
        });
    }

    // 모든 게시글 가져오기
    public Page<Support> getAllBoards(Pageable pageable, String title, String sort, String order) {
        Sort.Direction direction = order.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sortBy = Sort.by(direction, sort);

        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sortBy);

        if (title != null && !title.isEmpty()) {
            return supportRepository.findByTitleContaining(title, sortedPageable);
        } else {
            return supportRepository.findAll(sortedPageable);
        }
    }
}
