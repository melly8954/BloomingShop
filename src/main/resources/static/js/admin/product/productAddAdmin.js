function registerProduct() {
    let category = $('#category').val();
    let name = $('#name').val();
    let price = $('#price').val();
    let size = $('#size').val();
    let imageUrl = $('#imageUrl')[0].files[0];
    let description = $('#description').val();

    // AJAX 요청
    $.ajax({
        url: '/api/admin/product/add',  // 서버 API 경로
        type: 'POST',
        data: JSON.stringify({
            category: category,
            name: name,
            price: price,
            size: size,
            imageUrl: imageUrl,
            description: description,
        }),
        dataType: "json",
        contentType: "application/json; charset=UTF-8"
    }).done(function (data, status, xhr) {
        if (status === 'OK') {
            // 상품 등록 성공
            alert('상품 등록 성공');
            window.location.href = '/admin/product/list'; // 상품 목록 페이지로 이동
        } else {
            // 오류 메시지 출력
            alert('에러: ' + response.message);
        }
    }).fail(function (jqXHR, textStatus, errorThrown) {
        // 서버 오류 시
        alert('서버 오류: ' + error);
    });
}
