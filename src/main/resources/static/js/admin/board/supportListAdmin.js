const pageSize = 10;

$(document).ready(function () {
    const title = $('#search-title');
    const sortSelect = $('#sort');  // 정렬 기준 select
    const ascBtn = $('#ascBtn');    // 오름차순 버튼
    const descBtn = $('#descBtn');  // 내림차순 버튼

    let currentPage = 1;
    let sortBy = sortSelect.val();  // 초기 정렬 기준
    let sortOrder = 'desc';  // 초기 정렬 순서

    loadBoardList(currentPage, title.val(), sortBy, sortOrder);

    // 검색 버튼 클릭 시 호출
    $('#searchBtn').on('click', function() {
        loadBoardList(1, title.val(), sortBy, sortOrder); // 검색어와 카테고리 값으로 첫 번째 페이지 로드
    });

    // 리셋 버튼 클릭 시 호출
    $('#resetBtn').on('click', function() {
        title.val('');  // 검색어 초기화
        loadBoardList(1, '', sortBy, sortOrder);  // 전체 상품 목록을 첫 번째 페이지로 로드
    });

    // 정렬 기준 선택 시 호출
    sortSelect.on('change', function() {
        sortBy = sortSelect.val();  // 선택된 정렬 기준
        loadBoardList(1, title.val(), sortBy, sortOrder);  // 첫 번째 페이지로 로드
    });

    // 오름차순 버튼 클릭 시 호출
    ascBtn.on('click', function () {
        sortOrder = 'asc'; // 오름차순
        toggleActiveButton(ascBtn, descBtn); // 버튼 활성화 효과
        loadBoardList(1, title.val(), sortBy, sortOrder); // 첫 번째 페이지로 로드
    });

    // 내림차순 버튼 클릭 시 호출
    descBtn.on('click', function () {
        sortOrder = 'desc'; // 내림차순
        toggleActiveButton(descBtn, ascBtn); // 버튼 활성화 효과
        loadBoardList(1, title.val(), sortBy, sortOrder); // 첫 번째 페이지로 로드
    });
});

// 활성화 버튼 토글 함수
function toggleActiveButton(activeBtn, inactiveBtn) {
    activeBtn.addClass('btn-primary').removeClass('btn-outline-secondary'); // 활성화된 버튼 스타일
    inactiveBtn.addClass('btn-outline-secondary').removeClass('btn-primary'); // 비활성화된 버튼 스타일
}

// 게시글 목록 로드 함수
function loadBoardList(page, title, sortBy, sortOrder) {
    $.ajax({
        url: `/api/admin/board/support/list?page=${page-1}&title=${title}&sort=${sortBy}&order=${sortOrder}&size=${pageSize}`,  // 게시글 목록을 가져오는 URL
        type: 'GET'
    }).done(function (data) {
        console.log(data);
        const boardListContainer = $('#board-list');
        boardListContainer.empty();  // 기존 내용을 비우고 새로 추가
        data.responseData.supportBoards.forEach(function(board) {
            const createdDate = formatDate(board.createdDate);
            // 게시글 제목 처리 (삭제 여부에 따라 다르게)
            let titleHtml = board.deletedFlag
                ? `<s style="opacity: 0.5; text-decoration:line-through; color:red;">${board.title}</s>`  // 삭제된 게시글이면 취소선
                : board.isSecret
                    ? `<a href="/admin/board/support/view/${board.id}" class="secret-board cursor-pointer text-decoration-none fw-bold">${board.title}</a> 
                        <span class="text-danger">🔒 비밀글</span>`
                    : `<a href="/admin/board/support/view/${board.id}" class="text-decoration-none fw-bold">${board.title}</a>`;

            // 삭제된 게시글이면 복구 버튼 추가
            let restoreButton = board.deletedFlag
                ? `<button class="btn btn-success btn-sm ms-2" onclick="restoreBoard(${board.id}, ${page}, '${title}', '${sortBy}', '${sortOrder}')">게시글 복구</button>`
                : '';

            const boardItem = `
                <li class="list-group-item d-flex justify-content-between align-items-center">
                    <div>
                        ${titleHtml} ${restoreButton} 
                        <small class="text-muted d-block">작성자: ${board.authorName} | 조회수: ${board.viewQty} | 작성일: ${createdDate}</small>
                    </div>
                </li>
            `;
            boardListContainer.append(boardItem);
        });
        // 페이지네이션 UI 생성
        makePageUI(data.responseData.totalElements, page, "#pagination", sortOrder);
    }).fail(function () {
        alert('게시글 목록을 불러오는 데 실패했습니다.');
    });
}


// 날짜 형식 변환 함수 (예: "2025-01-31T18:44:47" → "2025-01-31")
function formatDate(dateStr) {
    const d = new Date(dateStr);
    const year = d.getFullYear();
    const month = ("0" + (d.getMonth() + 1)).slice(-2);
    const day = ("0" + d.getDate()).slice(-2);
    return year + "-" + month + "-" + day;
}

function makePageUI(totalElements, currentPage, pageDivId, sortOrder) {
    const rowsPerPage = 10; // 페이지 당 항목 수
    const totalPages = Math.ceil(totalElements / rowsPerPage); // 전체 페이지 수
    const startPage = getStartPage(currentPage);
    const endPage = getEndPage(startPage, totalPages);

    // 페이지네이션 HTML 초기화
    let paginationHTML = `<nav aria-label="Page navigation example"><ul class="pagination justify-content-center">`;

    // Previous 버튼
    let prevPage = currentPage > 1 ? currentPage - 1 : 1;
    paginationHTML += `<li class="page-item">
        <a class="page-link btn btn-outline-primary" onclick="loadBoardList(${prevPage}, '${$('#search-title').val()}', '${$('#sort').val()}', '${sortOrder}')">Prev</a>
    </li>`;

    // 페이지 버튼들
    for (let i = startPage; i <= endPage; i++) {
        let sClass = i === currentPage ? 'page-item active' : 'page-item';
        paginationHTML += `<li class="${sClass}">
            <a class="page-link btn btn-outline-primary" onclick="loadBoardList(${i}, '${$('#search-title').val()}', '${$('#sort').val()}', '${sortOrder}')">${i}</a>
        </li>`;
    }

    // Next 버튼
    let nextPage = currentPage < totalPages ? currentPage + 1 : totalPages;
    paginationHTML += `<li class="page-item">
        <a class="page-link btn btn-outline-primary" onclick="loadBoardList(${nextPage}, '${$('#search-title').val()}', '${$('#sort').val()}', '${sortOrder}')">Next</a>
    </li>`;

    paginationHTML += `</ul></nav>`;

    $(pageDivId).html(paginationHTML);
}

// 시작 페이지 계산
function getStartPage(page) {
    return Math.floor((page - 1) / 5) * 5 + 1;
}

// 끝 페이지 계산
function getEndPage(startPage, totalPages) {
    return Math.min(startPage + 4, totalPages);
}

// 삭제된 게시글 복구
function restoreBoard(boardId, page, title, sortBy, sortOrder){
    if(!confirm("해당 게시글을 복구 하시겠습니까?")){
        return;
    }
    $.ajax({
        url: `/api/admin/board/support/${boardId}/status`,
        type: 'PATCH',
    }).done(function (data) {
        console.log(data);
        if(data.responseData === true){
            alert("게시글이 정상적으로 복구되었습니다.");
            loadBoardList(page, title, sortBy, sortOrder); // 복구 후 목록 새로고침
        }
    }).fail(function (jqXHR, textStatus, errorThrown) {
        alert('게시글 복구에 실패했습니다.');
    });
}