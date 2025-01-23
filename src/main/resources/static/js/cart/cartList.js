let userId = null; // 전역 변수로 userId 선언

$(document).ready(function () {
    // 페이지 로드 시 사용자 ID를 얻어서 장바구니 정보를 가져옴
    $.ajax({
        url: '/api/user/getUserId',
        method: 'GET',
    }).done(function (data) {
        userId = data.responseData.id; // 서버에서 받은 userId 저장

        // userId가 없으면 로그인 상태가 아님
        if (!userId) {
            alert('로그인이 필요합니다.');
            return;
        }
        // userId를 사용하여 장바구니 데이터 가져오기
        $.ajax({
            url: `/api/cart/${userId}/list`,
            method: 'GET',
        }).done(function (cartData) {
            displayCartItems(cartData.responseData); // 장바구니 아이템 표시 함수 호출
        }).fail(function () {
            alert('장바구니 정보를 가져오는 데 실패했습니다.');
        });
    }).fail(function () {
        console.error('사용자 ID를 가져오는 데 실패했습니다.');
        alert('로그인이 필요합니다.');
    });
});

// 가격 포맷팅 함수
function formatPrice(price) {
    if (price == null || isNaN(price)) {
        return '₩0';  // 유효하지 않은 가격일 경우 기본값 '₩0' 반환
    }
    return '₩' + price.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

// 장바구니 아이템을 화면에 표시하는 함수
function displayCartItems(cartData) {
    let cartItemsContainer = $('#cart-items');
    cartItemsContainer.empty(); // 기존 아이템 초기화

    // 장바구니 데이터가 있으면 아이템을 추가
    if (cartData && cartData.length > 0) {
        cartData.forEach(item => {
            let cartItemHtml = `
                <div class="col-sm-6 col-md-4 col-lg-6 mb-4">
                    <div class="card shadow-sm h-100" style="max-width: 350px; margin: 0 auto;">
                        <img src="${item.productImageUrl}" alt="${item.productName}" class="card-img-top" style="height: auto; object-fit: cover;">
                        <div class="card-body d-flex flex-column">
                            <h5 class="card-title">${item.productName} <button class="btn btn-danger btn-sm" id="remove-${item.productId}">장바구니 제거</button> </h5>
                            <p class="card-text">가격: ${formatPrice(item.productPrice)}</p>

                            <div class="d-flex justify-content-between align-items-center mt-auto">
                                <div class="d-flex align-items-center">
                                <label for="quantity-${item.productId}" style="margin-right: 10px">구매 수량</label>
                                <input type="number" class="form-control" id="quantity-${item.productId}" value="${item.quantity}" min="1" onchange="updateQuantity(${item.productId});" style="width: auto; max-width: 80px; border: 2px solid #e5daff;""/>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            `;
            cartItemsContainer.append(cartItemHtml); // 장바구니 아이템을 추가

            // 삭제 버튼 이벤트 리스너 추가
            $(`#remove-${item.productId}`).click(function () {
                removeItemFromCart(item.productId);
            });
        });
    } else {
        cartItemsContainer.append('<p>장바구니에 아이템이 없습니다.</p>'); // 장바구니가 비었을 경우
    }
}

// 수량이 변경될 때마다 UI에서만 반영되도록 처리
function updateQuantity(productId) {
    // 입력된 수량을 가져옴
    let newQuantity = $(`#quantity-${productId}`).val();

    // 수량이 1보다 작은 값으로 변경되지 않도록 처리
    if (newQuantity < 1) {
        newQuantity = 1;
        $(`#quantity-${productId}`).val(1); // UI에서 1로 강제로 설정
    }

    // 서버에 수량을 전송하지 않고 UI만 업데이트
    // updateQuantity(productId, newQuantity)처럼 서버에 바로 요청을 보내지 않고 UI 업데이트만 처리
}

function checkout() {
    let cartItems = [];

    // 장바구니에 있는 모든 아이템을 배열로 수집
    $('#cart-items .card').each(function () {
        let productId = $(this).find('.btn-outline-secondary').data('productId');
        let quantity = $(this).find(`#quantity-${productId}`).val();  // 변경된 수량 값을 가져옴
        cartItems.push({ productId: productId, quantity: parseInt(quantity) });
    });

    // 서버에 장바구니 정보 전송 (구매)
    $.ajax({
        url: '/api/cart/checkout',
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({ userId: userId, cartItems: cartItems }),
    }).done(function (response) {
        if (response.success) {
            alert('구매가 완료되었습니다!');
            // 구매 후 장바구니 비우기
            $('#cart-items').empty();
        } else {
            alert('구매에 실패했습니다.');
        }
    }).fail(function () {
        alert('서버와의 연결이 실패했습니다.');
    });
}