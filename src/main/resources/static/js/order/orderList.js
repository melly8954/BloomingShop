$(document).ready(function() {
    orderList();
});

function orderList() {
    // 비로그인 유저 -> LocalStorage에서 guestId를 가져옴
    const guestId = localStorage.getItem('guestId');

    // guestId가 없다면 새로 생성하여 LocalStorage에 저장
    if (!guestId) {
        const newGuestId = "guest-" + java.util.UUID.randomUUID().toString();
        localStorage.setItem('guestId', newGuestId);
    }

    $.ajax({
        url: '/api/order/list',
        type: 'GET',
        dataType: 'json',
        headers: {
            'Guest-Id': guestId || newGuestId  // 헤더에 guestId를 포함시켜 전송
        }
    }).done(function (data) {
        const orderListContainer = $('#order-list');
        orderListContainer.empty(); // 기존에 있던 주문 목록 초기화

        // 응답 받은 주문 목록을 순회하여 화면에 표시
        data.responseData.forEach(function (order) {
            const orderItem = `
                <div class="col-md-4 mb-4">
                    <div class="card">
                        <img src="${order.productImageUrl}" class="card-img-top" alt="${order.productName}">
                        <div class="card-body">
                            <h5 class="card-title">${order.productName}</h5>
                            <p class="card-text">
                                가격: ${order.productPrice} <br>
                                사이즈: ${order.productSize} <br>
                                수량: ${order.quantity} <br>
                                총액: ${order.totalPrice}원 <br>
                                결제 상태: ${order.paymentStatus} <br>
                                배송 상태: ${order.deliveryStatus}
                            </p>
                        </div>
                    </div>
                </div>
            `;
            orderListContainer.append(orderItem);
        });
    }).fail(function (xhr, status, error) {
        console.error("주문 목록 로드 실패:", error);
    });
}