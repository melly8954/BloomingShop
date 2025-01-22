function registerProduct(){
    let productId = $("#product-id").val();
    let category = $('#category').val();
    let name = $('#name').val();
    let price = $('#price').val();
    let size = $('#size').val();
    let imageUrl = $('#imageUrl')[0].files[0]; // 파일 선택
    let description = $('#description').val();

    // FormData 객체 생성
    let formData = new FormData();
    formData.append("productId", productId);
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
        url: '/api/admin/product/modify', // 서버 API 경로
        type: 'PATCH',
        data: formData,
        processData: false, // FormData를 사용할 때 false로 설정
        contentType: false, // FormData를 사용할 때 false로 설정
    }).done(function (data, status, xhr) {
        if (xhr.status === 200) {
            // 상품 등록 성공
            alert('상품 수정 성공');
            window.location.href = '/admin/product/list'; // 상품 목록 페이지로 이동
        } else {
            // 오류 메시지 출력
            alert('에러: ' + data.message);
        }
    }).fail(function (jqXHR, textStatus, errorThrown) {
        // 서버 오류 시
        alert('서버 오류: ' + errorThrown);
    });
}
