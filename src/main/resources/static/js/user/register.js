function register(){
    let loginId =  $("#login-id").val()
    let password = $("#password").val()
    let confirmPassword = $("#confirm-password").val()
    let name = $("#name").val()
    let gender = $("#gender").val()
    let birthdate = $("#birthdate").val()
    let email =  $("#email").val()
    let phoneNumber = $("#phone-number").val()
    let address = $("#address").val()

    if (password !== confirmPassword) {
        alert("비밀번호가 일치하지 않습니다.");
        return;
    }

    $.ajax({
        url: "/api/user/register",
        method: "POST",
        dataType: "json",
        contentType: "application/json; charset=UTF-8",
        data: JSON.stringify({
            loginId : loginId,
            password : password,
            confirmPassword : confirmPassword,
            name : name,
            gender : gender,
            birthdate : birthdate,
            email : email,
            phoneNumber : phoneNumber,
            address : address
        })
    }).done(function (data,status) {
        if (status === "success"){
            console.log(data.responseData);
            alert("회원가입이 성공적으로 완료되었습니다!");
            window.location.href = "/";
        } else {
            alert("회원가입 실패: " + data.message);
        }
    }).fail(function (jqXHR, textStatus, errorThrown) {
        // 응답에서 오류 메시지 추출하여 출력
        let errorMessage = "회원가입 실패: 입력란을 모두 작성하시길 바랍니다.";
        if (jqXHR.responseJSON && jqXHR.responseJSON.message) {
            errorMessage = jqXHR.responseJSON.message; // 서버에서 보낸 메시지를 사용
        }
        console.error("Request failed: " + textStatus + ", " + errorThrown);
        alert(errorMessage); // 에러 메시지 출력
    });
};