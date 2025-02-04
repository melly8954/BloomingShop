package com.melly.bloomingshop.admin.restcontroller;

import com.melly.bloomingshop.admin.dto.UserPageResponseDto;
import com.melly.bloomingshop.admin.service.UserManageService;
import com.melly.bloomingshop.common.ResponseController;
import com.melly.bloomingshop.common.ResponseDto;
import com.melly.bloomingshop.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/user")
public class UserManageController implements ResponseController {
    private final UserManageService userManageService;

    // 모든 사용자 조회 API
    @GetMapping("/all-list")
    public ResponseEntity<ResponseDto> getAllUsers(@RequestParam int page) {
        // 페이지 번호가 1 이상인지 확인
        if (page < 1) {
            return makeResponseEntity(HttpStatus.BAD_REQUEST, "페이지 번호는 1 이상이어야 합니다.", null);
        }

        int size = 5;

        Pageable pageable = PageRequest.of(page - 1, size);  // 1-based index를 0-based로 변환

        try {
            Page<User> allUsers = this.userManageService.getAllUsers(pageable);

            // 페이징 정보 포함하여 응답
            return makeResponseEntity(HttpStatus.OK, "회원 목록 조회 성공",
                    new UserPageResponseDto<>(allUsers.getContent(), allUsers.getTotalElements(), allUsers.getTotalPages(), allUsers.getNumber()+1, allUsers.getSize()));
        } catch (Exception e) {
            log.error("서버 오류: ", e);
            return makeResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다.", null);
        }
    }

}
