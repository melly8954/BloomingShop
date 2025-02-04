let userId = null; // 로그인 유저 ID

$(document).ready(function () {
    // 로그인 여부 확인
    checkLoginStatus();
});

// 로그인 유저의 정보 가져오기
function checkLoginStatus() {
    $.ajax({
        url: '/api/user/getUserId',
        method: 'GET',
    }).done(function (data) {
        // 로그인 상태
        userId = data.responseData.id;
        fetchCartItems(userId, true); // 로그인 유저의 장바구니 가져오기
    }).fail(function () {
        // 비로그인 상태
        console.log('로그인하지 않은 상태입니다.');
        fetchCartItems(guestId, false); // 비회원의 장바구니 가져오기
    });
}

// 계정 탈퇴 (비활성화)
function deleteUser() {
    if (confirm("정말 계정을 탈퇴하시겠습니까?")) {
        $.ajax({
            url: `/api/user/${userId}/delete`,  // 사용자 탈퇴 요청 URL
            type: 'PATCH',
        }).done(function (data, status, xhr){
            if (status === "success") {
                // 성공 후 세션 무효화 및 로그아웃 처리
                $.ajax({
                    url: "/logout",  // Spring Security 로그아웃 URL
                    type: "POST",  // 로그아웃 요청
                    success: function() {
                        alert("계정이 탈퇴되었습니다.");
                        location.href = "/";  // 홈 화면으로 리다이렉트
                    },
                    error: function(jqXHR, textStatus, errorThrown) {
                        console.error("Logout failed: " + textStatus + ", " + errorThrown);
                    }
                });
            }
        }).fail(function (jqXHR, textStatus, errorThrown) {
            console.error("Request failed: " + textStatus + ", " + errorThrown);
        });
    } else {
        console.log('계정 탈퇴가 취소되었습니다.');
    }
}