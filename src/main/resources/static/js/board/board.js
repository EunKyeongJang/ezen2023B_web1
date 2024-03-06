//============ 페이지 정보 관련 객체 = 여러개의 변수를 묶음 =============
let pageObject={
    page : 1,           //현재페이지
    pageBoardSize : 5,  //페이지당 표시 할 개수
    bcno : 0,            //현재 카테고리
    key : 'b.btitle',   //현재검색 key
    keyword : ''        //현재검색 keyword
}
window.onpageshow=function(event){
    if(event.persisted){
        location.reload();
    }
}

//1. 전체출력용 : 함수 : 매개변수 x, 반환, 언제 실행할건지 : 페이지 열릴때(js)(ex. 회원가입 버튼 클릭할때)
doViewList(1);  //첫페이지 호출
function doViewList(page){
    console.log("doViewList()");

    pageObject.page=page;   //매개변수로 들어온 페이지를 현재페이지에 대입

    $.ajax({
        url : '/board/do',
        method : 'get',
        data : pageObject,
        success : (r)=>{
            console.log(r);
            //테이블에 레코드 구성
            //1. 어디에
            let boardTableBody=document.querySelector("#boardTableBody");
            //2. 무엇을
            let html="";
                //서버가 보내준 데이터를 출력
                //1.
                r.list.forEach(board => {
                    console.log(board);
                    html+=`<tr>
                              <th scope="row">${board.bno}</th>
                              <td><a href="/board/view?bno=${board.bno}">${board.btitle}<a></td>
                              <td><img src="/img/${board.mimg}" style="width : 23px; border-radius : 50%"/> ${board.mid} </td>
                              <td>${board.bdate}</td>
                              <td>${board.bview}</td>
                          </tr>`;
                })
                //2.
                for(let i=0;i<r.length; i++){
                    console.log(r[i]);
                }
            //3. 출력
            boardTableBody.innerHTML=html;

            // 페이지네이션 =========================================================

            //1. 어디에
            let pagination=document.querySelector(".pagination");
            //2. 무엇을
            let pagehtml=``;
                //이전버튼 (만약에 현재페이지가 1페이지이면 1페이지고정)
                pagehtml+=`<li class="page-item">
                                <a class="page-link" onclick="doViewList( ${page-1 < 1 ? 1 : page-1} )">이전</a>
                            </li>`;
                //페이지번호 버튼(1페이지부터 마지막페이지까지)
                for(let i=r.startBtn; i<=r.endBtn; i++){
                    //만약에 i가 현재페이지와 같으면 active 아니면 생략 (*조건부 렌더링)
                    pagehtml+=`<li class="page-item ${i==page ? 'active' : ''}"><a class="page-link" onclick="doViewList(${i})">${i}</a></li>`;
                }

                //다음버튼 (만약에 현재페이지가 마지막 페이지이면 현재 페이지 고정
                pagehtml+=`<li class="page-item">
                               <a class="page-link" onclick="doViewList( ${page+1 > r.totalPage ? r.totalPage : page+1} )">다음</a>
                           </li>`;

            //3. 출력
            pagination.innerHTML=pagehtml;

            //====== 부가출력 =========
            document.querySelector('.totalPage').innerHTML=r.totalPage;
            document.querySelector('.totalBoardSize').innerHTML=r.totalBoardSize;

        }//success end
    }); //ajax end

    return;
}//f end

//2. 페이지당 게시물 수
function onPageBoardSize(object){
    console.log(object);
    console.log(object.value);

    pageObject.pageBoardSize=object.value;
    doViewList(1);
}//f end

//3. 카테고리 변경 함수
function onBcno(bcno){
    //bcno : 카테고리 식별번호 [0: 없다,전체, 1~: 식별번호(pk)]
    pageObject.bcno=bcno;
    //검색 제거 (검색이 없다는 기준 데이터)

    pageObject.key='b.title';
    pageObject.keyword='';
    document.querySelector(".key").value='b.title';
    document.querySelector(".keyword").value='';

    //카테고리 활성화 css 적용 ( 해당 버튼에 categoryActive 클래스 대입)
    //1. 모든 카테고리 버튼 호출
    let categoryBtns=document.querySelectorAll(".boardCategoryBox > button");
    console.log(categoryBtns);
    //2. 선택된 카테고리 번호(매개변수bno) 에 class 대입
        //dom객체.classList.add("새로운 클래스명")
        //dom객체.classList.remove("삭제할 클래스명")

    //1. 활성화 초기화
    for(let i=0; i<categoryBtns.length; i++){
        categoryBtns[i].classList.remove("categoryActive")
    }
    //2. 활성화 대입
    categoryBtns[bcno].classList.add("categoryActive");

    doViewList(1);
}

//4. 검색함수
function onSearch(){
//1.입력받은 값 가져오기
    let key=document.querySelector('.key').value;
    let keyword=document.querySelector('.keyword').value;
//2. 서버에 전송할 객체에 담아주고
    pageObject.key=key;
    pageObject.keyword=keyword;
//3. 출력함수 호출
    doViewList(1);
}

