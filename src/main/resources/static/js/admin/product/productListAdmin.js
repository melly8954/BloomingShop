const pageSize = 6;

$(document).ready(function () {
    const searchInput = $('#search');
    const categorySelect = $('#category');
    const sortSelect = $('#sort');  // 정렬 기준 select
    const ascBtn = $('#ascBtn');    // 오름차순 버튼
    const descBtn = $('#descBtn');  // 내림차순 버튼

    let currentPage = 1;
    let sortBy = sortSelect.val();  // 초기 정렬 기준
    let sortOrder = 'asc';  // 초기 정렬 순서

    // 페이지 로딩 시 첫 번째 호출
    loadProduct(currentPage, searchInput.val(), categorySelect.val(), sortBy, sortOrder);

    // 카테고리나 검색어가 변경될 때마다 호출
    $('#category').on('change', function () {
        loadProduct(1, searchInput.val(), categorySelect.val(), sortBy, sortOrder); // 카테고리 변경 시 첫 번째 페이지로 로드
    });

    // 검색 버튼 클릭 시 호출
    $('#searchBtn').on('click', function() {
        loadProduct(1, searchInput.val(), categorySelect.val(), sortBy, sortOrder); // 검색어와 카테고리 값으로 첫 번째 페이지 로드
    });

    // 리셋 버튼 클릭 시 호출
    $('#resetBtn').on('click', function() {
        searchInput.val('');  // 검색어 초기화
        categorySelect.val('0');  // 카테고리 초기화 (전체 선택)
        loadProduct(1, '', '0', sortBy, sortOrder);  // 전체 상품 목록을 첫 번째 페이지로 로드
    });

    // 정렬 기준 선택 시 호출
    sortSelect.on('change', function() {
        sortBy = sortSelect.val();  // 선택된 정렬 기준
        loadProduct(1, searchInput.val(), categorySelect.val(), sortBy, sortOrder);  // 첫 번째 페이지로 로드
    });

    // 오름차순 버튼 클릭 시 호출
    ascBtn.on('click', function () {
        sortOrder = 'asc'; // 오름차순
        toggleActiveButton(ascBtn, descBtn); // 버튼 활성화 효과
        loadProduct(1, searchInput.val(), categorySelect.val(), sortBy, sortOrder); // 첫 번째 페이지로 로드
    });

    // 내림차순 버튼 클릭 시 호출
    descBtn.on('click', function () {
        sortOrder = 'desc'; // 내림차순
        toggleActiveButton(descBtn, ascBtn); // 버튼 활성화 효과
        loadProduct(1, searchInput.val(), categorySelect.val(), sortBy, sortOrder); // 첫 번째 페이지로 로드
    });

    $('#navigateToProductModify').on('click', function () {
        confirm("")
    })
});

// 상품 로드 함수
function loadProduct(page, name, category, sortBy, sortOrder) {
    if (category === '0') {
        category = ''; // 카테고리 필터를 비워서 모든 카테고리를 포함
    }

    $.ajax({
        url: `/api/admin/product?page=${page}&name=${name}&category=${category}&sort=${sortBy}&order=${sortOrder}&size=${pageSize}`,
        method: 'GET',
    }).done(function (data) {
        renderProductList(data); // 상품 렌더링
        updateProductCount(data.responseData.totalElements); // 상품 총 갯수 업데이트
        makePageUI(data.responseData.totalElements, page, "#pagination", sortOrder); // 페이지네이션 UI 생성
    }).fail(function (jqXHR, status, errorThrown) {
        console.error(`Failed to load products: ${errorThrown}`);
        alert('상품을 불러오는 데 실패했습니다.');
    });
}

// 상품 리스트 렌더링
function renderProductList(data) {
    let html = "";
    data.responseData.products.forEach(function (product) {
        // 가격을 한화 포맷팅
        const formattedPrice = formatPrice(product.price);
        // 삭제 여부 확인
        const isDeleted = product.deletedFlag;
        const productContent = isDeleted
            ? `<p class="text-danger">해당 상품은 삭제된 상태입니다.</p>`
            : `<p class="card-text">가격 : ${formattedPrice}<br>
                                     사이즈 : ${product.size}<br>
                                     상품 ID : ${product.id}</p>`;
        html += `
            <div class="col-md-4 mb-4">
                <div class="card navigateToProductModify" data-id="${product.id}" 
                     style="${isDeleted ? 'opacity: 0.5;' : ''}">
                    <img src="${product.imageUrl}" alt="${product.name}" class="card-img-top">
                    <div class="card-body">
                        <h5 class="card-title">${product.name}</h5>
                        ${productContent}
                    </div>
                </div>
            </div>`;
    });
    $("#product-grid").html(html);
}

function makePageUI(totalElements, currentPage, pageDivId, sortOrder) {
    const rowsPerPage = 6; // 페이지 당 항목 수
    const totalPages = Math.ceil(totalElements / rowsPerPage); // 전체 페이지 수
    const startPage = getStartPage(currentPage);
    const endPage = getEndPage(startPage, totalPages);

    // 페이지네이션 HTML 초기화
    let paginationHTML = `<nav aria-label="Page navigation example"><ul class="pagination justify-content-center">`;

    // Previous 버튼
    let prevPage = currentPage > 1 ? currentPage - 1 : 1;
    paginationHTML += `<li class="page-item">
        <a class="page-link btn btn-outline-primary" onclick="loadProduct(${prevPage}, '${$('#search').val()}', '${$('#category').val()}', '${$('#sort').val()}', '${sortOrder}')">Prev</a>
    </li>`;

    // 페이지 버튼들
    for (let i = startPage; i <= endPage; i++) {
        let sClass = i === currentPage ? 'page-item active' : 'page-item';
        paginationHTML += `<li class="${sClass}">
            <a class="page-link btn btn-outline-primary" onclick="loadProduct(${i}, '${$('#search').val()}', '${$('#category').val()}', '${$('#sort').val()}', '${sortOrder}')">${i}</a>
        </li>`;
    }

    // Next 버튼
    let nextPage = currentPage < totalPages ? currentPage + 1 : totalPages;
    paginationHTML += `<li class="page-item">
        <a class="page-link btn btn-outline-primary" onclick="loadProduct(${nextPage}, '${$('#search').val()}', '${$('#category').val()}', '${$('#sort').val()}', '${sortOrder}')">Next</a>
    </li>`;

    paginationHTML += `</ul></nav>`;

    $(pageDivId).html(paginationHTML);
}

// 시작 페이지 계산
function getStartPage(page) {
    return Math.floor((page - 1) / 5) * 5 + 1;
}

// 끝 페이지 계산
function getEndPage(startPage, totalPages) {
    return Math.min(startPage + 4, totalPages);
}

// 상품 총 갯수 업데이트
function updateProductCount(totalElements) {
    $('#product-count').text(`상품 갯수: ${totalElements}`);
}

// 활성화 버튼 토글 함수
function toggleActiveButton(activeBtn, inactiveBtn) {
    activeBtn.addClass('btn-primary').removeClass('btn-outline-secondary'); // 활성화된 버튼 스타일
    inactiveBtn.addClass('btn-outline-secondary').removeClass('btn-primary'); // 비활성화된 버튼 스타일
}


// 이미지 카드 누르는 경우
$(document).on('click', '.navigateToProductModify', function () {
    const productId = $(this).data('id'); // 클릭된 카드의 data-id 값 가져오기
    if (confirm(`상품 ID : ${productId}번 수정할까요?`)) {
        navigateToProductModify(productId);
    }
});

function navigateToProductModify(productId) {
    // productId를 기반으로 원하는 URL로 이동
    location.href = `/admin/product/modify/${productId}`;
}

// 가격 포맷팅 함수
function formatPrice(price) {
    return '₩' + price.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}