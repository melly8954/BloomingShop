$(document).ready(function () {
    loadBoardList();
});

// 게시글 목록 로드 함수
function loadBoardList() {
    $.ajax({
        url: '/api/support/list',  // 게시글 목록을 가져오는 URL
        type: 'GET'
    }).done(function (data) {
        const boardListContainer = $('#board-list');
        boardListContainer.empty();  // 기존 내용을 비우고 새로 추가

        data.responseData.forEach(function (board) {
            const boardItem = $('<div>').addClass('board-item').attr('id', 'board-' + board.id);
            boardItem.append('<h3>' + board.title + '</h3>');
            boardItem.append('<p>조회수: ' + board.viewQty + '</p>');
            boardItem.append('<p>작성자: ' + board.authorId + '</p>');
            boardItem.append('<p>작성일: ' + board.createdDate + '</p>');

            // 비밀글일 경우 비밀번호 입력 폼
            if (board.isSecret) {
                boardItem.append('<div id="board-secret-' + board.id + '"><strong>비밀글입니다. 비밀번호를 입력해주세요.</strong></div>');
                boardItem.append('<form class="secret-form" data-board-id="' + board.id + '"><input type="password" name="password" placeholder="비밀번호" required><button type="submit">확인</button></form>');
            } else {
                // 비밀글이 아니면 바로 내용 표시
                boardItem.append('<div id="board-content-' + board.id + '">' + board.content + '</div>');
            }

            $('#board-list').append(boardItem);
        });
    }).fail(function () {
        alert('게시글 목록을 불러오는 데 실패했습니다.');
    });
}