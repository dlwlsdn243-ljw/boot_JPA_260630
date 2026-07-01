console.log("test")

// 수정버튼을 클릭하면 title, content만 readOnly 풀기
document.getElementById("modBtn").addEventListener("click", () => {
    document.getElementById("t").readOnly = false
    document.getElementById("c").readOnly = false
    // Form태그의 submit역할을 할 수 있는 버튼 생성
    // 수정, 삭제버튼을 지우고, update 버튼 생성

    // update 버튼 만들기
    let regBtn = document.createElement("button");
    regBtn.setAttribute('type', 'submit')
    regBtn.classList.add('btn', 'btn-success')
    regBtn.innerText = 'upatet'

    // from 태그의 가장 마지막 요소로 추가
    document.getElementById('modForm').appendChild(regBtn);

    // 수정, 삭제 버튼 지우기
    document.getElementById('modBtn').remove();
    document.getElementById('delBtn').remove();
    document.getElementById('listBtn').remove();
})