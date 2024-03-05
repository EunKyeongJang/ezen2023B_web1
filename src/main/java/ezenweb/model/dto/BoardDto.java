package ezenweb.model.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@ToString
@Builder    //생성자의 단점을 보완한 라이브러리 함수 제공
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
    //+ 전체 출력 시 필드
    String mid;
    String mimg;
}

/*
    - 용도에 따라 다양한 dto 존재할 수 있다.
    - 하나의 dto에 서로다른 용도로 사용. 
    1. 글쓰기 용
        - 입력받기 : btitle, bcontent, bfile, bcno
        - 서버처리 : bno 자동 bview 기본값0  bdate기본값현재날짜  mno로그인(*세션)
    2. 개별 출력 용
        - 출력용 : bno  btitle  bcontent  bfile  bview  bdate  mno  bcno
    3. 전체 출력용 : bno  btitle  bcontent  bfile  bview  bdate  mno  bcno  mid  mimg

    - 생성자 단점/규칙 : 1.매개변수의 순서, 개수 => 유연성 떨어짐
    - 빌더 패턴 : @Builder
 */
