$(document).ready(function() {
    orderList();
});

// 가격 포맷팅 함수
function formatPrice(price) {
    if (price == null || isNaN(price)) {
        return '₩0';  // 유효하지 않은 가격일 경우 기본값 '₩0' 반환
    }
    return '₩' + price.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

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
            'Guest-Id': guestId || newGuestId
        }
    }).done(function (data) {
        const orderListContainer = $('#order-list');
        orderListContainer.empty(); // 기존에 있던 주문 목록 초기화
        console.log(data);

        // 주문을 orderId별로 그룹화
        const groupedOrders = data.responseData.reduce((groups, order) => {
            if (!groups[order.orderId]) {
                groups[order.orderId] = [];
            }
            groups[order.orderId].push(order);
            return groups;
        }, {});

        // 그룹화된 주문을 순회하여 표시
        Object.keys(groupedOrders).forEach(function (orderId) {
            const orders = groupedOrders[orderId];
            const orderGroup = $('<div class="order-group mb-5"></div>'); // 각 주문별 그룹
            const orderSummary = `
        <div>
            <h5>주문 ID: ${orders[0].orderId}</h5>
            <div>주문 총액 : ${formatPrice(orders[0].totalOrderPrice)}<br>
                 결제 상태 : ${orders[0].paymentStatus} <br>
                 배송 상태 : ${orders[0].deliveryStatus}<br>     
            </div>
        </div>
        `;

            const orderItemsContainer = $('<div class="row"></div>'); // 주문 아이템 리스트

            // 각 상품 정보 출력
            orders.forEach(function (order) {
                const orderItem = `
            <div class="col-md-4 mb-4">
                <div class="card">
                    <img src="${order.productImageUrl}" class="card-img-top" alt="${order.productName}">
                    <div class="card-body">
                        <h5 class="card-title">${order.productName}</h5>
                        <p class="card-text">
                            가격 : ${formatPrice(order.productPrice)} <br>
                            사이즈 : ${order.productSize} <br>
                            수량 : ${order.quantity} <br>
                            상품 총액 : ${formatPrice(order.singleItemTotalPrice)}<br> 
                        </p>
                    </div>
                </div>
            </div>
            `;
                orderItemsContainer.append(orderItem);
            });

            // 주문별 요약 및 상품 정보 추가
            orderGroup.append(orderSummary);
            orderGroup.append(orderItemsContainer);
            orderListContainer.append(orderGroup);
        });
    }).fail(function (xhr, status, error) {
        console.error("주문 목록 로드 실패:", error);
    });

}