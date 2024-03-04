package ezenweb.service;

import example.day04._1리스트컬렉션.Board;
import ezenweb.model.dao.BoardDao;
import ezenweb.model.dto.BoardDto;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    //3. 개별 글 출력 호출
    public BoardDto doGetBoardView(int bno){ //bno(변수명) 같으면 @RequestParam 생략 가능
        System.out.println("Boardcontroller.doGetBoardView");
        return boardDao.doGetBoardView(bno);
    }//m end

    //4. 글 수정 처리

    //5. 글 삭제 처리

}//c end
