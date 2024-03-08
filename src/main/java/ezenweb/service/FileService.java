package ezenweb.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.URLEncoder;
import java.util.UUID;

@Service // 해당 클래스를 스프링 컨터이너(저장소)에 빈(객체) 등록
public class FileService {
    // Controller : 중계자 역할 ( HTTP매핑 , HTTP요청/응답 , 데이터 유효성검사 )등등
    // Service : Controller <--  Service(비지니로직) --> Dao ,  Controller <--> Service(비지니로직)
    
    @Autowired
    private HttpServletRequest request; //HTTP로 요청을 보낸 정보가 담긱 객체 (매개변수와 브라우저 정보 -> 세션)
    @Autowired
    private HttpServletResponse response;   //HTTP로 응답을 보낼 정보와 기능/메소드 가지고 있는 객체

    // 어디에(PATH) 누구를(파일객체 MultipartFile )
    String uploadPath = "C:\\Users\\504\\Desktop\\ezen2023B_web1-master\\build\\resources\\main\\static\\img\\";

    // 1. 업로드 서비스 메소드
    public String fileUpload( MultipartFile multipartFile){
        // * 파일 이름 조합하기 : 새로운 식별이름과 실제 파일 이름
        String uuid = UUID.randomUUID().toString();
        String filename  = uuid+"_"+multipartFile.getOriginalFilename().replaceAll("_","-");
        // 1. [어디에] 첨부파일을 저장할 경로
        // File 클래스 : 파일 관련된 메소드 제공.
        File file = new File( uploadPath+filename );
        // 2. [무엇을] 첨부파일 객체
        // .transferTo( 경로 )
        try { multipartFile.transferTo( file );}
        catch ( Exception e ){
            System.out.println("e = " + e);
            return null;
        }
        return filename; // 반환 : 어떤 이름으로 업로드 했는지 식별명 반환해서
    }
    // 2. 다운로드 메소드
    public void fileDownload(String bfile){
        System.out.println("FileService.fileDownload");
        System.out.println("bfile = " + bfile);
        
        //1. 다운로드 할 파일의 경로와 파일명 연결해서 해당파일 찾기
        String downloadPath=uploadPath+bfile;
        System.out.println("downloadPath = " + downloadPath);
        
        //2. 해당 파일을 객체로 가져오기 [File 클래스]
        File file=new File(downloadPath);
        System.out.println("file = " + file);

        //3. exists() : 해당경로에 파일이 있다/없다 판단
        if(file.exists()){
            System.out.println("exists");

            //HTTP가 파일을 전송하는 방법 : 파일을 바이트로 전송
            try {   //혹시나 파일이 없을 수 있으므로 예외처리 필수

                // HTTP로 응답 시 응답방법(다운로드 모양)에 대한 정보(HTTP header)를 추가
                //url은 한글지원 안한다.
                //url에 한글 지원 하기 위해서는 URLEncoder.encode(url정보, "utf-8")
                //첨부파일 다운로드 형식 : 브라우저마다 형식이 다르다.(커스텀 불가능)
                response.setHeader("Content-Disposition", "attachment;filename="
                        + URLEncoder.encode(bfile.split("_")[1],"utf-8"));

                //1. 해당파일을 바이트로 불러온다.                           [BufferedInputStream]
                    //1-1 파일 입력스트림(바이트가 다니는 통로)객체 생성
                BufferedInputStream fin = new BufferedInputStream(new FileInputStream(file));
                    //1-2 바이트 배열(고정길이) vs 리스트(가변길이)
                        //1. 파일의 사이즈/크기/용량 (파일의 크기만큼 바이트 배열 선언하기 위해)
                long fileSize=file.length();
                        //2. 해당 파일의 사이즈 만큼 바이트 배열 선언
                byte[] bytes=new byte[(int)fileSize];    //배열의 길이는 int형
                    //1-3 입력(불러오기) 메소드
                        //바이트 하나씩 읽어오면서바이트 배열 저장 => 바이트 배열 필요하다.
                fin.read(bytes);    //입력스트림객체.read(바이트) 하나씩 바이트를 읽어와서 해당 바이트 배열에 저장 해 주는 함구
                
                    //1-4(확인용) 읽어온 파일의 바이트가 들어있다
                System.out.println("bytes = " + bytes);
                
                //2. 불러온 바이트를 HTTP response 이용한 바이트로 응답한다.  [BufferedOutputStream]
                    //2-1 HTTP 응답스트림 객체 생성
                BufferedOutputStream fout = new BufferedOutputStream(response.getOutputStream());
                    //2-2 응답스트림.write (내보내기 할 바이트 배열) : 내보내기 할 바이트 배열 준비상태이면 내보내기
                fout.write(bytes);

                //======== 버퍼 초기화(안전하게) =========
                fin.close();    //스트림 닫기
                fout.close();   //스트림 닫기
                    //JVM이 함수 종료 시 지워주기는 하지만 안전하게 하기 위해 메모리 초기화 작업을 추가했음

            }//try end
            catch (Exception e){
                System.out.println("e = " + e);
            }
            
        }//if end
        else{
            System.out.println("undefined");
        }//else end

        return;
    }//m end

    //3. 파일삭제 [게시물 삭제 시 첨부파일이 있으면 첨부파일도 같이 삭제, 게시물 수정 시 첨부파일 변경하면 기존 첨부파일 삭제
    public boolean fileDelete(String bfile){
        System.out.println("FileService.fileDelete");
        //1. 경로와 파일을 합쳐서 파일 위치 찾기
        String filePath=uploadPath+bfile;
        //2. File클래스의 메소드 활용
            //.exists() : 해당 파일의 존재 여부
            //.length() : 해당 파일의 크기/용량(바이트 단위)
            //.delete() : 해당 파일을 삭제
        File file=new File(filePath);
        if(file.exists()){//해당 경로에 파일이 존재하면 삭제
            System.out.println("fileService if run");
            return file.delete();  //해당 경로에 파일 삭제;
        }
        return false;
    }//m end
}//c end
/*


        System.out.println("memberDto = " + memberDto);
        // 확인 : 첨부파일 MultipartFile 타입
        MultipartFile 첨부파일 = memberDto.getImg();
        System.out.println( 첨부파일 );         // 첨부파일이 들어있는 객체 주소
        System.out.println( 첨부파일.getSize() );   // 첨부파일 용량 : 18465 바이트
        System.out.println( 첨부파일.getContentType()); //  image/png : 첨부파일의 확장자
        System.out.println( 첨부파일.getOriginalFilename()); // logo.png : 첨부파일의 이름(확장자포함)
        System.out.println( 첨부파일.getName() );   // img : form input name


        // 서버에 업로드 했을때 설계
        // 1. 여러 클라이언트[다수]가 동일한 파일명으로 서버[1명]에게 업로드 했을떄 [ 식별깨짐]
        // 식별이름 : 1.(아이디어)날짜조합+데이터(PK)  2.UUID( 중복 거의 없음 식별 난수 생성 , 가독성 떨어짐 )
        // 2. 클라이언트 화면 표시
        // 업로드 경로 : 아파치 톰캣( static )에 업로드./
        // * 업로드 할 경로 설정( 내장 톰캣 경로 )

        // * 파일 이름 조합하기 : 새로운 식별이름과 실제 파일 이름
        // 식별키 와 실제 이름 구분 : 왜?? 나중에 쪼개서 구분할려고.[ 다운로드시 식별키 빼고 제공할려고 ]
        // 혹시나 파일 이름이 구분문자 가 있을경우 기준이 꺠짐.
        // .replaceAll() : 문자열 치환/교체


 // 1 첨부파일 업로드 하기. [ 업로드란 : 클라이언트의 바이트(대용량/파일)을 서버로 복사 ]
        // 1. [어디에] 첨부파일을 저장할 경로
        // File 클래스 : 파일 관련된 메소드 제공.
        // new File( 파일경로 );
                System.out.println("file = " + file);  // c:\java\test.png
        System.out.println("file.exists() = " + file.exists() );

 */
