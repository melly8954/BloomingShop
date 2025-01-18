function setActive(button) {
    // 모든 버튼에서 active 클래스 제거
    const buttons = document.querySelectorAll('.btn-group .btn');
    buttons.forEach((btn) => {
        btn.classList.remove('active');
    });

    // 클릭한 버튼에 active 클래스 추가
    button.classList.add('active');
}