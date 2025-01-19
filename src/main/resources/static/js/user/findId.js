$.findId = function (){
    let email = $("#email").val();
    $.ajax({
        url: `/api/user/login-id/${email}`, // 이메일을 URL에 추가
        method: "GET",
    }).done(function (data) {
        // 요청 성공 시 실행
        if (data.message === "아이디 찾기 성공") {
            let html = `<div> 해당 계정의 로그인 ID : ${data.responseData}</div>`;
            $("#find_email").html(html); // HTML 추가
        } else {
            $("#find_email").html(`<p>오류: ${data.message}</p>`);
        }
    }).fail(function (jqXHR, textStatus, errorThrown) {
        // 요청 실패 시 실행
        console.error("Request failed: " + textStatus + ", " + errorThrown);
        $("#find_email").html("<p>아이디 찾기 중 오류가 발생했습니다.</p>");
    });
}