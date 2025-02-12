package com.melly.bloomingshop.admin.restcontroller;

import com.melly.bloomingshop.common.ResponseController;
import com.melly.bloomingshop.common.ResponseDto;
import com.melly.bloomingshop.dto.request.SupportAnswer;
import com.melly.bloomingshop.service.SupportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class SupportManageController implements ResponseController {
    private final SupportService supportService;

    @PostMapping("/board/support/{boardId}/answers")
    public ResponseEntity<ResponseDto> answerSupport(@PathVariable Long boardId, @RequestBody SupportAnswer answer) {
        try{
            if(boardId == null || boardId <= 0){
                return makeResponseEntity(HttpStatus.BAD_REQUEST, "해당 ID의 게시글은 존재하지 않습니다.", null);
            }
            this.supportService.insertAnswer(boardId,answer);
            return makeResponseEntity(HttpStatus.OK, "문의글 답변 저장 성공", true);
        }catch (Exception ex){
            log.error(ex.getMessage(), ex);
            return makeResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러 : " + ex.getMessage(), null);
        }
    }


}
