$(document).ready(function () {
    // 파일 선택 시 경고 메시지 처리
    $('#imageUrl').change(function() {
        var fileInput = $('#imageUrl');
        var warningMessage = $('#imageUrl-warning');

        // 파일이 선택되지 않으면 경고 메시지 표시
        if (fileInput[0].files.length === 0) {
            warningMessage.css('display', 'inline');  // 경고 메시지 표시
        } else {
            warningMessage.css('display', 'none');   // 경고 메시지 숨기기
        }
    });
});

function registerProduct() {
    let category = $('#category').val();
    let name = $('#name').val();
    let price = $('#price').val();
    let size = $('input[name="size"]:checked').val();
    let imageUrl = $('#imageUrl')[0].files[0]; // 파일 선택
    let description = $('#description').val();

    // FormData 객체 생성
    let formData = new FormData();
    formData.append("category", category);
    formData.append("name", name);
    formData.append("price", price);
    formData.append("size", size);
    formData.append("description", description);
    if (imageUrl) {
        formData.append("image", imageUrl); // 파일 추가
    }

    // AJAX 요청
    $.ajax({
        url: '/api/admin/product/add', // 서버 API 경로
        type: 'POST',
        data: formData,
        processData: false, // FormData를 사용할 때 false로 설정
        contentType: false, // FormData를 사용할 때 false로 설정
    }).done(function (data, status, xhr) {
        if (xhr.status === 200) {
            // 상품 등록 성공
            if (confirm('해당 상품을 등록 하시겠습니까?')) {
                alert('상품 등록 성공')
                window.location.href = '/admin/product/list';
            } else {
                alert('수정이 취소되었습니다.');
            }
        }
    }).fail(function (jqXHR, textStatus, errorThrown) {
        // 서버 오류 시
        console.log('textStatus:', textStatus);  // 예: "error"
        console.log('errorThrown:', errorThrown);  // 예: "Internal Server Error"
        console.log('responseText:', jqXHR.responseText);  // 서버가 반환한 메시지 확인 --> json 문자열 반환
        // JSON 문자열을 객체로 변환
        let responseJson = JSON.parse(jqXHR.responseText);
        alert('서버 오류: ' + responseJson.message);
    });
}
