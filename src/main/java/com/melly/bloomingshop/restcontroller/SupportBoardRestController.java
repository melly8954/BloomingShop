package com.melly.bloomingshop.restcontroller;

import com.melly.bloomingshop.common.ResponseController;
import com.melly.bloomingshop.common.ResponseDto;
import com.melly.bloomingshop.domain.SupportBoard;
import com.melly.bloomingshop.dto.request.SupportBoardPassword;
import com.melly.bloomingshop.dto.request.SupportBoardRegister;
import com.melly.bloomingshop.dto.response.SupportBoardResponse;
import com.melly.bloomingshop.service.SupportBoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board/support")
public class SupportBoardRestController implements ResponseController {
    private final SupportBoardService supportBoardService;

    @GetMapping("/list")
    public ResponseEntity<ResponseDto> getAllSupportBoards(
            @RequestParam(required = false, defaultValue = "") String title,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,  // 정렬 기준
            @RequestParam(defaultValue = "desc") String order) {  // 정렬 방향
        try{
            // 페이지 번호와 크기 설정
            if (page <= 0) page = 1;
            if (size <= 0) size = 10;

            Sort sortBy = Sort.by(Sort.Order.by(sort));
            sortBy = order.equalsIgnoreCase("desc") ? sortBy.descending() : sortBy.ascending();
            Pageable pageable = PageRequest.of(page - 1, size, sortBy);

            SupportBoardResponse<SupportBoard> supportBoardResponse;

            Page<SupportBoard> allBoards = this.supportBoardService.getAllBoards(pageable,title);
            supportBoardResponse = new SupportBoardResponse<>(
                    allBoards.getContent(),
                    allBoards.getTotalElements(),
                    allBoards.getTotalPages(),
                    allBoards.getNumber(),
                    allBoards.getSize()
            );
            return makeResponseEntity(HttpStatus.OK, "모든 문의 게시글 반환 성공", supportBoardResponse);
        }catch (Exception ex){
            log.error(ex.getMessage(), ex);
            return makeResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러 : " + ex.getMessage(), null);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseDto> registerBoard(
            @RequestPart("boardData") SupportBoardRegister supportBoardRegister,  // JSON 데이터
            @RequestPart(value = "attachments[]", required = false) List<MultipartFile> attachments) {  // 파일 리스트
        try {
            SupportBoard insert = this.supportBoardService.registerBoard(supportBoardRegister, attachments);

            return makeResponseEntity(HttpStatus.OK, "문의 게시글 등록 완료", insert);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            return makeResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러 : " + ex.getMessage(), null);
        }
    }

    @PostMapping("/{boardId}/check-password")
    public ResponseEntity<ResponseDto> checkPassword(@PathVariable Long boardId, @RequestBody SupportBoardPassword supportBoardPassword){
        try {
            boolean isChecked = this.supportBoardService.checkBoardPassword(boardId, supportBoardPassword.getPassword());
            if(isChecked){
                return makeResponseEntity(HttpStatus.OK, "비밀번호 확인 완료", isChecked);
            }else{
                log.error("비밀번호가 틀렸습니다.");
                return makeResponseEntity(HttpStatus.OK, "비밀번호가 틀렸습니다.", isChecked);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            return makeResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러 : " + ex.getMessage(), null);
        }
    }

    @GetMapping("/{boardId}/details")
    public ResponseEntity<ResponseDto> getBoardDetail(@PathVariable Long boardId) {
        try{
            if(boardId == null || boardId <= 0){
                return makeResponseEntity(HttpStatus.BAD_REQUEST, "해당 ID의 게시글은 존재하지 않습니다.", null);
            }
            SupportBoard detail = this.supportBoardService.findByBoardId(boardId);
            return makeResponseEntity(HttpStatus.OK, "해당 게시글 조회 완료", detail);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            return makeResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러 : " + ex.getMessage(), null);
        }
    }
}
