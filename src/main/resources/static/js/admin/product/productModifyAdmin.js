$(document).ready(function () {
    const productId = getProductIdFromUrl(); // URL에서 productId를 추출
    loadProduct(productId);
    loadCategories(productId);

    // 파일 선택 시 경고 메시지 처리
    $('#imageUrl').change(function () {
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

// URL에서 productId 추출
function getProductIdFromUrl() {
    const urlParts = window.location.pathname.split('/');
    return urlParts[urlParts.length - 1]; // 마지막 경로가 productId
}


function loadProduct(productId) {
    $.ajax({
        url: `/api/admin/product/${productId}`,
        method: 'GET',
    }).done(function (data) {
        let product = data.responseData;
        // 각 필드에 값 채우기
        $("#name").val(product.name);
        $("#price").val(product.price);
        $("#size").val(product.size);
        $("#description").val(product.description);

        // 삭제 여부에 따라 버튼 텍스트 변경
        const deleteButtonHtml = product.deletedFlag
            ? `<button onclick="cancelDeleteProduct('${productId}');">삭제취소</button>`
            : `<button onclick="deleteProduct('${productId}');">삭제</button>`;
        $('#product-id').html(`
                상품 ID : ${productId}
                ${deleteButtonHtml}`);
    }).fail(function (jqXHR, status, errorThrown) {
        console.error(`Failed to load products: ${errorThrown}`);
        alert('상품을 불러오는 데 실패했습니다.');
    });
}

// 카테고리 목록을 불러오는 함수
function loadCategories(productId) {
    $.ajax({
        url: `/api/admin/product/${productId}/categories`, // 카테고리 목록을 불러오는 API
        method: 'GET',
    }).done(function (data) {
        let categories = data.responseData;

        // 카테고리 목록을 드롭다운에 추가
        categories.forEach(function (category) {
            // 카테고리가 이미 드롭다운에 있는지 확인
            let option = $(`#category option[value='${category.name}']`);
            if (option.length > 0) {
                // 이미 존재하는 카테고리인 경우 selected로 설정
                option.prop('selected', true);
            }
        })
    }).fail(function (jqXHR, status, errorThrown) {
        console.error(`Failed to load categories: ${errorThrown}`);
        alert('카테고리 목록을 불러오는 데 실패했습니다.');
    });
}

function modifyProduct() {
    const productId = getProductIdFromUrl();

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
            if (confirm('해당 상품을 수정 하시겠습니까?')) {
                alert('상품 수정 성공')
                window.location.href = '/admin/product/list';
            } else {
                alert('수정이 취소되었습니다.');
            }
        } else {
            // 오류 메시지 출력
            alert('에러: ' + data.message);
        }
    }).fail(function (jqXHR, textStatus, errorThrown) {
        // 서버 오류 시
        alert('서버 오류: ' + errorThrown);
    });
}

// 삭제 함수 정의
function deleteProduct(productId) {
    if (confirm(`${productId}번 상품을 정말로 삭제하시겠습니까?`)) {
        // AJAX를 통해 서버 API 호출
        $.ajax({
            url: `/api/admin/product/${productId}`, // 서버의 삭제 API 경로
            method: 'DELETE',
        }).done(function () {
            alert('상품이 성공적으로 삭제되었습니다.');
            window.location.href = "/admin/product/list";
        }).fail(function (jqXHR, textStatus, errorThrown) {
            console.error('상품 삭제 실패:', errorThrown);
            alert('상품 삭제에 실패했습니다.');
        });
    }
}

// 삭제 취소 함수
function cancelDeleteProduct(productId) {
    if (confirm(`${productId}번 상품의 삭제를 취소하시겠습니까?`)) {
        $.ajax({
            url: `/api/admin/product/${productId}/restore`, // 삭제취소 API 경로
            method: 'PATCH', // PATCH 메서드 사용
        }).done(function () {
            alert('상품 삭제가 성공적으로 취소되었습니다.');
            window.location.href = "/admin/product/list";
        }).fail(function (jqXHR, textStatus, errorThrown) {
            console.error('삭제 취소 실패:', errorThrown);
            alert('상품 삭제 취소에 실패했습니다.');
        });
    }
}