$(document).ready(function() {
    $.loadUserList(); // 바뀐 함수 이름으로 호출

    // 엔터 키로 버튼 클릭 효과 추가
    $("#findName").keypress(function(event) {
        if (event.which === 13) {  // Enter key code
            event.preventDefault();  // 기본 엔터키 동작 방지
            $.showUser();  // 조회 버튼 클릭과 같은 동작 수행
        }
    });
});

$.renderUserList = function (data) {
    let html = "";
    data.responseData.users.forEach(function (user) {
        let accountStatusDropdown = `
            <select id="statusDropdown_${user.id}" onchange="updateUserStatus(${user.id})">
                <option value="ACTIVE" ${user.status === 'ACTIVE' ? 'selected' : ''}>활성</option>
                <option value="DISABLED" ${user.status === 'DISABLED' ? 'selected' : ''}>비활성</option>
                <option value="DELETED" ${user.status === 'DELETED' ? 'selected' : ''}>탈퇴</option>
            </select>
        `;

        html += `
            <div>
                <p>ID : ${user.id}</p>
                <p>로그인 아이디 : ${user.loginId}</p>
                <p>이름 : ${user.name}</p>
                <p>성별 : ${user.gender}</p>
                <p>생년월일 : ${user.birthdate}</p>
                <p>이메일 : ${user.email}</p>
                <p>연락처 : ${user.phoneNumber}</p>
                <p>역할 : ${user.roleId.roleName}</p>
                <p>계정 상태 : ${accountStatusDropdown}</p>
                <p>계정 비활성화 시간 : ${user.disabledDate}</p>
                <p>계정 탈퇴처리 시간 : ${user.deletedDate}</p>
                <hr>
            </div>`;
    });
    $("#showUsers").html(html);
};

$.loadUserList = function () {
    const page = 1;

    $("#searchPageDiv").hide();  // 검색 결과 페이지네이션 숨기기
    $("#findName").val("");  // 입력 필드 값 삭제
    $("#pageDiv").show();  // 기존 페이지네이션 보이기

    $.ajax({
        url: `/api/admin/user/all-list`,
        method: "GET",
        data: {
            page: page,
        },
        dataType: 'json'
    }).done(function (data, status, xhr) {
        if (status === "success") {
            $.renderUserList(data);  // 사용자 목록 렌더링
            $.makePageUI(data.responseData.totalElements, page, "#pageDiv");   // 페이지네이션 UI 생성
        } else {
            $("#showUsers").html(`<p>오류: ${data.message}</p>`);
        }
    }).fail(function (jqXHR, textStatus, errorThrown) {
        console.error("Request failed: " + textStatus + ", " + errorThrown);
        $("#showUsers").html("<p>회원 정보를 불러오는 중 오류가 발생했습니다.</p>");
    });
}

$.makePageUI = function(paramTotal, paramPage, pageDivId, isSearch) {
    const rowsOnePage = 5;  // 페이지 당 항목 수 고정
    let totPage = Math.ceil(paramTotal / rowsOnePage);  // 전체 페이지 수
    let startPage = getStartPage(paramPage);
    let endPage = getEndPage(startPage, paramTotal);
    let prev = (paramPage - 1) < 1 ? 1 : paramPage - 1;
    let next = (paramPage + 1) >= totPage ? totPage : paramPage + 1;

    // 기존 내용 초기화
    $(pageDivId).html("");

    let paginationHtml = `
        <nav>
            <ul class="pagination justify-content-center">
                <li class="page-item ${paramPage === 1 ? 'disabled' : ''}">
                    <a class="page-link" href="#" onclick="$.searchBoardList(${prev}, '${pageDivId}', ${isSearch}); return false;">Prev</a>
                </li>`;

    if (paramTotal > 0) {
        for (let i = startPage; i <= endPage; i++) {
            paginationHtml += `
                <li class="page-item ${paramPage === i ? 'active' : ''}">
                    <a class="page-link" href="#" onclick="$.searchBoardList(${i}, '${pageDivId}', ${isSearch}); return false;">${i}</a>
                </li>`;
        }
    }

    paginationHtml += `
                <li class="page-item ${paramPage === totPage ? 'disabled' : ''}">
                    <a class="page-link" href="#" onclick="$.searchBoardList(${next}, '${pageDivId}', ${isSearch}); return false;">Next</a>
                </li>
            </ul>
        </nav>`;

    $(pageDivId).html(paginationHtml);
}

function getStartPage(page) {
    let one = 1;
    let ten = Math.floor((page - 1) / 10) * 10;
    return ten + one;
}

function getEndPage(startPage, paramTotal) {
    const rowsOnePage = 5;  // 페이지 당 항목 수 고정
    let totPage = Math.ceil(paramTotal / rowsOnePage);
    let tPage = startPage + 9;
    return tPage < totPage ? tPage : totPage;
}

$.searchBoardList = function(page, pageDivId, isSearch) {
    const rowsOnePage = 5;  // 페이지 당 항목 수 고정
    let url = isSearch
        ? `/api/admin/${$("#findName").val()}?page=${page}&size=${rowsOnePage}`  // 검색 시 URL 처리
        : `/api/admin/user/all-list?page=${page}&size=${rowsOnePage}`;  // 기본 페이지 처리

    $.ajax({
        url: url,
        method: "GET",
    }).done(function(data,status,xhr) {
        if (status === "success") {
            $.renderUserList(data);  // 사용자 목록 렌더링
            $.makePageUI(data.responseData.totalElements, page, pageDivId, isSearch); // 페이지 UI 갱신
        }
    }).fail(function(jqXHR, textStatus, errorThrown) {
        console.error("Request failed: " + textStatus + ", " + errorThrown);
        $("#showUsers").html("<p>회원 정보를 불러오는 중 오류가 발생했습니다.</p>");
    });
}

// 유효성 검사 함수
$.validateName = function(name) {
    if (name.trim() === "") {
        alert("이름을 입력해주세요.");
        return false;
    }
    if (name.length < 2 || name.length > 100) {
        alert("이름은 2자 이상 100자 이하로 입력해주세요.");
        return false;
    }
    return true;
};

$.showUser = function () {
    const name = $("#findName").val()
    const page = 1;  // 페이지 1부터 시작
    const rowsOnePage = 5;  // 페이지 당 항목 수 고정

    // 유효성 검사 함수 호출
    if (!$.validateName(name)) {
        return; // 유효성 검사 실패 시 종료
    }

    $("#pageDiv").hide();  // 기존 페이지네이션 숨기기
    $("#searchPageDiv").show();  // 검색 결과 페이지네이션 보기

    $.ajax({
        url: `/api/admin/${name}?page=${page}&size=${rowsOnePage}`,
        method: "GET",
    }).done(function (data, status, xhr) {
        if (status === "success") {
            $.renderUserList(data);  // 사용자 목록 렌더링
            $.makePageUI(data.responseData.totalElements, page, "#searchPageDiv", true);
        } else {
            $("#showUsers").html(`<p>오류: ${data.message}</p>`);
        }
    }).fail(function (jqXHR, textStatus, errorThrown) {
        console.error("Request failed: " + textStatus + ", " + errorThrown);
        if (jqXHR.status === 404) {
            $("#showUsers").html(`<p>검색하신 ${name} 라는 이름의 회원은 존재하지 않습니다.</p>`);
            $.makePageUI(0, 1,"#searchPageDiv");
        } else {
            $("#showUsers").html("<p>회원 정보를 불러오는 중 오류가 발생했습니다.</p>");
            $.makePageUI(0, 1,"#searchPageDiv");
        }
    });
}

function updateUserStatus(userId) {
    const selectedStatus = $(`#statusDropdown_${userId}`).val(); // 선택된 상태 값

    let apiUrl = "";
    let httpMethod = "";
    let confirmMessage = "";

    if (selectedStatus === "DISABLED") {
        apiUrl = `/api/admin/${userId}/disable`;
        httpMethod = "PATCH";
        confirmMessage = "계정을 비활성화하시겠습니까?";
    } else if (selectedStatus === "DELETED") {
        apiUrl = `/api/user/${userId}/delete`;
        httpMethod = "PATCH";
        confirmMessage = "계정을 삭제하시겠습니까?";
    } else if (selectedStatus === "ACTIVE") {
        apiUrl = `/api/admin/${userId}/undo`;
        httpMethod = "PATCH";
        confirmMessage = "계정을 활성화하시겠습니까?";
    }

    if (confirm(confirmMessage)) {
        $.ajax({
            url: apiUrl,
            type: httpMethod,
        })
            .done(function () {
                alert("상태가 성공적으로 변경되었습니다.");
                // 목록 갱신
                const pageDivId = $("#searchPageDiv").is(":visible") ? "#searchPageDiv" : "#pageDiv";
                const currentPage = parseInt($(pageDivId + " .active").text()) || 1;
                const isSearch = $("#searchPageDiv").is(":visible");
                $.searchBoardList(currentPage, pageDivId, isSearch);
            })
            .fail(function (jqXHR, textStatus, errorThrown) {
                console.error("요청 실패: " + textStatus + ", " + errorThrown);
                alert("상태 변경에 실패했습니다.");
            });
    } else {
        // 변경 취소 시 드롭다운 값을 원래 상태로 복구
        const originalStatus = $(`#statusDropdown_${userId} option[selected]`).val();
        $(`#statusDropdown_${userId}`).val(originalStatus);
    }
}
