package ezenweb.service;

import example.day04._1리스트컬렉션.Board;
import ezenweb.model.dao.BoardDao;
import ezenweb.model.dto.BoardDto;
import ezenweb.model.dto.BoardPageDto;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Service
public class BoardService {
    @Autowired
    BoardDao boardDao;
    @Autowired
    FileService fileService;

    //1. 글쓰기 처리
    public long doPostBoardWrite(BoardDto boardDto){
        System.out.println("Boardcontroller.doPostBoardWrite");

        //1. 첨부파일 처리
            //첨부파일이 존재하면
        if(!boardDto.getUploadfile().isEmpty()){//첨부파일이 존재하면
            String fileName=fileService.fileUpload(boardDto.getUploadfile());
            if(fileName!=null){ //업로드 성공했으면
                boardDto.setBfile(fileName);    //db저장할 첨부파일명 대입
            }
            else{   //업로드에 문제가 발생하면 글쓰기 취소
                return -1;
            }
        }//if end

        //2. DB처리
        return boardDao.doPostBoardWrite(boardDto);
    }//m end

    //2. 전체 글 출력 호출
    public BoardPageDto doGetBoardViewList(int page, int pageBoardSize, int bcno, String field, String value){
        System.out.println("BoardService.doGetBoardViewList");
        //페이지 처리 시 사용할 sql 구문 : limit 시작레코드번호(0부터), 개수

        //1. 페이지당 게시물을 출력할 개수               [출력 개수]
        //int pageBoardSize=1;

        //2. 페이지당 게시물을 출력할 시작 레코드 번호    [시작레코드번호(0부터)]
        int startRow=(page-1)*pageBoardSize;

        //3. 총 페이지수
            //1. 전체 게시물 수
        int totalBoardSize=boardDao.getBoardSize(bcno, field, value);
            //2. 총 페이지 수 계산 (나머지 값이 존재하면 +1)
        int totalPage=totalBoardSize % pageBoardSize ==0 ?
                        totalBoardSize / pageBoardSize :
                        totalBoardSize/pageBoardSize + 1;

        //4. 게시물 정보 요청
        List<BoardDto> list=boardDao.doGetBoardViewList(startRow, pageBoardSize, bcno, field, value);

        //5. 페이징 버튼 개수
            //1. 페이지 버튼 최대 개수
        int btnSize=5;      //5개씩
            //2. 페이지버튼 시작번호
        int startBtn=((page-1)/btnSize)*btnSize+1;
        System.out.println("startBtn = " + startBtn);
            //3. 페이지버튼 끝번호
        int endBtn=startBtn+btnSize-1;
        System.out.println("endBtn = " + endBtn);
            //만약에 페이지버튼의 끝번호가 총 페이지수 보다는 커질 수 없으므로
        if(endBtn >= totalPage) endBtn=totalPage;

        //pageDto 구성 * 빌더패턴 : 생성자의 단점 (매개변수에 따른 유연성부족) 을 보완
            //new 연산자 없이 builder() 함수 이용한 객체 생성 라이브러리 제공
            //사용방법 : 클래스명.builder().필드명(대입값).필드명(대입값)/build();
            //+ 생성자보단 유연성 : 매개변수의 순서와 개수 자유롭다.
                //빌더패턴 vs 생성자 vs setter
        BoardPageDto boardPageDto=BoardPageDto.builder()
                .page(page)
                .totalPage(totalPage)
                .list(list)
                .startBtn(startBtn)
                .endBtn(endBtn)
                .totalBoardSize(totalBoardSize)
                .build();
        //============== vs ================
        //BoardPageDto boardPageDto = new BoardPageDto(page, totalPage, startBtn, endBtn, list);

        return boardPageDto;
    }

    //3. 개별 글 출력 호출
    public BoardDto doGetBoardView(int bno){ //bno(변수명) 같으면 @RequestParam 생략 가능
        System.out.println("Boardcontroller.doGetBoardView");

        //조회수 처리 // 조회수 많으면 수익 : 조회수 처리 했다 / 안했다 증거 남겨서 하루에 한번 또는 회원마다 한번(log, 세션)
        boardDao.boardViewIncrease(bno);

        return boardDao.doGetBoardView(bno);
    }//m end

    //4. 글 수정 처리

    //5. 글 삭제 처리

}//c end
