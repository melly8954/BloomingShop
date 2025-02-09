// 기본값 설정
let currentPage = 1;
const pageSize = 6;
let sortField = "";
let sortOrder = "asc"; // 기본 정렬 방식

$(document).ready(function () {
    sortField = $("#sort").val();

    // 초기 주문 목록 로드
    loadOrders(currentPage, pageSize, sortField, sortOrder);

    // 정렬 기준 변경 시 (올바른 선택자 사용)
    $("#sort").on("change", function () {
        sortField = $(this).val(); // 선택된 value 값을 가져옴
        loadOrders(currentPage, pageSize, sortField, sortOrder);
    });

    // 정렬 버튼 클릭 시
    $(".btn-group button").on("click", function () {
        $(".btn-group button").removeClass("active");
        $(this).addClass("active");
        sortOrder = $(this).val(); // 버튼의 value 값을 가져옴
        loadOrders(currentPage, pageSize, sortField, sortOrder);
    });

    // 배달 상태 변경 시 DB 업데이트
    $(document).on('change', '.delivery-status-select', function() {
        const orderId = $(this).data('orderid');
        const newStatus = $(this).val();

        // AJAX 요청으로 상태 업데이트
        updateDeliveryStatus(orderId, newStatus);
    });
});

// 주문 목록 로드 함수
function loadOrders(page, size, sort, order) {
    $.ajax({
        url: '/api/admin/order/list',
        type: 'GET',
        data: {
            page: page,
            size: size,
            sort: sort,
            sortOrder: order
        },
        dataType: 'json'
    }).done(function (response) {
        console.log(response);
        // 응답 객체에서 responseData를 가져옴
        if (response && response.responseData) {
            renderOrders(response.responseData);
            makePageUI(response.responseData);
        } else {
            $("#orderList").html("<p>주문 데이터가 없습니다.</p>");
            $("#orderPagination").empty();
        }
    }).fail(function (xhr, status, error) {
        console.error("오류 발생:", error);
        $("#orderList").html("<p>주문 데이터를 불러오는 중 오류가 발생했습니다.</p>");
        $("#orderPagination").empty();
    });
}

// 주문 데이터 렌더링
function renderOrders(data) {
    let orders = data.orders;
    let html = "";

    if (orders && orders.length > 0) {
        html += '<div class="row">';

        orders.forEach(function (order) {
            let createdDate = order.createdDate ? formatDate(order.createdDate) : "";
            let totalOrderPrice = formatPrice(order.totalPrice);

            // 회원 정보 처리
            let userInfo = order.userId
                ? `<p class="card-text"><strong>회원 ID :</strong> ${order.userId.id || ""}</p>
                   <p class="card-text"><strong>회원 이름 :</strong> ${order.userId.name || ""}</p>`
                : `<p class="card-text"><strong>비회원 ID :</strong> ${order.guestId || ""}</p>`;

            // 배송지 처리
            let shippingAddress = "";
            if (order.userId && order.addressId) {
                // 회원의 배송지 정보
                shippingAddress = `
                    <p class="card-text"><strong>배송지 :</strong> ${order.addressId.address}, ${order.addressId.detailAddress}</p>
                    <p class="card-text"><strong>우편번호 :</strong> ${order.addressId.postcode}</p>
                `;
            } else if (order.guestId) {
                // 비회원의 배송지 정보
                shippingAddress = `<p class="card-text"><strong>배송지 :</strong> ${order.shippingAddressNonMember || "없음"}</p>`;
            }

            // 배송 상태 처리
            let deliveryStatusHtml = "";
            if (order.paymentStatus === "결제 완료") {
                let options = ["배송 준비 중", "배송 중", "배송 완료"].map(option =>
                    `<option value="${option}" ${order.deliveryStatus === option ? "selected" : ""}>${option}</option>`
                ).join("");
                deliveryStatusHtml = `
                    <p class="card-text"><strong>배송 상태</strong> 
                        <select class="delivery-status-select form-control" data-orderid="${order.orderId}">
                            ${options}
                        </select>
                    </p>
                `;
            } else if (order.paymentStatus === "결제 진행 중") {
                deliveryStatusHtml = `<p class="card-text delivery-status" style="display: none;">`; // 결제 진행 중일 때 배송 상태 숨김
            } else {
                deliveryStatusHtml = `<p class="card-text"><strong>배송 상태</strong> ${order.deliveryStatus || ""}</p>`;
            }

            // 주문번호 옆에 이미지 버튼 추가
            let deleteButtonHtml = `
                <img src="/image/delete.png" alt="삭제" class="delete-button" onclick="deleteOrder(${order.orderId})" style="cursor: pointer; width: 20px; height: 20px; margin-left: 10px;" />
            `;

            html += `
                <div class="col-md-auto mb-5">
                    <div class="card">
                        <div class="card-header">주문번호 : ${order.orderId || ""} ${deleteButtonHtml}</div>
                        <div class="card-body">
                            ${userInfo}
                            <p class="card-text"><strong>주문일자 :</strong> ${createdDate}</p>
                            <p class="card-text"><strong>총 금액 :</strong> ${totalOrderPrice || ""}</p>
                            <p class="card-text"><strong>결제 방식 :</strong> ${order.paymentMethod || ""}</p>
                            <p class="card-text"><strong>결제 상태 :</strong> ${order.paymentStatus || ""}</p>
                            ${shippingAddress} <!-- 배송지 추가 -->
                            ${deliveryStatusHtml}
                        </div>
                    </div>
                </div>`;
        });

        html += '</div>'; // row 닫기
    } else {
        html = "<p>주문 데이터가 없습니다.</p>";
    }

    $("#orderList").html(html);
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
    return '₩' + price.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

// 페이징 UI 렌더링 함수 (makePageUI 스타일로 수정)
function makePageUI(data) {
    const totalElements = data.totalElements;
    const rowsPerPage = 6; // 페이지 당 항목 수
    const totalPages = Math.ceil(totalElements / rowsPerPage); // 전체 페이지 수
    const currentPage = data.currentPage + 1;   // responseData.currentPage가 0부터 시작하므로 1을 더해준다.
    const startPage = getStartPage(currentPage);
    const endPage = getEndPage(startPage, totalPages);

    // 페이지네이션 HTML 초기화
    let paginationHTML = `<nav aria-label="Page navigation example"><ul class="pagination justify-content-center">`;

    // Previous 버튼
    let prevPage = currentPage > 1 ? currentPage - 1 : 1;
    paginationHTML += `<li class="page-item">
        <a class="page-link btn btn-outline-primary" onclick="loadOrders(${prevPage}, ${pageSize}, '${sortField}', '${sortOrder}')">Prev</a>
    </li>`;

    // 페이지 버튼들
    for (let i = startPage; i <= endPage; i++) {
        let sClass = i === currentPage ? 'page-item active' : 'page-item';
        paginationHTML += `<li class="${sClass}">
            <a class="page-link btn btn-outline-primary" onclick="loadOrders(${i}, ${pageSize}, '${sortField}', '${sortOrder}')">${i}</a>
        </li>`;
    }

    // Next 버튼
    let nextPage = currentPage < totalPages ? currentPage + 1 : totalPages;
    paginationHTML += `<li class="page-item">
        <a class="page-link btn btn-outline-primary" onclick="loadOrders(${nextPage}, ${pageSize}, '${sortField}', '${sortOrder}')">Next</a>
    </li>`;

    paginationHTML += `</ul></nav>`;

    $("#orderPagination").html(paginationHTML);
}

// 시작 페이지 계산
function getStartPage(page) {
    return Math.floor((page - 1) / 5) * 5 + 1;
}

// 끝 페이지 계산
function getEndPage(startPage, totalPages) {
    return Math.min(startPage + 4, totalPages);
}

// 배달 상태 업데이트 함수
function updateDeliveryStatus(orderId, deliveryStatus) {
    $.ajax({
        url: '/api/admin/order/delivery-status',
        type: 'PATCH',
        data: JSON.stringify({
            orderId: orderId,       // 객체를 JSON으로 변환하여 보내기
            deliveryStatus: deliveryStatus
        }),
        dataType: 'json',
        contentType: 'application/json',
    }).done(function(response) {
        alert("배송 상태가 변경되었습니다."); // 성공 메시지 표시
    }).fail(function(xhr, status, error) {
        console.error("배달 상태 업데이트 오류:", error);
        alert("배달 상태 업데이트 중 오류가 발생했습니다.");
    });
}

// 주문 항목 삭제
function deleteOrder(orderId) {
    if (!confirm("해당 주문 항목을 삭제 하시겠습니까?")) {
        return;
    }
    $.ajax({
        url: `/api/order/${orderId}/cancel`,
        type: 'PATCH',
    }).done(function (data) {
        console.log(data);
        loadOrders(currentPage, pageSize, sortField, sortOrder); // 삭제 후 페이지 새로고침
        alert("주문 취소가 완료되었습니다.");
    }).fail(function (jqXHR, textStatus, errorThrown) {
        console.log(errorThrown);
    })
}