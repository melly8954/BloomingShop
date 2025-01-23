$(document).ready(function() {
    // 페이지 로드 시 사용자 ID를 얻어서 장바구니 정보를 가져옴
    $.ajax({
        url: '/api/user/getUserId',
        method: 'GET',
    }).done(function(data) {
        let userId = data.responseData.id; // 서버에서 받은 userId 저장

        // userId가 없으면 로그인 상태가 아님
        if (!userId) {
            alert('로그인이 필요합니다.');
            return;
        }
        // userId를 사용하여 장바구니 데이터 가져오기
        $.ajax({
            url: `/api/cart/${userId}/list`,
            method: 'GET',
        }).done(function(cartData) {
            displayCartItems(cartData.responseData); // 장바구니 아이템 표시 함수 호출
        }).fail(function() {
            alert('장바구니 정보를 가져오는 데 실패했습니다.');
        });
    }).fail(function() {
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
                            <h5 class="card-title">${item.productName}</h5>
                            <p class="card-text">가격: ${formatPrice(item.productPrice)}</p>

                            <div class="d-flex justify-content-between align-items-center mt-auto">
                                <div class="d-flex align-items-center">
                                    <!-- 수량 조절 버튼 -->
                                    <button class="btn btn-outline-secondary btn-sm" id="decrease-${item.productId}">-</button>
                                    <span class="px-3" id="quantity-${item.productId}">${item.quantity}</span>
                                    <button class="btn btn-outline-secondary btn-sm" id="increase-${item.productId}">+</button>
                                </div>

                                <!-- 삭제 버튼 -->
                                <button class="btn btn-danger btn-sm" id="remove-${item.productId}">삭제</button>
                            </div>
                        </div>
                    </div>
                </div>
            `;
            cartItemsContainer.append(cartItemHtml); // 장바구니 아이템을 추가

            // 수량 조절 버튼 이벤트 리스너 추가
            $(`#decrease-${item.productId}`).click(function() {
                updateQuantity(item.productId, item.quantity - 1);
            });

            $(`#increase-${item.productId}`).click(function() {
                updateQuantity(item.productId, item.quantity + 1);
            });

            // 삭제 버튼 이벤트 리스너 추가
            $(`#remove-${item.productId}`).click(function() {
                removeItemFromCart(item.productId);
            });
        });
    } else {
        cartItemsContainer.append('<p>장바구니에 아이템이 없습니다.</p>'); // 장바구니가 비었을 경우
    }
}

// 수량 업데이트 함수
function updateQuantity(productId, newQuantity) {
    if (newQuantity <= 0) return; // 수량이 0 이하로 내려가지 않도록 방지

    // 수량 변경 후 서버로 업데이트 요청 (예시)
    $.ajax({
        url: `/api/cart/updateQuantity`,
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({ productId: productId, quantity: newQuantity }),
        success: function() {
            $(`#quantity-${productId}`).text(newQuantity); // UI 업데이트
        },
        error: function() {
            alert('수량 변경에 실패했습니다.');
        }
    });
}
