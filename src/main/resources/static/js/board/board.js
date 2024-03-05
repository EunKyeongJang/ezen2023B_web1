//1. 전체출력용 : 함수 : 매개변수 x, 반환, 언제 실행할건지 : 페이지 열릴때(js)(ex. 회원가입 버튼 클릭할때)
doViewList(1);  //첫페이지 호출
function doViewList(page){
    console.log("doViewList()");

    $.ajax({
        url : '/board/do',
        method : 'get',
        data : {'page' : page },
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
                              <td>${board.btitle}</td>
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

        }//success end
    }); //ajax end

    return;
}//f end

