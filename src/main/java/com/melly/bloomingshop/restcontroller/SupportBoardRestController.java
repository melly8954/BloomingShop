package com.melly.bloomingshop.restcontroller;

import com.melly.bloomingshop.common.ResponseController;
import com.melly.bloomingshop.common.ResponseDto;
import com.melly.bloomingshop.domain.SupportBoard;
import com.melly.bloomingshop.service.SupportBoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/support")
public class SupportBoardRestController implements ResponseController {
    private final SupportBoardService supportBoardService;

    @GetMapping("/list")
    public ResponseEntity<ResponseDto> getAllSupportBoards() {
        try{
            List<SupportBoard> allBoards = this.supportBoardService.getAllBoards();
            return makeResponseEntity(HttpStatus.OK, "모든 문의 게시글 반환 성공", allBoards);
        }catch (Exception ex){
            log.error(ex.getMessage(), ex);
            return makeResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러 : " + ex.getMessage(), null);
        }
    }
}
