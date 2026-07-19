console.log("boardRegister.js in");

document.getElementById('trigger').addEventListener('click',()=>{
    document.getElementById('file').click();
});

const regExp = new RegExp("\.(exe|sh|bat|dll|jar|msi)$");
const maxSize = 1024*1024*10; // 10MB

// 실행파일 막기, 10MB 이상 사이즈 제한
function fileVaild(fileName, fileSize){
    if(regExp.test(fileName)){
        return 0;
    } else if(fileSize > maxSize){
        return 0;
    }else{
        return 1;
    }
}

document.getElementById('file').addEventListener('change', (e)=>{
    const fileObject = e.target.files;
    console.log(fileObject);   // FileList[]

    const div = document.getElementById('fileZone');
    div.innerHTML = ''; // 이전 등록된 값이 있다면 지우기.
    // disabled 속성이 풀어주지 않으면 스스로 풀어지지 않음.
    document.getElementById('regBtn').disabled = false;

    let ul = `<ul class="list-group list-group-flush">`;
    let isOk = 1;  // vaild 통과 했는지 확인용  * 로 확인할 부분이라
    for(let file of fileObject){
        let vaild = fileVaild(file.name, file.size);
        isOk *= vaild;
        ul+=`<li class="list-group-item">`;
        ul+=`<div class="mb-3">`;
        ul+=`${vaild ? '<div class="fw-bold mb-2">업로드 가능</div>' :
            '<div class="fw-bold mb-2 text-danger">업로드 불가능</div>'}`;
        ul+=`${file.name} `;
        ul+=`<span class="badge rounded-pill text-bg-${vaild ? 'success' : 'danger'}">${file.size}Byte</span>`;
        ul+=`</div></li>`;
    }
    ul += `</ul>`;
    div.innerHTML = ul;

    console.log(isOk);
    if(isOk == 0){
        // 파일 중 하나라도 검증을 통과 못했다면
        document.getElementById('regBtn').disabled = true;
    }

});

/*
        <ul class="list-group list-group-flush">
            <li class="list-group-item">
                <div class="mb-3">
                    <div class="fw-bold mb-2">업로드 가능 여부</div>
                    fileName
                    <span class="badge rounded-pill text-bg-primary">fileSize</span>
                </div>
            </li>
        </ul>
* */