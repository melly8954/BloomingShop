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

// 장바구니 아이템을 화면에 표시하는 함수
function displayCartItems(cartData) {
    let cartItemsContainer = $('#cart-items');
    cartItemsContainer.empty(); // 기존 아이템 초기화

    // 장바구니 데이터가 있으면 아이템을 추가
    if (cartData && cartData.length > 0) {
        cartData.forEach(item => {
            let cartItemHtml = `
                <div class="cart-item">
                    <img src="${item.productImageUrl}" alt="${item.productName}" class="cart-item-img">
                    <div class="cart-item-info">
                        <p>상품명: ${item.productName}</p>
                        <p>가격: ${formatPrice(item.productPrice)}</p>
                        <p>수량: ${item.quantity}</p>
                    </div>
                </div>
            `;
            cartItemsContainer.append(cartItemHtml);
        });
    } else {
        cartItemsContainer.append('<p>장바구니에 아이템이 없습니다.</p>');
    }
}

// 가격 포맷팅 함수
function formatPrice(price) {
    if (price == null || isNaN(price)) {
        return '₩0';  // 유효하지 않은 가격일 경우 기본값 '₩0' 반환
    }
    return '₩' + price.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}