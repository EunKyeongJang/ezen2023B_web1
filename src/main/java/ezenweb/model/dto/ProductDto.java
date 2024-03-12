package ezenweb.model.dto;
import lombok.*;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@ToString
@Builder    //빌더패턴
public class ProductDto {
    private int pno;
    private String  pname;
    private int pprice;
    private String pcontent;
    private byte pstate;
    private String pdate;
    private String plat;
    private String plng;
    private  int mno;

    //- 등록할때 이미지
    private List<MultipartFile> uploadFiles;
    //- 출력할때 이미지
    private List<String> pimg; //= new ArrayList<>(); //**new 넣는거 주의! service에 만들어주면 메모리가 좀 더 효율적임
    //- 출력 시 작성자 번호가 아닌 작성자 아이디
    private String mid;

    //+1. 제품등록 [pname, pprice, pcontent, plat, plng, mno(세션)]
    //+2. 제품출력 [pno, pname, pprice, pstate, plat, plng]
    //+3. 제품 지도에서 마커 클릭 시 상세출력 [pno, pname, pprice, pcontent, pstate, pdate, plat, plng, mno]
}
