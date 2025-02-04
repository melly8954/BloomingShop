let userId = null; // 로그인 유저 ID
let guestId = null; // 비회원 ID

$(document).ready(function () {
    // 비회원 ID 확인 및 초기화
    guestId = getGuestId();

    // 로그인 여부 확인
    checkLoginStatus();

    // 로그인 유저 주소 출력
    loadUserAddress();
});

function checkLoginStatus() {
    $.ajax({
        url: '/api/user/getUserId',
        method: 'GET',
    }).done(function (data) {
        // 로그인 상태
        userId = data.responseData.id;
        fetchCartItems(userId, true); // 로그인 유저의 장바구니 가져오기
    }).fail(function () {
        // 비로그인 상태
        console.log('로그인하지 않은 상태입니다.');
        fetchCartItems(guestId, false); // 비회원의 장바구니 가져오기
    });
}

// 장바구니 아이템 가져오기 공통 함수
function fetchCartItems(id, isLoggedIn) {
    const apiUrl = isLoggedIn
        ? `/api/cart/user/${id}/list` // 로그인 유저 API
        : `/api/cart/guest/${id}/list`; // 비회원 API (guestId 사용)

    $.ajax({
        url: apiUrl,
        method: 'GET',
    }).done(function (cartData) {
        displayCartItems(cartData.responseData); // 장바구니 아이템 표시
        calculateCartTotal(); // 장바구니 총 가격 계산
    }).fail(function () {
        alert(isLoggedIn
            ? '로그인 유저의 장바구니 정보를 가져오는 데 실패했습니다.'
            : '비회원 장바구니 정보를 가져오는 데 실패했습니다.');
    });
}

// guestId가 없을 경우 새로 생성하는 함수 (예: UUID로 생성)
function getGuestId() {
    let guestId = localStorage.getItem('guestId');

    if (!guestId) {
        guestId = generateGuestId();
        localStorage.setItem('guestId', guestId);
    }
    return guestId;
}

// guestId 생성 함수 (UUID 스타일로 생성)
function generateGuestId() {
    return 'guest-' + Math.random().toString(36).substr(2, 9); // 간단한 랜덤 문자열 생성
}

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
                            <button class="btn btn-danger btn-sm" id="remove-${item.productId}">해당 상품 제거</button>
                            <p class="card-text">가격: ${formatPrice(item.price)}</p>

                            <div class="d-flex justify-content-between align-items-center mt-auto">
                                <div class="d-flex align-items-center">
                                    <label for="quantity-${item.productId}" style="margin-right: 10px">구매 수량</label>
                                    <input type="number" class="form-control" 
                                           id="quantity-${item.productId}" 
                                           value="${item.quantity}" 
                                           min="1" 
                                           onchange="updateprice(${item.productId}, ${item.price});" 
                                           style="width: auto; max-width: 80px; border: 2px solid #e5daff;" />
                                </div>
                            </div>
                            <!-- price 영역 -->
                            <div id="price-${item.productId}" style="margin-top: 10px;">
                                총 가격: ${formatPrice(item.price * item.quantity)}
                            </div>
                        </div>
                    </div>
                </div>
            `;
            cartItemsContainer.append(cartItemHtml); // 장바구니 아이템을 추가

            // 삭제 버튼 이벤트 리스너 추가
            $(`#remove-${item.productId}`).click(function () {
                removeItemFromCartByUser(item.productId);
            });
        });

        // 장바구니에 아이템이 있을 때 결제 요약 업데이트
        updateOrderSummary();
    } else {
        cartItemsContainer.append(`<p class="alert alert-info">장바구니에 상품이 없습니다.<br>
                                    <a href="/product">상품 추가하러 가기</a></p>`); // 장바구니가 비었을 경우
    }
}

// 수량 변경 시 총 가격 업데이트
function updateprice(productId, price) {
    // 수량 input에서 현재 값 가져오기
    const quantity = $(`#quantity-${productId}`).val();

    // 수량이 1보다 작은 값으로 변경되지 않도록 처리
    if (quantity < 1) {
        $(`#quantity-${productId}`).val(1); // UI에서 1로 강제로 설정
        return updateprice(productId, price); // 다시 업데이트 호출
    }

    const totalPrice = price * quantity;

    // 해당 상품의 총 가격을 업데이트
    $(`#price-${productId}`).html(`총 가격: ${formatPrice(totalPrice)}`);

    // 결제 요약 업데이트
    updateOrderSummary();

    // 장바구니 전체 총 가격 업데이트
    calculateCartTotal();
}

// 장바구니에서 해당 상품 제거
function removeItemFromCartByUser(productId) {
    // 장바구니 상품 제거 여부 확인
    const isConfirmed = confirm("해당 상품을 제거 하시겠습니까?");

    if (isConfirmed) {
        // 로그인 여부에 따른 API URL 설정
        const apiUrl = userId
            ? `/api/cart/user/${userId}/${productId}` // 로그인 유저의 장바구니 제거 API
            : `/api/cart/guest/${guestId}/${productId}`; // 비회원의 장바구니 제거 API

        $.ajax({
            url: apiUrl,
            method: 'DELETE',
        }).done(function (data) {
            if (data.responseData === true) {
                alert('상품이 장바구니에서 제거되었습니다.');
                $(`#remove-${productId}`).closest('.col-sm-6').remove(); // UI에서 제거
                // 장바구니가 비었는지 확인
                if ($('#cart-items .card').length === 0) {
                    // 장바구니가 비었을 때 메시지 표시
                    $('#cart-items').html(`<p class="alert alert-info">장바구니에 상품이 없습니다.<br><a href="/product">상품 추가하러 가기</a></p>`);
                }
                // 결제 요약창 업데이트
                updateOrderSummary();
                calculateCartTotal();
            } else {
                alert('아이템 제거에 실패했습니다.');
            }
        }).fail(function () {
            alert('서버와의 연결이 실패했습니다.');
        });
    } else {
        alert('해당 상품 제거를 취소했습니다.');
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
        const priceText = $(this).find('.card-text').text().replace('가격: ', '').replace(',', ''); // 가격 텍스트에서 '가격: ' 제거
        const price = parseInt(priceText.replace('₩', ''));  // 가격 숫자만 남기기
        const totalPrice = price * quantity;

        cartItems.push({productId, productName, quantity, totalPrice});

        // 상품별 요약 정보 추가
        orderSummaryContainer.append(`
            <div class="order-item">
                <strong>${productName}</strong> x ${quantity} = ${formatPrice(totalPrice)}
            </div>
        `);
    });
}

function calculateCartTotal() {
    let cartItems = [];

    // 현재 장바구니의 상품 수집
    $('#cart-items .card').each(function () {
        const productId = $(this).find('.btn-danger').attr('id').split('-')[1];
        const quantity = $(this).find(`#quantity-${productId}`).val();
        cartItems.push({
            productId: productId,
            quantity: parseInt(quantity),
        });
    });

    // 로그인 여부에 따른 API URL 결정
    let url = userId ? `/api/cart/user/${userId}/total` : `/api/cart/guest/${guestId}/total`;

    // AJAX 호출로 총 가격 계산 요청
    $.ajax({
        url: url,
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(cartItems),
    }).done(function (data) {
        $('#total-price').html(`<strong>총 결제 금액 : ${formatPrice(data.responseData.totalCost)}</strong>`);
    }).fail(function () {
        alert('장바구니 계산 중 오류가 발생했습니다.');
    });
}


// 결제 진행 버튼 클릭 시 호출되는 함수
function orderRegister() {
    let orderSummaryContainer = $("#modal-order-summary");
    let totalAmount = 0; // 총 결제 금액 초기화
    let cartItems = [];
    let guestId = getGuestId();
    orderSummaryContainer.empty(); // 내부 콘텐츠 비우기

    // 장바구니에 아이템이 있는지 확인
    if ($('#cart-items .card').length === 0) {
        alert('장바구니에 상품이 없습니다. 상품을 추가해주세요.');
        return; // 장바구니가 비었으면 함수 종료 (주문 신청 진행되지 않음)
    }

    // 장바구니에 있는 모든 아이템 정보를 수집
    $('#cart-items .card').each(function () {
        const productId = $(this).find('.btn-danger').attr('id').split('-')[1];
        const productName = $(this).find('.card-title').text().trim();
        const quantity = $(this).find(`#quantity-${productId}`).val();
        const priceText = $(this).find('.card-text').text().replace('가격: ', '').replace(',', ''); // 가격 텍스트에서 '가격: ' 제거
        const price = parseInt(priceText.replace('₩', '')); // 가격 숫자만 남기기
        const totalPrice = price * quantity;

        // 총 결제 금액 합산
        totalAmount += totalPrice;

        // cartItems에 아이템 추가
        cartItems.push({
            productId: productId,
            quantity: parseInt(quantity),
            price: price,
            totalPrice: totalPrice
        });

        // 상품별 요약 정보 추가
        orderSummaryContainer.append(`
            <div class="order-item">
                <strong>${productName}(${formatPrice(price)})</strong> x ${quantity} = ${formatPrice(totalPrice)}
            </div>
        `);
    });

    // 총 결제 금액 표시
    orderSummaryContainer.append(`
        <div class="order-total">
            <hr />
            <strong>총 결제 금액:</strong> ${formatPrice(totalAmount)}
        </div>
    `);

    // 모달 창 띄우기
    $('#order-regist-modal').modal('show');

    // 주문 신청 버튼에 대한 클릭 이벤트 핸들러 추가
    $('#order-regist').off('click').on('click', function () {
        // 비회원 배송 정보 가져오기
        const postcode = $('#postcode').val(); // 비회원 우편번호
        const address = $('#address').val(); // 비회원 주소
        const detailAddress = $('#detail-address').val(); // 비회원 상세주소

        // 회원 배송 정보 가져오기
        const userPostcode = $('#user-postcode').val(); // 회원 우편번호
        const userAddress = $('#user-address').val(); // 회원 주소

        // 최종 배송 주소 합치기
        const shippingAddress = `${postcode} ${address} ${detailAddress}`.trim();

        // 회원인지 여부 확인
        if ($('#user-address-section').length > 0) {
            // 회원인 경우 유효성 검사
            if (!userPostcode || !userAddress) {
                alert('회원 배송 정보가 누락되었습니다. 관리자에게 문의하세요.');
                return;
            }
        } else {
            // 비회원인 경우 유효성 검사
            if (!postcode || !address) {
                alert('모든 배송 정보를 입력해주세요.');
                return;
            }
        }

        const paymentMethod = $('input[name="paymentMethod"]:checked').val();

        // 로그인 여부에 따라 userId 또는 guestId 설정
        const id = userId ? userId : null;  // 로그인 시 userId, 비로그인 시 null
        guestId = userId ? null : guestId;  // 로그인 시 guestId는 null, 비로그인 시 guestId 사용
        const idType = userId ? "userId" : "guestId";  // idType을 "userId" 또는 "guestId"로 설정

        $.ajax({
            url: '/api/order/register',
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({
                id: id,  // 로그인 시 userId, 비로그인 시 null
                guestId: guestId,  // 비로그인 시 guestId 값을 설정
                idType: idType,  // "userId" 또는 "guestId"
                cartItems: cartItems,
                shippingAddress: shippingAddress,
                paymentMethod: paymentMethod,
            })
        }).done(function (data, xhr) {
            if (data.responseData === true) {
                alert('주문 신청 성공!');
                $('#order-regist-modal').modal('hide');
                clearCartAfterOrder();
                location.href="/order/list";
            } else {
                alert('주문 신청에 실패했습니다.');
            }
        }).fail(function () {
            alert('서버와의 연결이 실패했습니다.');
        });
    })
}

// 주소 검색 버튼 클릭 시 주소 입력 필드 활성화
function searchPostcode() {
    new daum.Postcode({
        oncomplete: function (data) {
            var addr = ''; // 주소 변수

            if (data.userSelectedType === 'R') { // 도로명 주소 선택시
                addr = data.roadAddress;
            } else { // 지번 주소 선택시
                addr = data.jibunAddress;
            }

            // 우편번호와 주소 입력
            document.getElementById('postcode').value = data.zonecode;
            document.getElementById("address").value = addr;

            // 상세주소 필드로 포커스 이동
            document.getElementById("detail-address").focus();
        }
    }).open();
}

// 로그인 유저 주소 출력
function loadUserAddress() {
    $.ajax({
        url: '/api/user/user-address', // API 주소
        type: 'GET',
        dataType: 'json'
    }).done(function (data) {
        // 응답 성공 시, 주소 정보를 템플릿에 반영
        $('#user-address').val(data.responseData.address);
        $('#user-postcode').val(data.responseData.postcode);
        $('#user-detail-address').val(data.responseData.detailAddress);
    }).fail(function (xhr, status, error) {// 실패 시, 오류 메시지 출력
        console.log('주소 정보를 가져오는 데 실패했습니다. 다시 시도해주세요.');
    });
}

// 장바구니 항목 주문 신청 시 항목 삭제
function clearCartAfterOrder(){
    const guestId = getGuestId();
    $.ajax({
        url: '/api/cart',
        type: 'DELETE',
        dataType: 'json',
        headers: {
            'Guest-Id': guestId
        }
    }).done(function (data) {
        console.log(data);
    }).fail(function () {
        alert('서버와의 연결이 실패했습니다.');
    });
}