$(document).ready(function () {
    $('#check-loginId-button').on('click', function () {
        validateLoginId();
    });
});

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

    // 각 유효성 검사 함수 호출
    if( !validateLoginId()){
        return; // 유효하지 않으면 함수 종료
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
}

// 유효성 검사 메시지 표시 함수
function displayValidationMessage(inputId, message, status) {
    const span = $('<span>').addClass('validation-message').text(message);
    if (status === 'error') {
        span.css('color', 'red');
    } else {
        span.css('color', 'green');
    }

    // 기존의 메시지가 있다면 제거하고 새로운 메시지로 갱신
    $(inputId).next('.validation-message').remove();
    $(inputId).after(span);
}

// 로그인 ID 유효성 검사
function validateLoginId() {
    let loginId = $('#login-id').val();

    // 클라이언트에서 최소 길이 체크
    if (loginId.length < 5 || loginId.length > 20) {
        displayValidationMessage('#login-id', '로그인 ID는 5~20자 사이여야 합니다.', 'error');
        return false;
    }

    // 서버에서 중복 확인
    let isValid = false;
    $.ajax({
        type: 'POST',
        url: '/api/user/check-loginId',
        contentType: 'application/json',
        data: JSON.stringify({ loginId: loginId }),
        async: false, // 동기 처리
        success: function (response) {
            if (response) {
                displayValidationMessage('#login-id', '로그인 ID가 이미 존재합니다.', 'error');
                isValid = false;
            } else {
                displayValidationMessage('#login-id', '사용 가능한 로그인 ID입니다.', 'success');
                isValid = true;
            }
        },
        error: function () {
            displayValidationMessage('#login-id', '로그인 ID 확인 중 오류가 발생했습니다. 다시 시도해주세요.', 'error');
            isValid = false;
        }
    });

    return isValid;
}
