package ezenweb.model.dao;

import com.sun.jdi.event.ExceptionEvent;
import ezenweb.model.dto.ProductDto;
import org.springframework.stereotype.Component;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Component
public class ProductDao extends Dao{

    //1. 등록 서비스/처리 요청
    public boolean postProductRegister(ProductDto productDto){
        System.out.println("ProductDao.postProductRegister");
        System.out.println("productDto = " + productDto);
        try{
            //1. 제품등록
            String sql="insert into product (pname, pprice, pcontent, plat, plng, mno) values(?,?,?,?,?,?)";
            ps=conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS); //이미지 등록 시 제품번호 필요
            ps.setString(1, productDto.getPname());
            ps.setInt(2, productDto.getPprice());
            ps.setString(3, productDto.getPcontent());
            ps.setString(4, productDto.getPlat());
            ps.setString(5, productDto.getPlng());
            ps.setInt(6, productDto.getMno());
            int count=ps.executeUpdate();
            
            if(count==1){
                //2. 이미지 등록
                rs=ps.getGeneratedKeys();   //pk번호 호출
                if(rs.next()){
                    //등록할 이미지 개수만큼 sql 실행
                    productDto.getPimg().forEach((pimg)->{
                        try {
                            String subSql = "insert into productimg(pimg, pno) values(?,?)";
                            ps = conn.prepareStatement(subSql);
                            ps.setString(1, pimg);
                            ps.setInt(2, rs.getInt(1));
                            ps.executeUpdate();
                        }
                        catch (Exception e2){
                            System.out.println("e2 = " + e2);
                        }
                        
                    });//forEach end
                }//if2 end
                return true;
            }//if1 end
            
        }//try1 end
        catch (Exception e){
            System.out.println("e = " + e);
        }
        return false;
    }//m end

    //2. 제품출력(지도에 출력할) 요청
    public List<ProductDto> getProductList(){
        System.out.println("ProductDao.getProductList");

        List<ProductDto> list=new ArrayList<>();
        try{
            String sql="select*from product";
            ps=conn.prepareStatement(sql);
            rs=ps.executeQuery();
            while(rs.next()){
                //빌더패턴 : 클래스명.Builder().필드명(값).build()
                list.add(ProductDto.builder()
                        .pno(rs.getInt("pno"))
                        .pname(rs.getString("pname"))
                        .pprice(rs.getInt("pprice"))
                        .plat((rs.getString("plat")))
                        .plng(rs.getString("plng"))
                        .build());
            }//while end
            return list;
        }//try end
        catch (Exception e){
            System.out.println("e = " + e);
        }
        return null;
    }//m end

}
