package ezenweb.service;

import ezenweb.model.dao.ProductDao;
import ezenweb.model.dto.ProductDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductDao productDao;
    @Autowired
    FileService fileService;

    //1. 등록 서비스/처리 요청
    public boolean postProductRegister(ProductDto productDto){
        System.out.println("ProductService.postProductRegister");
        //- 1. 첨부파일 업로드 처리
        List<String> list=new ArrayList<>();
        productDto.getUploadFiles().forEach((uploadFile)->{
            String fileName=fileService .fileUpload(uploadFile);
            list.add(fileName);
        });
        productDto.setPimg(list);
        return productDao.postProductRegister(productDto);
    }//m end

    //2. 제품출력(지도에 출력할) 요청
    public List<ProductDto> getProductList(){
        System.out.println("ProductService.getProductList");

        return productDao.getProductList();
    }//m end

    //3. 해당 제품의 찜하기 등록  //언제실행:로그인했고 찜하기 버튼 클릭시 , 매개변수:pno , 리턴:boolean(등록 성공/실패)
    public boolean getPlikeWrite(int pno, int mno){
        System.out.println("ProductService.getPlikeWrite");
        System.out.println("pno = " + pno);
        return productDao.getPlikeWrite(pno, mno);
    }//m end

    //4. 해당 제품의 찜하기 상태 등록   //언제실행:로그인했고 찜하기 버튼 출력시 , 매개변수:pno , 리턴:boolean(등록 있다/없다)
    public boolean getPlikeView(int pno, int mno){
        System.out.println("ProductService.getPlikeView");
        System.out.println("pno = " + pno);
        return productDao.getPlikeView(pno, mno);
    }//m end

    //5. 해당 제품의 찜하기 취소/삭제   //언제실행:로그인했고 찜하기 버튼 클릭시 , 매개변수:pno , 리턴:boolean(등록 취소 성공/실패)
    public boolean getPlikeDelete(int pno, int mno){
        System.out.println("ProductService.getPlikeDelete");
        System.out.println("pno = " + pno);
        return productDao.getPlikeDelete(pno, mno);
    }
}//c end
