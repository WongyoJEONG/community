$(document).ready(function(){
    $("#categorySelect").change(function(){
        let selectedCategory = $(this).val();
        loadBoardList(selectedCategory);
    });
});

function loadBoardList(category) {
    $.ajax({
        url: '/board/list-ajax',
        type: 'GET',
        data: {category: category},
        success: function(fragment) {
            $("#boardListContainer").replaceWith(fragment);
        }
    });
}

function confirmDelete(boardNum) {
    Swal.fire({
        title: '게시글 삭제',
        text: "정말 이 글을 삭제하시겠습니까?",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#d33',
        cancelButtonColor: '#3085d6',
        confirmButtonText: '삭제',
        cancelButtonText: '취소'
    }).then((result) => {
        if (result.isConfirmed) {
            location.href = '/board/delete?boardNum=' + boardNum;
        }
    })
}

// 신청 확인
function confirmApply(form) {
    Swal.fire({
        title: '소모임 가입 신청',
        text: "이 모임에 참여 신청하시겠습니까?",
        icon: 'question',
        showCancelButton: true,
        confirmButtonColor: '#0d6efd',
        confirmButtonText: '신청하기',
        cancelButtonText: '취소'
    }).then((result) => {
        if (result.isConfirmed) {
            form.submit();
        }
    });
    return false; // 기본 제출 막기
}

// 거절 확인
function confirmReject(form) {
    Swal.fire({
        title: '신청 거절',
        text: "정말 이 신청을 거절하시겠습니까?",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#d33',
        confirmButtonText: '거절하기',
        cancelButtonText: '취소'
    }).then((result) => {
        if (result.isConfirmed) {
            form.submit();
        }
    });
    return false;
}

function checkWrite() {
    let title = document.getElementById("title");
    let contents = document.getElementById("contents");

    if (title.value.length == 0) {
        alert("제목을 입력하세요.");
        return false;
    }
    if (contents.value.length == 0) {
        alert("내용을 입력하세요.");
        return false;
    }
    return true;
}
    function formCheck(e) {
    // submit 이벤트를 일단 멈춤 (SweetAlert는 비동기라서)
    e.preventDefault();

    let id = document.getElementById("memberId");
    let pw = document.getElementById("memberPw");
    let name = document.getElementById("name");

    if (id.value.length < 3 || id.value.length > 10) {
    Swal.fire({
    icon: 'error',
    title: '아이디 확인',
    text: '아이디는 3~10자 사이여야 합니다!'
});
    return false;
}

    if (pw.value.length < 3 || pw.value.length > 10) {
    Swal.fire({
    icon: 'warning',
    title: '비밀번호 확인',
    text: '비밀번호는 3~10자 사이여야 합니다!'
});
    return false;
}

    if (name.value.length == 0) {
    Swal.fire('이름을 입력해주세요!');
    return false;
}

    Swal.fire({
    icon: 'success',
    title: '가입 완료!',
    text: '환영합니다. 로그인 페이지로 이동합니다.',
    showConfirmButton: false,
    timer: 1500
}).then(() => {
    // 실제 폼 전송
    document.getElementById("joinForm").submit();
});
}
