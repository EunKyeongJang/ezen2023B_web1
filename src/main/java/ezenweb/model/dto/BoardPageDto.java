package ezenweb.model.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@ToString
@Builder
public class BoardPageDto {
    //부가 정보
    //1. 현재 페이지 :
    private int page;
    //2. 총 게시물 수
    private int totalPage;
    //3. 페이지 버튼의 시작번호
    private int startBtn;
    //4. 페이지 버튼의 끝번호
    private int endBtn;

    //실제 내용//
    private List<BoardDto> list;
}
