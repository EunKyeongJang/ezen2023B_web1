//썸머노트 실행
    //$ : jquery문법
    //js : window.onload = function(){}  vs  jQuery : $(document).ready(function() {}
    //js : document.querySelector('#summernote')  vs  jQuery : $('#summernote')


let bno=new URL(location.href).searchParams.get('bno');
onView();

function onView(){
    $.ajax({
        url : "/board/view.do",
        method : "Get",
        data : {"bno" : bno},
        success : (r) => {

            document.querySelector('.btitle').value=r.btitle;
            document.querySelector('.bcontent').innerHTML=r.bcontent;
            document.querySelector('.bcno').value = r.bcno;
            document.querySelector('.bfile').innerHTML = r.bfile;
            //썸머노트 옵션
             let option={
                 lang : "ko-KR", //한글패치
                 height : 500   // 에디터 세로 크기
             };
             $('#summernote').summernote(option);    //썸머노트 실행
        }//success end
    })//ajax end
}//f end

//2. 게시물 수정
function onUpdate(){
    console.log("onUpdate()-js")

    //1. 폼 가져온다
    let boardUpdateForm=document.querySelector(".boardUpdateForm");
    //2. 폼 객체화(첨부파일 바이트화)
    let boardUpdateFormData=new FormData(boardUpdateForm);

        //+폼 객체 에 데이터 추가 [html 입력 폼 외에 데이터 삽입 가능]
        //폼데이터객체명.set(속성명(name), 데이터(value));
        boardUpdateFormData.set("bno" , bno);

    //멀티파트 폼전송
    $.ajax({
        url : "/board/update.do",
        method : "Put",
        data : boardUpdateFormData,
        contentType : false,
        processData : false,
        success : (r) => {
            if(r){
                alert("수정성공");
                location.href="/board/view?bno="+bno;
            }
            else{
                alert("수정실패");
            }
        }//success end
    })//ajax end
}//f end
