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
            calculateCartTotal();   // 첫 페이지 로드 시 장바구니 전체 가격 업데이트
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
                            <h5 class="card-title">${item.productName} </h5>
                            <button class="btn btn-danger btn-sm" id="remove-${item.productId}">장바구니 제거</button>
                            <p class="card-text">가격: ${formatPrice(item.productPrice)}</p>

                            <div class="d-flex justify-content-between align-items-center mt-auto">
                                <div class="d-flex align-items-center">
                                    <label for="quantity-${item.productId}" style="margin-right: 10px">구매 수량</label>
                                    <input type="number" class="form-control" 
                                           id="quantity-${item.productId}" 
                                           value="${item.quantity}" 
                                           min="1" 
                                           onchange="updateprice(${item.productId}, ${item.productPrice});" 
                                           style="width: auto; max-width: 80px; border: 2px solid #e5daff;" />
                                </div>
                            </div>
                            <!-- price 영역 -->
                            <div id="price-${item.productId}" style="margin-top: 10px;">
                                총 가격: ${formatPrice(item.productPrice * item.quantity)}
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

        // 장바구니에 아이템이 있을 때 결제 요약 업데이트
        updateOrderSummary();
    } else {
        cartItemsContainer.append(`<p>장바구니에 아이템이 없습니다.<br>
                                    <a href="/product">상품 추가하러 가기</a></p>`); // 장바구니가 비었을 경우
    }
}

// 수량 변경 시 총 가격 업데이트
function updateprice(productId, productPrice) {
    // 수량 input에서 현재 값 가져오기
    const quantity = $(`#quantity-${productId}`).val();

    // 수량이 1보다 작은 값으로 변경되지 않도록 처리
    if (quantity < 1) {
        $(`#quantity-${productId}`).val(1); // UI에서 1로 강제로 설정
        return updateprice(productId, productPrice); // 다시 업데이트 호출
    }

    const totalPrice = productPrice * quantity;

    // 해당 상품의 총 가격을 업데이트
    $(`#price-${productId}`).html(`총 가격: ${formatPrice(totalPrice)}`);

    // 결제 요약 업데이트
    updateOrderSummary();

    // 장바구니 전체 총 가격 업데이트
    calculateCartTotal();
}

// 장바구니에서 아이템 제거
function removeItemFromCart(productId) {
    // 장바구니 상품 제거 여부 확인
    const isConfirmed = confirm("해당 상품을 제거 하시겠습니까?");

    if (isConfirmed) {
        $.ajax({
            url: `/api/cart/${userId}/${productId}`,
            method: 'DELETE',
        }).done(function (data) {
            if (data.responseData === true) {
                alert('아이템이 장바구니에서 제거되었습니다.');
                $(`#remove-${productId}`).closest('.col-sm-6').remove(); // UI에서 제거
                // 장바구니가 비었는지 확인
                if ($('#cart-items .card').length === 0) {
                    // 장바구니가 비었을 때 메시지 표시
                    $('#cart-items').html(`<p>장바구니에 아이템이 없습니다.<br><a href="/product">상품 추가하러 가기</a></p>`);
                }
                // 결제 요약창 업데이트
                updateOrderSummary();
                calculateCartTotal();
            } else {
                alert('아이템 제거에 실패했습니다.');
            }
        }).fail(function () {
            alert('서버와의 연결에 실패했습니다.');
        });
    } else {
        alert('해당 상품제거를 취소했습니다.');
    }
}

// 장바구니에 있는 상품 요약을 결제 요약에 추가하는 함수
function updateOrderSummary() {
    let orderSummaryContainer = $('#order-summary');
    orderSummaryContainer.empty(); // 기존 내용을 초기화

    let cartItems = [];
    // 장바구니에 있는 모든 아이템 정보를 수집
    $('#cart-items .card').each(function () {
        const productId = $(this).find('.btn-danger').attr('id').split('-')[1];
        const productName = $(this).find('.card-title').text().trim();
        const quantity = $(this).find(`#quantity-${productId}`).val();
        const price = $(this).find(`#price-${productId}`).text().replace('총 가격: ₩', '').replace(',', '');
        const totalPrice = parseInt(price) * quantity;

        cartItems.push({ productId, productName, quantity, totalPrice });

        // 상품별 요약 정보 추가
        orderSummaryContainer.append(`
            <div class="order-item">
                <strong>${productName}</strong> x ${quantity} = ${formatPrice(totalPrice)}
            </div>
        `);
    });
}

// 장바구니 모든 상품 총가격 계산
function calculateCartTotal() {
    let cartItems = [];
    // 장바구니에 있는 모든 아이템 정보를 수집
    $('#cart-items .card').each(function () {
        const productId = $(this).find('.btn-danger').attr('id').split('-')[1];
        const quantity = $(this).find(`#quantity-${productId}`).val();
        cartItems.push({
            productId: productId,
            quantity: parseInt(quantity),
        });
    });

    $.ajax({
        url: `/api/cart/${userId}/total`,
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(cartItems), // 장바구니 데이터 전송
    }).done(function (data, status, xhr) {
        if (xhr.status === 200) {
            $('#total-price').html(`<strong>총 결제 금액 : ${formatPrice(data.responseData.totalCost)}</strong>`);
        } else {
            alert('장바구니 계산 중 오류가 발생했습니다.');
        }
    }).fail(function () {
        alert('서버와의 연결에 실패했습니다.');
    });
}

// 결제 처리
function checkout() {
    let cartItems = [];

    // 장바구니에 있는 모든 아이템을 배열로 수집
    $('#cart-items .card').each(function () {
        let productId = $(this).find('.btn-danger').attr('id').split('-')[1];
        let quantity = $(this).find(`#quantity-${productId}`).val();
        cartItems.push({productId: productId, quantity: parseInt(quantity)});
    });

    // 서버에 장바구니 정보 전송 (구매)
    $.ajax({
        url: '/api/cart/checkout',
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({userId: userId, cartItems: cartItems}),
    }).done(function (response) {
        if (response.success) {
            alert('구매가 완료되었습니다!');
            $('#cart-items').empty(); // UI 비우기
        } else {
            alert('구매에 실패했습니다.');
        }
    }).fail(function () {
        alert('서버와의 연결이 실패했습니다.');
    });
}