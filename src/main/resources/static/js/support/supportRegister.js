$(document).ready(function () {
    // 체크박스 상태에 따라 비밀번호 입력란을 토글
    $('#secret').on('change', function () {
        if ($(this).prop('checked')) {
            $('#passwordSection').show();  // 체크되면 비밀번호 입력란 보이기
        } else {
            $('#passwordSection').hide();  // 체크 해제되면 비밀번호 입력란 숨기기
        }
    });

    // 페이지 로드 시, 체크박스 상태에 따라 비밀번호 입력란 상태 초기화
    if ($('#secret').prop('checked')) {
        $('#passwordSection').show();  // 비밀글로 체크되어 있으면 비밀번호 입력란 보이기
    } else {
        $('#passwordSection').hide();  // 비밀글이 아니면 비밀번호 입력란 숨기기
    }
});

function boardRegist() {
    // 폼 데이터를 FormData로 수집
    const formData = new FormData($('#supportRegisterForm')[0]);

    // 비밀글 여부 체크
    const isSecret = $('#secret').prop('checked');
    formData.append('isSecret', isSecret);

    // 비밀글 체크 시 비밀번호 추가
    if (isSecret && $('#password').val()) {
        formData.append('password', $('#password').val());
    }

    // 여러 첨부파일 추가
    const attachments = $('#attachment')[0].files;
    if (attachments.length > 0) {
        for (let i = 0; i < attachments.length; i++) {
            formData.append('attachments[]', attachments[i]); // 배열 형식으로 첨부파일들 추가
        }
    }

    // JSON 데이터 추가
    const boardData = {
        title: $('#title').val(),
        content: $('#content').val(),
        authorName: $('#authorName').val()
    };
    formData.append('boardData', new Blob([JSON.stringify(boardData)], { type: 'application/json' }));

    // AJAX 요청
    $.ajax({
        url: '/api/support/register', // 실제 API URL로 변경
        type: 'POST',
        data: formData,
        processData: false,  // FormData를 사용하면 자동으로 처리되지 않으므로 false
        contentType: false,  // 자동으로 Content-Type을 설정하지 않도록 false
    }).done(function (data) {
        if (data.status === 200) {
            // 성공적인 응답 처리
            alert('게시글이 등록되었습니다!');
            window.location.href = '/support/list'; // 게시판 목록 페이지로 리디렉션
        }
    }).fail(function (jqXHR, textStatus, errorThrown) {
        // 실패한 경우
        alert('게시글 등록에 실패했습니다. 다시 시도해주세요.');
    });
}
