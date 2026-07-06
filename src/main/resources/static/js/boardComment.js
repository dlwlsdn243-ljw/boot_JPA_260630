console.log("boardComment.js in");

// cmtAddBtn 버튼을 클릭하면 입력한 댓글과 작성자, bno 값을 controller 전송
document.getElementById('cmtAddBtn').addEventListener('click',()=>{
    const cmtText = document.getElementById('cmtText');
    const cmtWrtier = document.getElementById('cmtWriter');

    if(cmtText == null || cmtText.value.trim() == ''){
        alert('댓글 입력요망!');
        cmtText.focus();
        return false;
    }
    let cmtData = {
        bno: bno,
        writer: cmtWrtier.innerText,
        content: cmtText.value
    }
    console.log(cmtData);
    registerCommentToServer(cmtData).then(result => {
        if(result == '1'){
            alert("댓글 등록 성공!");
            // 댓글 입력창을 비우고, 포커스 맞추기
            cmtText.value='';
            cmtText.focus();

            // 댓글 등록 성공 후 목록 다시 조회
            spreadCommentList(bno);

        }else{
            alert("댓글 등록 실패!");
            cmtText.focus();
        }
    });
})

// list를 화면에 출력하는 함수
function spreadCommentList(bno, page=1) {
    commentListFromServer(bno, page).then(result =>{
        console.log(result);
        const ul = document.getElementById('cmtListArea');
        if(result.content.length > 0){
            // 댓글이 있는 경우
            if (page == 1){
                ul.innerHTML = ''
            }
            let li = '';
            for(let comment of result.content){
                li += `<li class="list-group-item" data-cno=${comment.cno}>`;
                li += `<div class="ms-2 me-auto">`;
                li += `<div class="fw-bold">${comment.writer}</div>`;
                li += `${comment.content}`;
                li += `</div>`;
                li += `<span class="badge rounded-pill text-bg-primary">${comment.regDate}</span>`;
                li += `<button type="button" class="btn btn-outline-warning btn-sm mod" data-bs-toggle="modal" data-bs-target="#commentModal">수정</button>`;
                li += `<button type="button" class="btn btn-outline-danger btn-sm del">삭제</button>`;
                li += `</li>`;
            }
            ul.innerHTML += li;

            // page 처리 => moreBtn data-page = +1
            const moreBtn = document.getElementById("moreBtn");
            // 아직 리스트가 더 있다면  버튼 표시
            if (page < result.totalPages){
                moreBtn.style.visibility = "visible"; // 표시
                moreBtn.dataset.page = page + 1;
            }else {
                moreBtn.style.visibility = "hidden";
            }

        }else{
            // 댓글이 없는 경우
            ul.innerHTML = `<li class="list-group-item shadow-sm rounded-2">등록된 댓글이 없습니다.</li>`;
        }
    });
}

document.addEventListener("click", (e)=>{
    if (e.target.id == "moreBtn"){
        // 더보기 버튼을 클릭했을 때 => 남아있는 게시글 5개를 더 출력 => 비동기 호출
        spreadCommentList(bno,parseInt(e.target.dataset.page));
    }

    if (e.target.classList.contains("del")){
        // 삭제 버튼 인지
        // cno 값 추출 -> closest(내가 속한 내 부모값 찾기)
        let li = e.target.closest('li');
        let cno = li.dataset.cno;
        commentRemoveToServer(cno).then(result =>{
            if (result == "1"){
                alert("삭제성공!!")
                spreadCommentList(bno)
            }
        })

    }
})

// 전송 async 데이터 보내기
async function registerCommentToServer(cmtData){
    try {
        const url = "/comment/post";
        const config = {
            method:'post',
            headers : {
                'content-type': 'application/json; charset=utf-8'
            },
            body: JSON.stringify(cmtData)
        };

        const response = await  fetch(url, config);
        const result = await response.text();
        return result;
    } catch (e) {
        console.log(e);
    }
}

// list 요청
async function commentListFromServer(bno, page){
    try {
        const response = await fetch(`/comment/list/${bno}/${page}`);
        const result = await response.json();
        return result;
    } catch (e){
        console.log(e);
    }
}

// remove
async function commentRemoveToServer(cno){
    try {
        // fetch(url, config)
        const response = await fetch("/comment/remove/"+cno, {method:"delete"});
        const result = await response.text();
        return result;
    } catch (e){
        console.log(e);
    }
}