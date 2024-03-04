package ezenweb.controller;

import ezenweb.model.dto.BoardDto;
import ezenweb.service.BoardService;
import ezenweb.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/board")   //공통 url
public class Boardcontroller {
    @Autowired
    BoardService boardService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private HttpServletRequest request;

    //1. 글쓰기 처리              /board/write.do            Post                 Dto
    @PostMapping("/write.do")
    @ResponseBody
    private long doPostBoardWrite(BoardDto boardDto){
        System.out.println("Boardcontroller.doPostBoardWrite");

        //1. 현재 로그인된 섹션 호출 (톰켓서버(자바프로그램) 메모리(jvm)저장소) 호출
        Object object= request.getSession().getAttribute("loginDto");
        if(object==null){
            return -2;
        }

        //2. 형변환
        String mid=(String) object;

        //3. mid를 mno
        long mno = memberService.doGetLoginInfo(mid).getNo();

        //4. 작성자 번호 대입
        boardDto.setMno(mno);

        return boardService.doPostBoardWrite(boardDto);
    }//m end

    //2. 전체 글 출력 호출         /board.do                  Get                  x, 페이징처리, 검색

    //3. 개별 글 출력 호출         /board/view.do             Get                  게시물번호
    @GetMapping("/view.do")
    @ResponseBody
    public BoardDto doGetBoardView(int bno){ //bno(변수명) 같으면 @RequestParam 생략 가능
        System.out.println("Boardcontroller.doGetBoardView");
        return boardService.doGetBoardView(bno);
    }//m end

    //4. 글 수정 처리             /board/update.do            Put                 Dto

    //5. 글 삭제 처리             /board/delete.do            delete              게시물번호

    //======================== 머스테치는 컨트롤에서 뷰 반환 ========================

    //1. 글쓰기 페이지 이동         /board/write               Get
    @GetMapping("/write")
    public String getBoardWrite(){
        return "ezenweb/board/write";
    }//m end

    //2. 게시판 페이지 이동         /board                     Get
    @GetMapping("/")    //가끔 배포할때 안잡히는 경우가 있어 "/"넣는게 좋음
    public String getBoard(){
        System.out.println("Boardcontroller.getBoard");
        return "ezenweb/board/board";
    }//m end

    //3. 게시판 상세 페이지 이동     /board/view                Get
    @GetMapping("/view")
    public String getBoardView(int bno){
        return "ezenweb/board/view";
    }

    //4. 글수정 페이지 이동         /board/update              Get
}//c end
