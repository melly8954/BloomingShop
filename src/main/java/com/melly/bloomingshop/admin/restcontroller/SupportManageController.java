package com.melly.bloomingshop.admin.restcontroller;

import com.melly.bloomingshop.admin.service.SupportManageService;
import com.melly.bloomingshop.common.ResponseController;
import com.melly.bloomingshop.common.ResponseDto;
import com.melly.bloomingshop.domain.Support;
import com.melly.bloomingshop.dto.request.SupportAnswer;
import com.melly.bloomingshop.dto.response.SupportResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class SupportManageController implements ResponseController {
    private final SupportManageService supportManageService;

    @GetMapping("/board/support/list")
    public ResponseEntity<ResponseDto> getAllSupportBoards(
            @RequestParam(required = false, defaultValue = "") String title, Pageable pageable,
            @RequestParam String sort, @RequestParam(defaultValue = "desc") String order ){
        try{
            SupportResponse<Support> supportResponse;
            Page<Support> allBoards = this.supportManageService.getAllBoards(pageable,title,sort,order);
            supportResponse = new SupportResponse<>(
                    allBoards.getContent(),
                    allBoards.getTotalElements(),
                    allBoards.getTotalPages(),
                    allBoards.getNumber(),
                    allBoards.getSize()
            );
            return makeResponseEntity(HttpStatus.OK, "모든 문의 게시글 반환 성공", supportResponse);
        }catch (Exception ex){
            log.error(ex.getMessage(), ex);
            return makeResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러 : " + ex.getMessage(), null);
        }
    }

    // 문의글 답변 API
    @PostMapping("/board/support/{boardId}/answers")
    public ResponseEntity<ResponseDto> answerSupport(@PathVariable Long boardId, @RequestBody SupportAnswer answer) {
        try{
            if(boardId == null || boardId <= 0){
                return makeResponseEntity(HttpStatus.BAD_REQUEST, "해당 ID의 게시글은 존재하지 않습니다.", null);
            }
            this.supportManageService.insertAnswer(boardId,answer);
            return makeResponseEntity(HttpStatus.OK, "문의글 답변 저장 성공", true);
        }catch (Exception ex){
            log.error(ex.getMessage(), ex);
            return makeResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러 : " + ex.getMessage(), null);
        }
    }

    // 게시글 상태 변경(논리삭제 및 복구) API
    @PatchMapping("/board/support/{boardId}/status")
    public ResponseEntity<ResponseDto> updateSupportStatus(@PathVariable Long boardId) {
        try{
            if(boardId == null || boardId <= 0){
                return makeResponseEntity(HttpStatus.BAD_REQUEST, "해당 ID의 게시글은 존재하지 않습니다.", null);
            }
            this.supportManageService.updateDeleteFlag(boardId);
            return makeResponseEntity(HttpStatus.OK, "게시글 상태 변경 완료", true);
        } catch (Exception ex){
            log.error(ex.getMessage(), ex);
            return makeResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러 : " + ex.getMessage(), null);
        }
    }


}
