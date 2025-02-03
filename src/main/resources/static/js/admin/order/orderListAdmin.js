$(document).ready(function () {
    // 초기 주문 목록 로드
    loadOrders(currentPage, pageSize, sortField, sortOrder);
});

// 기본값 설정
let currentPage = 1;  // Make sure currentPage is defined at the top level
const pageSize = 5;
const sortField = "orderId"; // 필요에 따라 변경
const sortOrder = "asc";     // 또는 "desc"

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
            renderPagination(response.responseData);
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

        $.each(orders, function (index, order) {
            let createdDate = order.createdDate ? formatDate(order.createdDate) : "";

            // 회원 정보 처리
            let userInfo = order.userId
                ? `<p class="card-text"><strong>회원 ID:</strong> ${order.userId.id || ""}</p>
                   <p class="card-text"><strong>회원 이름:</strong> ${order.userId.name || ""}</p>`
                : `<p class="card-text"><strong>비회원 ID:</strong> ${order.guestId || ""}</p>`;

            // 배송 상태 처리
            let deliveryStatusHtml = "";
            if (order.paymentStatus === "결제 완료") {
                let options = ["배송 준비 중", "배송 중", "배송 완료"].map(option =>
                    `<option value="${option}" ${order.deliveryStatus === option ? "selected" : ""}>${option}</option>`
                ).join("");
                deliveryStatusHtml = `
                    <p class="card-text"><strong>배송 상태:</strong> 
                        <select class="delivery-status-select form-control" data-orderid="${order.orderId}">
                            ${options}
                        </select>
                    </p>
                    `;
            } else if (order.paymentStatus === "결제 진행 중") {
                deliveryStatusHtml = `<p class="card-text"><strong>배송 상태:</strong> 결제 진행 중</p>`;
            } else {
                deliveryStatusHtml = `<p class="card-text"><strong>배송 상태:</strong> ${order.deliveryStatus || ""}</p>`;
            }

            html += `
                        <div class="col-md-auto mb-3">
                            <div class="card">
                                <div class="card-header">주문번호: ${order.orderId || ""}</div>
                                <div class="card-body">
                                    <p class="card-text"><strong>주문일자:</strong> ${createdDate}</p>
                                    <p class="card-text"><strong>총 금액:</strong> ${order.totalPrice || ""}</p>
                                    <p class="card-text"><strong>결제 방식:</strong> ${order.paymentMethod || ""}</p>
                                    ${userInfo}
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

// 페이징 UI 렌더링 함수 (makePageUI 스타일로 수정)
function renderPagination(data) {
    const totalElements = data.totalElements;
    const rowsPerPage = 5; // 페이지 당 항목 수
    const totalPages = Math.ceil(totalElements / rowsPerPage); // 전체 페이지 수
    const startPage = getStartPage(currentPage);
    const endPage = getEndPage(startPage, totalPages);

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

// 페이징 링크 클릭 이벤트 처리
$(document).on("click", ".page-link", function (e) {
    e.preventDefault();
    let selectedPage = $(this).text(); // 수정: .data("page") 대신 .text()를 사용
    currentPage = parseInt(selectedPage); // 클릭된 페이지 번호 업데이트
    loadOrders(currentPage, pageSize, sortField, sortOrder); // 페이지 로드
});
