package ezenweb.controller;

import ezenweb.model.dto.BoardDto;
import ezenweb.model.dto.BoardPageDto;
import ezenweb.service.BoardService;
import ezenweb.service.FileService;
import ezenweb.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/board")   //공통 url
public class BoardController {
    @Autowired
    BoardService boardService;
    @Autowired
    FileService fileService;
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

    //2. 전체 글 출력 호출         /board/do                  Get                  x, 페이징처리, 검색
    @GetMapping("/do")  //(쿼리스트링)매개변수 : 현재페이지
    @ResponseBody
    public BoardPageDto doGetBoardViewList(@RequestParam int  page, 
                                           @RequestParam int pageBoardSize, 
                                           @RequestParam int bcno,
                                           @RequestParam("key") String field,
                                           @RequestParam("keyword") String value){
        System.out.println("BoardController.doGetBoardViewList");
        System.out.println("page = " + page);
        return boardService.doGetBoardViewList(page, pageBoardSize, bcno, field, value);
    }

    //3. 개별 글 출력 호출         /board/view.do             Get                  게시물번호
    @GetMapping("/view.do")
    @ResponseBody
    public BoardDto doGetBoardView(int bno){ //bno(변수명) 같으면 @RequestParam 생략 가능
        System.out.println("Boardcontroller.doGetBoardView");
        return boardService.doGetBoardView(bno);
    }//m end

    //4. 글 수정 처리             /board/update.do            Put                 Dto
    @PutMapping("/update.do")
    @ResponseBody
    public boolean doUpdateBoard(BoardDto boardDto){
        System.out.println("BoardController.doUpdateBoard");
        System.out.println("boardDto = " + boardDto);

        //유효성검사
            //1. 현재 로그인된 아이디 (세션)
        Object object= request.getSession().getAttribute("loginDto");
        if(object!=null) {
            String mid = (String) object;

            boolean result= boardService.boardWriterAuth(boardDto.getBno(), mid);  //해당 세션정보가 작성한 글인지 체크
            if(result){
                //2. 현재 수정할 게시물의 작성자 아이디 (DB)
                return boardService.doUpdateBoard(boardDto);
            }
        }
        return false;
    }

    //5. 글 삭제 처리             /board/delete.do            delete              게시물번호
    @DeleteMapping("/delete.do")
    @ResponseBody
    public boolean doDeleteBoard(@RequestParam int bno){
        System.out.println("BoardController.doDeleteBoard");

        //유효성검사
        //1. 현재 로그인된 아이디 (세션)
        Object object= request.getSession().getAttribute("loginDto");
        if(object!=null) {
            String mid = (String) object;

            boolean result= boardService.boardWriterAuth(bno, mid);  //해당 세션정보가 작성한 글인지 체크
            //2. 현재 수정할 게시물의 작성자 아이디 (DB)
            return boardService.doDeleteBoard(bno);
        }
        return false;
    }

    //6. 다운로드 처리(함수만들때 고민할점 1.매개변수:파일명 2.반환  3.사용처:get http 요청)
    @GetMapping("/file/download")
    @ResponseBody
    public void getBoardFileDownload(String bfile){
        System.out.println("BoardController.getBoardFileDownload");
        System.out.println("bfile = " + bfile);

        fileService.fileDownload(bfile);

        return;
    }

    //7. 댓글작성 (brcontent, brindex, mno, bno)
    @PostMapping("/reply/write.do")
    @ResponseBody
    public boolean postReplyWrite(@RequestParam Map<String, String> map){
        System.out.println("BoardController.getReplyWrite");

        //1. 현재 로그인된 섹션 호출 (톰켓서버(자바프로그램) 메모리(jvm)저장소) 호출
        Object object= request.getSession().getAttribute("loginDto");
        if(object==null){
            return false;   //세션없다/로그인안했다.
        }
        //2. 형변환
        String mid=(String) object;
        //3. mid를 mno
        long mno = memberService.doGetLoginInfo(mid).getNo();
        //4. map에 mno 넣기
        map.put("mno",mno+"");

        System.out.println("map = " + map);

        return boardService.postReplyWrite(map);
    }

    //8. 댓글출력 (brno, brcontent, brdate, brindex, mno), 매개변수 bno
    @GetMapping("/reply/do")
    @ResponseBody
    public List<Map<String , String>> getReplyDo(int bno){
        System.out.println("BoardController.getReplyDo");

        return boardService.getReplyDo(bno);
    }

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
    @GetMapping("/update")
    public String getBoardUpdate(){
        return "/ezenweb/board/update";
    }
}//c end
