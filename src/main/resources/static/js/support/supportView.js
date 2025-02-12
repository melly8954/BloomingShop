$(document).ready(function(){
    // 현재 URL에서 파라미터를 추출하는 방법
    const pathSegments = window.location.pathname.split('/');
    const boardId = pathSegments[pathSegments.length - 1];

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
            $("#answer-content").show();
        }
    }).fail(function (jqXHR, textStatus, errorThrown) {
        alert('게시글 정보를 불러오는 데 실패했습니다.');
    });
});