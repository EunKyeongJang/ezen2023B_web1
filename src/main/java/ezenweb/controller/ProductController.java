package ezenweb.controller;

import ezenweb.model.dao.ProductDao;
import ezenweb.model.dto.ProductDto;
import ezenweb.service.MemberService;
import ezenweb.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    MemberService memberService;

    //1. 등록 서비스/처리 요청
    @PostMapping("/register.do")
    @ResponseBody
    public boolean postProductRegister(ProductDto productDto){
        System.out.println("ProductController.postProductRegister");
        //- 1. 작성자 처리
        Object object=request.getSession().getAttribute("loginDto");
        if(object==null){
            return false;
        }
        String mid=(String)object;
        productDto.setMno(memberService.doGetLoginInfo(mid).getNo());

        return productService.postProductRegister(productDto);
    }//m end

    //2. 제품출력(지도에 출력할) 요청
    @GetMapping("/list.do")
    @ResponseBody
    public List<ProductDto> getProductList(){
        System.out.println("ProductController.getProductList");

        return productService.getProductList();
    }//m end
    
    //3. 해당 제품의 찜하기 등록  //언제실행:로그인했고 찜하기 버튼 클릭시 , 매개변수:pno , 리턴:boolean(등록 성공/실패)
    @PostMapping("/plike.do")
    @ResponseBody
    public boolean getPlikeWrite(int pno){
        System.out.println("ProductController.getPlikeWrite");
        System.out.println("pno = " + pno);

        Object object=request.getSession().getAttribute("loginDto");
        if(object==null){
            return false;
        }
        int mno=memberService.doGetLoginInfo((String) object).getNo();

        return productService.getPlikeWrite(pno, mno);
    }
    
    //4. 해당 제품의 찜하기 상태 등록   //언제실행:로그인했고 찜하기 버튼 출력시 , 매개변수:pno , 리턴:boolean(등록 있다/없다)
    @GetMapping("/plike.do")
    @ResponseBody
    public boolean getPlikeView(int pno){
        System.out.println("ProductController.getPlikeView");
        System.out.println("pno = " + pno);

        Object object=request.getSession().getAttribute("loginDto");
        if(object==null){
            return false;
        }
        int mno=memberService.doGetLoginInfo((String) object).getNo();

        return productService.getPlikeView(pno, mno);
    }
    
    //5. 해당 제품의 찜하기 취소/삭제   //언제실행:로그인했고 찜하기 버튼 클릭시 , 매개변수:pno , 리턴:boolean(등록 취소 성공/실패)
    @DeleteMapping("/plike.do")
    @ResponseBody
    public boolean getPlikeDelete(int pno){
        System.out.println("ProductController.getPlikeDelete");
        System.out.println("pno = " + pno);

        Object object=request.getSession().getAttribute("loginDto");
        if(object==null){
            return false;
        }
        int mno=memberService.doGetLoginInfo((String) object).getNo();

        return productService.getPlikeDelete(pno, mno);
    }

    //1. 등록 페이지/화면/뷰 요청
    @GetMapping("/register")
    public String productRegister(){
        return "ezenweb/product/register";
    }//m end

    //2. 제품 지도 페이지/화면/뷰 요청
    @GetMapping("/list")
    public String productList(){
        return "ezenweb/product/list";
    }//m end

}//c end
