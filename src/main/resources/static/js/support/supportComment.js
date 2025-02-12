// 현재 URL에서 파라미터를 추출하는 방법
const pathSegments = window.location.pathname.split('/');
const boardId = pathSegments[pathSegments.length - 1];

const pageSize = 5;
let currentPage = 1;

$(document).ready(function () {
    loadComments(currentPage,pageSize); // 페이지 로드 시 댓글 목록을 가져옴
})

// 날짜 형식 변환 함수 (예: "2025-01-31T18:44:47" → "2025-01-31")
function formatDate(dateStr) {
    const d = new Date(dateStr);
    const year = d.getFullYear();
    const month = ("0" + (d.getMonth() + 1)).slice(-2);
    const day = ("0" + d.getDate()).slice(-2);
    return year + "-" + month + "-" + day;
}

// 댓글 목록 로드
function loadComments(page,size) {
    $.ajax({
        url: `/api/support/board/${boardId}/comments?page=${page-1}&size=${size}`,
        type: 'GET',
    }).done(function (data) {
        let commentsHtml = '';
        data.responseData.content.forEach(comment => {
            commentsHtml += `
                        <div class="comment mb-4 p-3 border rounded shadow-sm bg-light">
                            <div class="d-flex justify-content-between">
                                <strong class="fw-bold text-primary">작성자 : ${comment.commentAuthorName}</strong>
                                <small class="text-muted">${formatDate(comment.createdDate)}</small>
                            </div>
                            <hr>
                            <p class="mt-2">${comment.commentContent}</p>
                        </div>
                        `;
        });
        $('#comments-section').html(commentsHtml);
        // 페이지네이션 버튼 업데이트
        currentPage = page;
        makePageUI(data.responseData);
    }).fail(function () {
        console.log('댓글 목록 조회 실패', error);
    })
}

function makePageUI(data) {
    const rowsPerPage = 5; // 페이지 당 항목 수
    const totalPages = Math.ceil(data.totalElements / rowsPerPage); // 전체 페이지 수
    const startPage = getStartPage(currentPage);
    const endPage = getEndPage(startPage, data.totalPages);

    // 페이지네이션 HTML 초기화
    let paginationHTML = `<nav aria-label="Page navigation example"><ul class="pagination justify-content-center">`;

    // Previous 버튼
    let prevPage = currentPage > 1 ? currentPage - 1 : 1;
    paginationHTML += `<li class="page-item">
        <a class="page-link btn btn-outline-primary" onclick="loadComments(${prevPage}, ${rowsPerPage})">Prev</a>
    </li>`;

    // 페이지 버튼들
    for (let i = startPage; i <= endPage; i++) {
        let sClass = i === currentPage ? 'page-item active' : 'page-item';
        paginationHTML += `<li class="${sClass}">
            <a class="page-link btn btn-outline-primary" onclick="loadComments(${i}, ${rowsPerPage})">${i}</a>
        </li>`;
    }

    // Next 버튼
    let nextPage = currentPage < totalPages ? currentPage + 1 : totalPages;
    paginationHTML += `<li class="page-item">
        <a class="page-link btn btn-outline-primary" onclick="loadComments(${nextPage}, ${rowsPerPage})">Next</a>
    </li>`;

    paginationHTML += `</ul></nav>`;

    $("#pagination").html(paginationHTML);
}

// 시작 페이지 계산
function getStartPage(page) {
    return Math.floor((page - 1) / 5) * 5 + 1;
}

// 끝 페이지 계산
function getEndPage(startPage, totalPages) {
    return Math.min(startPage + 4, totalPages);
}

// 댓글 작성
function commentRegister() {
    const commentAuthorName = $('#comment-author-name').val();
    const commentContent = $('#comment-content').val();

    $.ajax({
        url: `/api/support/board/${boardId}/comments`,
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({
            commentAuthorName: commentAuthorName,
            commentContent: commentContent
        })
    }).done(function (data) {
        console.log(data);
        if(data.responseData === true){
            $('#comment-author-name').val('');  // 입력란 초기화
            $('#comment-content').val(''); // 입력란 초기화
            loadComments(currentPage,pageSize); // 댓글 목록 다시 로드
        }
    }).fail(function (xhr, status, error) {
        console.log('댓글 등록 실패', error);
    })
}