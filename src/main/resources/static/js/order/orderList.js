$(document).ready(function() {
    orderList();

    // 오름차순 버튼 클릭 시
    $('#sort-asc').click(function() {
        orderList('asc');
    });

    // 내림차순 버튼 클릭 시
    $('#sort-desc').click(function() {
        orderList('desc');
    });

    // 주문 내역 보관 메시지 표시
    checkLoginStatus();
});

// guestId가 없을 경우 새로 생성하는 함수 (예: UUID로 생성)
function getGuestId() {
    let guestData = JSON.parse(localStorage.getItem('guestData'));

    // guestData가 없거나 만료된 경우
    if (!guestData || isGuestIdExpired(guestData.createdAt)) {
        guestData = {
            guestId: generateGuestId(),
            createdAt: new Date().toLocaleString() // 로컬 시간 형식으로 반환
        };
        localStorage.setItem('guestData', JSON.stringify(guestData)); // guestData 저장
    }

    return guestData.guestId;
}

// guestId 생성 함수 (UUID 스타일로 생성)
function generateGuestId() {
    return 'guest-' + Math.random().toString(36).substr(2, 9); // 간단한 랜덤 문자열 생성
}

// guestId의 만료 여부를 체크하는 함수 (1일=86400000ms)
function isGuestIdExpired(createdAt) {
    const currentTime = new Date().getTime();
    const guestIdCreationTime = new Date(createdAt).getTime();
    const timeDifference = currentTime - guestIdCreationTime;
    return timeDifference > 86400000; // 1일 (24시간) 초과하면 만료
}

// 날짜 형식 변환 함수 (예: "2025-01-31T18:44:47" → "2025-01-31")
function formatDate(dateStr) {
    const d = new Date(dateStr);
    const year = d.getFullYear();
    const month = ("0" + (d.getMonth() + 1)).slice(-2);
    const day = ("0" + d.getDate()).slice(-2);
    return year + "-" + month + "-" + day;
}

// 가격 포맷팅 함수
function formatPrice(price) {
    if (price == null || isNaN(price)) {
        return '₩0';  // 유효하지 않은 가격일 경우 기본값 '₩0' 반환
    }
    return '₩' + price.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

function checkLoginStatus() {
    $.ajax({
        url: '/api/user/getUserId',
        method: 'GET',
    }).done(function (data) {
        // 로그인 상태
        const userId = data.responseData.id;
        displayLoginNotice(userId);  // 로그인된 사용자에 대한 UI 처리
    }).fail(function () {
        // 비로그인 상태
        displayGuestOrderNotice();  // 비회원 메시지 표시
        console.log('로그인하지 않은 상태입니다.');
    });
}

function displayLoginNotice(userId) {
    $('#notice').html(''); // 로그인된 경우 메시지 지우기
    console.log('로그인된 사용자:', userId); // 로그인된 사용자 정보 출력
}

function displayGuestOrderNotice() {
    const guestData = JSON.parse(localStorage.getItem('guestData'));
    if (guestData) {
        const noticeMessage = '<h4 class="text-center mb-1">* 비회원의 주문내역은 7일동안 보관되며, 이후에는 고객센터에 문의하십시오. *</h4>';
        $('#notice').html(noticeMessage);
    }
}

// 주문 목록 출력 함수
function orderList(orderType = 'asc') {
    const guestId = getGuestId();

    $.ajax({
        url: '/api/order/list',
        type: 'GET',
        dataType: 'json',
        headers: {
            'Guest-Id': guestId
        }
    }).done(function(data) {
        const orderListContainer = $('#order-list');
        orderListContainer.empty(); // 기존 주문 목록 초기화
        console.log(data);

        if (data.responseData.length === 0) {
            orderListContainer.append('<div class="alert alert-info">현재 주문 내역이 없습니다.</div>');
            return;
        }

        // 주문을 orderId별로 그룹화
        const groupedOrders = data.responseData.reduce((groups, order) => {
            if (!groups[order.orderId]) {
                groups[order.orderId] = [];
            }
            groups[order.orderId].push(order);
            return groups;
        }, {});

        // 그룹화된 주문을 배열로 변환하고 orderId에 따라 정렬
        const sortedOrderIds = Object.keys(groupedOrders).sort((a, b) => {
            if (orderType === 'asc') {
                return a - b; // 오름차순 정렬
            } else {
                return b - a; // 내림차순 정렬
            }
        });

        // 정렬된 orderId를 순회하여 표시
        sortedOrderIds.forEach(function(orderId) {
            const orders = groupedOrders[orderId];
            const orderGroup = $('<div class="order-group mb-5"></div>');
            const orderSummary = `
                <div>
                    <h5>주문 ID: ${orders[0].orderId} <button id="order-cancel-${orders[0].orderId}" onclick="orderCancel(${orders[0].orderId});">주문 취소</button> </h5>
                    <div><span>주문 일시 : ${formatDate(orders[0].createdDate)}</span><br>
                         <span>주문 총액 : ${formatPrice(orders[0].totalOrderPrice)}</span><br>
                         <span>주문 상태 : ${orders[0].paymentStatus}
                         ${orders[0].paymentStatus === '결제 진행 중' ? `<button id="payment-btn-${orders[0].orderId}" onclick="payment(${orders[0].orderId});">결제 진행</button>` : ''}</span><br>
                         <span>배송 주소 : ${orders[0].userAddress ? orders[0].userAddress : orders[0].guestAddress || '주소 정보 없음'}</span><br>             
                         <span id="delivery-status-${orders[0].orderId}" ${orders[0].paymentStatus === '결제 완료' ? '' : 'style="display: none;"'}>배송 상태 : ${orders[0].deliveryStatus}</span>        
                    </div>
                </div>
            `;

            const orderItemsContainer = $('<div class="row"></div>');
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

            orderGroup.append(orderSummary);
            orderGroup.append(orderItemsContainer);
            orderListContainer.append(orderGroup);
        });
    }).fail(function (xhr, status, error) {
        console.error("주문 목록 로드 실패:", error);
    });
}

// 주문 id 별 결제 상태 업데이트
function payment(orderId){
    // 사용자에게 확인 요청
    if (!confirm('결제 하겠습니까?')) {
        return;
    }
    $.ajax({
        url: `/api/order/${orderId}/payment-status`,
        type: 'PATCH',
    }).done(function (data) {
        console.log(data);
        $(`#payment-btn-${orderId}`).hide();    // 결제 버튼 숨기기
        orderList();    // 주문 목록 갱신
        alert("결제가 완료되었습니다.");
    }).fail(function (jqXHR, textStatus, errorThrown) {
        console.log(errorThrown);
        alert("결제를 실패하셨습니다.")
    })
}

// 주문 취소
function orderCancel(orderId){
    if (!confirm('해당 주문을 취소 하겠습니까?')) {
        return;
    }
    $.ajax({
        url: `/api/order/${orderId}/cancel`,
        type: 'PATCH',
    }).done(function (data) {
        console.log(data);
        orderList();    // 주문 목록 갱신
        alert("주문 취소가 완료되었습니다.");
    }).fail(function (jqXHR, textStatus, errorThrown) {
        console.log(errorThrown);
    })
}