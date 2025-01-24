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
});

// 상품 로드 함수
function loadProduct(page, name, category, sortBy, sortOrder) {
    if (category === '0') {
        category = ''; // 카테고리 필터를 비워서 모든 카테고리를 포함
    }

    $.ajax({
        url: `/api/product?page=${page}&name=${name}&category=${category}&sort=${sortBy}&order=${sortOrder}&size=${pageSize}`,
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
    // deleted_flag가 false인 상품만 필터링
    const filteredProducts = data.responseData.products.filter(product => !product.deletedFlag);

    filteredProducts.forEach(function (product) {
        // 가격을 한화 포맷팅
        const formattedPrice = formatPrice(product.price);
        html += `
            <div class="col-md-4 mb-4">
                <div class="card" style="cursor: pointer;" onclick="loadProductDetail(${product.id});">
                    <img src="${product.imageUrl}" alt="${product.name}" class="card-img-top">
                    <div class="card-body">
                        <h5 class="card-title">${product.name}</h5>
                        <p class="card-text">가격 : ${formattedPrice}<br>
                                            사이즈 : ${product.size}</p>
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

function formatPrice(price) {
    if (price == null || isNaN(price)) {
        return '₩0';  // 유효하지 않은 가격일 경우 기본값 '₩0' 반환
    }
    return '₩' + price.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

// 상품 상세 정보 로딩 함수
function loadProductDetail(productId) {
    $.ajax({
        url: `/api/product/${productId}/detail`,
        method: 'GET',
    }).done(function(data) {
        let product = data.responseData;
        // 모달에 상품 정보 채우기
        $('#product-image').attr('src', product.imageUrl);
        $('#product-name').text(product.name);
        $('#product-description').text(product.description);
        $('#product-price').text(formatPrice(product.price));
        $('#product-size').text(product.size);

        let quantity = $("#quantityInput").val();
        // 수량 값 변경 시 처리
        $('#quantityInput').on('change', function() {
            quantity = $(this).val(); // 변경된 수량 값 가져오기
            console.log("Changed Quantity: ", quantity); // 변경된 수량 값 출력
        });
        // 장바구니 담기 버튼 이벤트 중복 등록 문제 방지
        $('#addToCartBtn').off('click').on('click', function() {
            addToCart(productId, quantity); // 장바구니에 추가
        });

        $('#orderNowBtn').on('click', function() {
            orderNow(productId,quantity); // 바로 주문 페이지로 이동
        });

        // 모달 열기
        $('#productDetailModal').modal('show');
    }).fail(function(jqXHR, status, errorThrown) {
        console.error(`상품 상세 정보 로드 실패: ${errorThrown}`);
        alert('상품 정보를 불러오는 데 실패했습니다.');
    });
}

// 장바구니 담기
function addToCart(productId, quantity) {
    // 사용자에게 확인 요청
    if (!confirm('해당 상품을 장바구니에 추가 하시겠습니까?')) {
        return; // 사용자가 취소를 클릭하면 함수 종료
    }
    $.ajax({
        url: '/api/user/getUserId',
        method: 'GET',
    }).done(function (data) {
        let userId = data.responseData.id; // 서버에서 받은 userId 저장

        // userId가 없는 경우 처리
        if (!userId) {
            alert('로그인 상태가 아닙니다.');
            return;
        }

        // 장바구니 추가 요청
        $.ajax({
            url: '/api/cart/add',
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({
                userId: userId,
                productId: productId,
                quantity: quantity
            }),
        }).done(function () {
            $('#productDetailModal').modal('hide')
            alert('장바구니에 상품이 추가되었습니다.');
        }).fail(function () {
            alert('장바구니 추가에 실패했습니다.');
        });
    }).fail(function () {
        console.error('사용자 ID를 가져오는 데 실패했습니다.');
        alert('로그인이 필요합니다.');
        location.href="/user/login";
    });
}