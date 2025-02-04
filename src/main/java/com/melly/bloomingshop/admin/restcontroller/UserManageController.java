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
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class UserManageController implements ResponseController {
    private final UserManageService userManageService;

    // 모든 사용자 조회 API
    @GetMapping("/user/all-list")
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

    // 이름 검색으로 회원 찾기 API
    @GetMapping("{name}")
    public ResponseEntity<ResponseDto> getUserByName(@PathVariable String name, @RequestParam int page, @RequestParam(defaultValue = "5") int size) {
        try{
            if(name == null || name.isEmpty()) {
                return makeResponseEntity(HttpStatus.BAD_REQUEST,"이름을 정확히 입력하세요",null);
            }
            // 페이지네이션을 적용하기 위해 Pageable 사용
            Pageable pageable = PageRequest.of(page - 1, size); // 페이지는 0부터 시작하므로, page - 1로 처리
            Page<User> user = this.userManageService.getUserByName(name,pageable);
            if(user == null || user.isEmpty()) {
                return makeResponseEntity(HttpStatus.NOT_FOUND,"해당 유저가 조회되지 않습니다.",null);
            }
            return makeResponseEntity(HttpStatus.OK,"해당 유저의 정보를 조회합니다.",
                    new UserPageResponseDto<>(user.getContent(), user.getTotalElements(), user.getTotalPages(), user.getNumber()+1, user.getSize()));
        }catch (Exception e) {
            log.error(e.getMessage(), e);
            return makeResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR,"서버 오류 : " + e.getMessage(),null);
        }
    }

    // 관리자 페이지에서 사용자가 비활성화된 계정을 확인하고 비활성화 처리
    @PatchMapping("/{id}/disable")
    public ResponseEntity<ResponseDto> disableUser(@PathVariable Long id) {
        try {
            if(id == null || id <= 0) {
                return makeResponseEntity(HttpStatus.BAD_REQUEST,"회원 ID > 0 을 만족해야 한다.",null);
            }
            this.userManageService.softDisabledUser(id);
            return makeResponseEntity(HttpStatus.OK,"해당 유저의 계정이 비활성화되었습니다.",true);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return makeResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR,"서버 오류: " + e.getMessage(),null);
        }
    }

    // 관리자 페이지에서 사용자가 탈퇴된 계정을 복구
    @PatchMapping("/{id}/undo")
    public ResponseEntity<ResponseDto> undoUser(@PathVariable Long id) {
        try {
            if(id == null || id <= 0) {
                return makeResponseEntity(HttpStatus.BAD_REQUEST,"회원 ID > 0 을 만족해야 한다.",null);
            }
            this.userManageService.undoUser(id);
            return makeResponseEntity(HttpStatus.OK,"해당 유저의 계정 삭제가 취소되었습니다.",true);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return makeResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR,"서버 오류: " + e.getMessage(),null);
        }
    }
}
