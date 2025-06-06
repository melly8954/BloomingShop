// 현재 URL에서 파라미터를 추출하는 방법
const pathSegments = window.location.pathname.split('/');
const boardId = pathSegments[pathSegments.length - 1];

$(document).ready(function(){
    loadBoardView();

});

function loadBoardView(){
    $.ajax({
        url: `/api/board/support/${boardId}/details`,
        type: 'GET'
    }).done(function (data) {
        console.log(data);
        // 받은 데이터를 HTML에 동적으로 삽입
        const boardDetails = data.responseData; // 여기서 data는 API에서 받은 게시글 데이터
        const createdDate = new Date(boardDetails.createdDate).toLocaleString();
        const updatedDate = new Date(boardDetails.updatedDate).toLocaleString();

        const boardContent = `
            <div>
                <h2>${boardDetails.title}</h2>
                <p><strong>작성자:</strong> ${boardDetails.authorName}</p>
                <p><strong>작성일:</strong> ${createdDate}</p>
                <p><strong style="margin-bottom: 10px">최종 수정일:</strong> ${updatedDate}</p>
                <p><strong>내용:</strong> ${boardDetails.content}</p>
                ${boardDetails.imageUrl ? `<img src="${boardDetails.imageUrl}" alt="${boardDetails.title}" class="img-fluid">` : ''}
            </div>
        `;
        // 해당 div에 게시글 내용을 삽입
        $('#board-detail').html(boardContent);
        if(data.responseData.isAnswer === false){
            $("#no-answer").show();
        }else{
            const answer = `
                                 <span style="color:red;"> 관리자 </span> 
                                 <div> ${boardDetails.answerContent}</div>
                                 `;
            $("#answer-content").html(answer);
            $("#answer-content").show();
        }
    }).fail(function (jqXHR, textStatus, errorThrown) {
        alert('게시글 정보를 불러오는 데 실패했습니다.');
    });
}

function answerRegister(){
    let answer = $("#answer").val();
    $.ajax({
        url: `/api/admin/board/support/${boardId}/answers`,
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({ answer: answer })
    }).done(function (data) {
        console.log(data);
        if(data.responseData === true){
            alert("답변이 정상적으로 등록되었습니다.");
            loadBoardView();
        }
    }).fail(function (jqXHR, textStatus, errorThrown) {
        alert('답변을 등록하는 데 실패했습니다.');
    });
}

// 게시글 논리 삭제
function deleteBoard(){
    if(!confirm("해당 게시글을 삭제 하시겠습니까?")){
        return;
    }
    $.ajax({
        url: `/api/admin/board/support/${boardId}/status`,
        type: 'PATCH',
    }).done(function (data) {
        console.log(data);
        if(data.responseData === true){
            alert("게시글이 정상적으로 삭제되었습니다.");
            window.location.href="/admin/board/support/list";
        }
    }).fail(function (jqXHR, textStatus, errorThrown) {
        alert('논리삭제에 실패했습니다.');
    });
}