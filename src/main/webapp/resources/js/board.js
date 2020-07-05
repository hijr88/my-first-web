'use strict'
function notAllowEmpty(form) {
    const q = form.querySelector("#q");
    if(q.value === ''){
        showAlert('danger','검색어를 입력해주세요.',true);
        return false;
    }
    return true;
}

//게시판 작성페이지
function validateBoard(form){
    const title = form.title;
    const content = form.content;
    if(title.value.length === 0 ){
        alert('제목을 입력하세요.');
        return false;
    }
    if(title.value.length > 26 ){
        alert('제목의 글자수는 최대 26자입니다.');
        return false;
    }
    if(content.value.length === 0 ){
        alert('내용을 입력하세요.');
        return false;
    }
    /* if(stringByteLength > 300){
        alert('최대 입력값을 초과하였습니다.');
        return false;
    } */
    return true;
}
function checkMaxByte(text){ //textArea
    let len = checkByte(text.value); //textarea 바이트 계산
    document.querySelector(".currentByte").textContent = len.b; //현재 바이트 업데이트
    if(len.b > 4000){
        text.value= text.value.substring(0,len.i -1);
        len = checkByte(text.value);
        document.querySelector(".currentByte").textContent = len.b
        //alert('최대 입력값을 초과하였습니다.');
        showAlert('danger','최대 입력값을 초과하였습니다.',true)
    }
}
function checkByte(s,b,i,c){//string, byte, index, character
    for(b=i=0;c=s.charCodeAt(i++);){
        b+=c>>11?3:c>>7?2:1; //2048로나눴을때 몫이 있으면 3바이트 다시 128이랑비교해서 몫이 있으면 2바이트 없으면1바이트
        if(b > 4000){
            return{
                b:b,  //바이트
                i:i   //글자수
            };
        }
    }
    return{
        b:b
    };
}
function changeFile(thisFile) { //파일버튼
    const file = thisFile.files[0];//fileList[0]; 첫번째파일객체
    const fileName = document.querySelector(".custom-file-label"); //화면상에 보이는 파일이름

    if (thisFile.value.length === 0) {  // 파일이름
        deleteFile();
        return;
    }
    if (file.size > 10485760) {  //10485760 10mb;
        //alert('파일 사이즈는 최대 10mb입니다.');
        showAlert('danger','파일 사이즈는 최대 10mb입니다.',true);
        deleteFile();
        return;
    }
    fileName.textContent = file.name; //파일이름 변경

    if(document.querySelector("#isDelete") != null)
        document.querySelector("#isDelete").value = 'false';
}

function cancel() {
    if (confirm("변경사항이 저장되지 않을 수 있습니다."))
        history.go(-1);
}
//게시판 작성페이지



function deleteBoard(form) { //글번호
    if(confirm('정말 삭제하시겠습니까?')){
        form.submit();
    }
}

//수정페이지
function deleteFile(){
    if(document.querySelector("#isDelete") != null)
        document.querySelector("#isDelete").value = 'true';
    document.querySelector(".custom-file-label").textContent = '파일을 선택해 주세요.';//화면상에 보이는 파일이름
    document.querySelector("#customFile").value = '';
}
