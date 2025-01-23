$(document).ready(function() {
    // 페이지 로드 시 장바구니 아이템 렌더링
    renderCartItems();

    // 결제 진행 버튼 클릭 시 처리
    $('#checkoutBtn').on('click', function() {
        // 결제 진행 로직 추가
        alert('결제 진행');
    });
});

// 장바구니 아이템 렌더링
function renderCartItems(cartItems) {
    let html = '';

    cartItems.forEach(item => {
        const formattedPrice = formatPrice(item.price);
        html += `
            <div class="cart-item d-flex justify-content-between align-items-center mb-3">
                <div class="cart-item-name">${item.name}</div>
                <div class="cart-item-price">${formattedPrice}</div>
                <div class="cart-item-quantity">
                    <input type="number" value="${item.quantity}" min="1" class="form-control cart-item-quantity-input">
                </div>
                <div class="cart-item-remove">
                    <button class="btn btn-danger btn-sm" onclick="removeFromCart(${item.id})">삭제</button>
                </div>
            </div>
        `;
    });

    $('#cart-items').html(html);
}

// 장바구니에서 아이템 삭제
function removeFromCart(productId) {
    // 해당 상품을 장바구니에서 삭제하는 로직 추가
    console.log(`상품 ${productId} 삭제`);
}