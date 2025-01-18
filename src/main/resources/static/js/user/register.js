$(document).ready(function () {
    $('#check-loginId-button').on('click', function () {
        validateLoginId();
    });
});

function register() {
    let loginId = $("#login-id").val()
    let password = $("#password").val()
    let confirmPassword = $("#confirm-password").val()
    let name = $("#name").val()
    let gender = $("#gender").val()
    let birthdate = $("#birthdate").val()
    let email = $("#email").val()
    let phoneNumber = $("#phone-number").val()
    let postcode = $("#postcode").val(); // 우편번호
    let address = $("#address").val(); // 기본 주소
    let detailAddress = $("#detail-address").val(); // 상세 주소

    // 이메일 인증이 완료되었는지 체크
    if (!validateEmailVerification()) {
        return; // 이메일 인증이 안된 경우 회원가입 진행 안 함
    }

    // 각 유효성 검사 함수 호출
    if (!validateLoginId() ||
        !validatePassword() ||
        !validateName() ||
        !validateBirthdate() ||
        !validateEmailDuplicate() ||
        !validateEmailVerification() ||
        !validatePhoneNumber()) {
        return; // 유효하지 않으면 함수 종료
    }


    $.ajax({
        url: "/api/user/register",
        method: "POST",
        dataType: "json",
        contentType: "application/json; charset=UTF-8",
        data: JSON.stringify({
            loginId: loginId,
            password: password,
            confirmPassword: confirmPassword,
            name: name,
            gender: gender,
            birthdate: birthdate,
            email: email,
            phoneNumber: phoneNumber,
            postcode: postcode,
            address: address,
            detailAddress: detailAddress
        })
    }).done(function (data, status) {
        if (status === "success") {
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
function displayValidationMessage(fieldId, message, status) {
    const span = $('<span>').addClass('validation-message').text(message);
    if (status === 'error') {
        span.css('color', 'red');
    } else {
        span.css('color', 'green');
    }

    // 기존의 메시지가 있다면 제거하고 새로운 메시지로 갱신
    $(fieldId).next('.validation-message').remove();
    $(fieldId).after(span);
}

// 로그인 ID 유효성 검사
function validateLoginId() {
    let loginId = $('#login-id').val();

    // 클라이언트에서 최소 길이 체크
    if (loginId.length < 5 || loginId.length > 20) {
        displayValidationMessage('#login-id', '로그인 ID는 5~20자 사이여야 합니다.', 'error');
        $('#login-id').focus(); // 포커스를 로그인 ID 입력란에 설정
        return false;
    }

    // 서버에서 중복 확인
    let isValid = false;
    $.ajax({
        type: 'POST',
        url: '/api/user/check-loginId',
        contentType: 'application/json',
        data: JSON.stringify({loginId: loginId}),
        async: false
    }).done(function (data) {
        if (data) {
            displayValidationMessage('#login-id', '로그인 ID가 이미 존재합니다.', 'error');
            $('#login-id').focus();
            isValid = false;
        } else {
            displayValidationMessage('#login-id', '사용 가능한 로그인 ID입니다.', 'success');
            isValid = true;
        }
    }).fail(function (jqXHR, textStatus, errorThrown) {
        displayValidationMessage('#login-id', '로그인 ID 확인 중 오류가 발생했습니다. 다시 시도해주세요.', 'error');
        $('#login-id').focus();
        isValid = false;
    });
    return isValid;
}

// 비밀번호 유효성 검사
function validatePassword() {
    const password = $('#password').val();
    const confirmPassword = $('#confirm-password').val();

    // 비밀번호 입력 여부 확인
    if (password === '') {
        displayValidationMessage('#password', '비밀번호를 입력해주세요.', 'error');
        $('#password').focus();
        return false;
    }

    // 비밀번호 길이 확인
    if (password.length < 8 || password.length > 20) {
        displayValidationMessage('#password', '비밀번호는 8~20자 사이여야 합니다.', 'error');
        $('#password').focus();
        return false;
    }

    // 비밀번호와 확인 비밀번호가 일치하는지 확인
    if (password !== confirmPassword) {
        displayValidationMessage('#password', '비밀번호가 일치하지 않습니다.', 'error');
        $('#password').focus();
        return false;
    }

    // 모든 조건이 통과되면 유효한 비밀번호로 판단
    displayValidationMessage('#password', '비밀번호가 유효합니다.', 'success');
    return true;
}

// 이름 유효성 검사
function validateName() {
    const name = $('#name').val();
    if (name.length < 2 || name.length > 100) {
        displayValidationMessage('#name', '이름은 2~100자 사이여야 합니다.', 'error');
        $('#name').focus();
        return false;
    } else {
        displayValidationMessage('#name', '이름이 유효합니다.', 'success');
        return true;
    }
}

// 생년월일 유효성 검사
function validateBirthdate() {
    const birthdate = $('#birthdate').val(); // 날짜 값 가져오기
    const today = new Date(); // 현재 날짜
    const birthDateObj = new Date(birthdate); // 입력된 날짜를 Date 객체로 변환

    if (!birthdate) {
        // 값이 비어 있는 경우
        displayValidationMessage('#birthdate', '생년월일을 입력해주세요.', 'error');
        $('#birthdate').focus();
        return false;
    }

    if (birthDateObj > today) {
        // 미래 날짜인 경우
        displayValidationMessage('#birthdate', '생년월일은 미래 날짜일 수 없습니다.', 'error');
        $('#birthdate').focus();
        return false;
    }

    displayValidationMessage('#birthdate', '생년월일이 유효합니다.', 'success');
    return true;
}

// 이메일 중복 유효성 검사
function validateEmailDuplicate() {
    let email = $('#email').val();

    const emailRegex = /^[a-zA-Z0-9+-\_.]+@[a-zA-Z0-9-]+\.[a-zA-Z0-9-.]+$/;

    // 이메일 형식 검사
    if (!emailRegex.test(email)) {
        displayValidationMessage('#email', '올바른 이메일 형식을 입력해주세요.', 'error');
        $('#email').focus();
        return false;
    }

    // 클라이언트에서 최소 길이 체크
    if (email.length < 10 || email.length > 255) {
        displayValidationMessage('#email', 'email 의 길이는 10~255자 사이여야 합니다.', 'error');
        $('#email').focus();
        return false;
    }

    // 이메일 중복 확인을 위한 비동기 요청
    return new Promise((resolve, reject) => {
        $.ajax({
            type: 'POST',
            url: '/api/user/check-email',
            contentType: 'application/json',
            data: JSON.stringify({mail: email})
        }).done(function (data) {
            if (data) {
                $('#emailCheckMessage').removeClass('success').addClass('error');
                displayValidationMessage('#email', '이메일 중복이 확인되었습니다.', 'error');
                resolve(false); // 중복 이메일이면 false 반환
            } else {
                $('#emailCheckMessage').removeClass('error').addClass('success');
                displayValidationMessage('#email', '사용 가능한 이메일입니다.', 'success');
                resolve(true); // 사용 가능한 이메일이면 true 반환
            }
        }).fail(function (error) {
            $('#emailCheckMessage').removeClass('success').addClass('error');
            displayValidationMessage('#email', '이메일 확인 중 오류가 발생했습니다.', 'error');
            resolve(false); // 오류 발생 시 false 반환
        });
    });
}

// 인증 메일 발송
function sendCodeButton() {
    // 이메일 중복 확인을 했는지 확인
    if (!$('#emailCheckMessage').hasClass('success')) {
        alert('email 중복 확인을 진행하지 않으면 코드가 정상적으로 발송되지 않습니다.');
        $('#email').focus();
        return; // 이메일 중복 확인을 하지 않았으면 코드 발송을 중단
    }

    let email = $('#email').val();

    // 로딩 스피너 표시
    $('#loading-spinner').show();

    $.ajax({
        type: 'POST',
        url: '/api/user/mail',
        contentType: 'application/json',
        data: JSON.stringify({mail: email})
    }).done(function (data) {
        $('#verifyCodeSection').show();
        alert('인증 email 이 발송되었습니다. 인증 번호를 확인해주세요.');
        $('#loading-spinner').hide(); // 로딩 스피너 숨기기
    }).fail(function (error) {
        alert('email 발송에 실패했습니다. 다시 시도해주세요.');
        $('#loading-spinner').hide(); // 로딩 스피너 숨기기
    });
}

// 이메일 인증 상태 체크
function validateEmailVerification() {
    const verificationCode = $('#verificationCode').val();

    // 인증 코드가 입력되지 않았으면 이메일 인증이 완료되지 않았다고 판단
    if (!verificationCode || !$('#verificationMessage').hasClass('success')) {
        alert('이메일 인증을 완료해주세요.');
        $('#verificationCode').focus();
        return false;
    }
    return true;
}

// 인증 코드 확인
function verifyCodeButton() {
    let email = $('#email').val();
    let code = $('#verificationCode').val();

    $.ajax({
        type: 'POST',
        url: '/api/user/verify-code',
        contentType: 'application/json',
        data: JSON.stringify({mail: email, code: code})
    }).done(function (data) {
        if (data === 'Verified') {
            alert('인증이 성공적으로 완료되었습니다.');
            $('#verificationMessage').removeClass('error').addClass('success'); // 성공 클래스를 추가
            displayValidationMessage('#verificationCode', '인증 성공', 'success');
        } else {
            alert('인증 실패. 올바른 코드를 입력하세요.');
            $('#verificationMessage').removeClass('success').addClass('error'); // 실패 클래스를 추가
            displayValidationMessage('#verificationCode', '인증 코드가 올바르지 않습니다.', 'error');
        }
    }).fail(function (error) {
        alert('인증 실패. 다시 시도해주세요.');
        $('#verificationMessage').removeClass('success').addClass('error'); // 실패 클래스를 추가
        displayValidationMessage('#verificationCode', '인증 실패. 다시 시도해주세요.', 'error');
    });
}

function validatePhoneNumber() {
    const phoneNumber = $('#phone-number').val();
    const phoneRegex = /^(010-\d{4}-\d{4})$/; // 010-xxxx-xxxx 형식

    if (!phoneRegex.test(phoneNumber)) {
        displayValidationMessage('#phone-number', '유효한 전화번호 형식을 입력해주세요.', 'error');
        return false;
    }

    // 이메일 중복 확인을 위한 비동기 요청
    return new Promise((resolve, reject) => {
        $.ajax({
            type: 'POST',
            url: '/api/user/check-phoneNumber',
            contentType: 'application/json',
            data: JSON.stringify({phoneNumber:phoneNumber})
        }).done(function (data) {
            if (data) {
                displayValidationMessage('#phone-number', '이미 등록된 전화번호입니다.', 'error');
                resolve(false); // 전화번호 중복이면 false 반환
            } else {
                displayValidationMessage('#phone-number', '사용 가능한 전화번호입니다.', 'success');
                resolve(true); // 사용 가능한 전화번호이면 true 반환
            }
        }).fail(function (error) {
            displayValidationMessage('#phone-number', '전화번호 확인 중 오류가 발생했습니다.', 'error');
            resolve(false); // 오류 발생 시 false 반환
        });
    });
}

// 주소 검색
function execDaumPostcode() {
    new daum.Postcode({
        oncomplete: function(data) {
            var addr = ''; // 주소 변수

            if (data.userSelectedType === 'R') { // 도로명 주소 선택시
                addr = data.roadAddress;
            } else { // 지번 주소 선택시
                addr = data.jibunAddress;
            }

            // 우편번호와 주소 입력
            document.getElementById('postcode').value = data.zonecode;
            document.getElementById("address").value = addr;

            // 상세주소 필드로 포커스 이동
            document.getElementById("detail-address").focus();
        }
    }).open();
}
