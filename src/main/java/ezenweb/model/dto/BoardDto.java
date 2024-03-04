package ezenweb.model.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@ToString
public class BoardDto {
    long bno;           //번호
    String btitle;      //제목
    String bcontent;    //내용
    String bfile;       //첨부파일 [이름 출력용]
    long bview;         //조회수
    String bdate;       //작성일
    long mno;           //작성자
    long bcno;          //카테고리

    MultipartFile uploadfile;    //실제 첨부파일 [db처리x, 서버에 저장]
}

/*
    글쓰기 용
        - 입력받기 : btitle, bcontent, bfile, bcno
        - 서버처리 : bno 자동 bview 기본값0  bdate기본값현재날짜  mno로그인(*세션)
    글출력 용
 */
