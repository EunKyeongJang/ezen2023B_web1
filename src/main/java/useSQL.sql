drop database if exists springweb;
create database springweb;
use springweb;

drop table if exists todo;
create table todo(
   id int auto_increment ,             -- 할일 식별번호 / 자동번호
    content varchar(30)   ,               -- 할일 내용
    deadline date ,                     -- 할일 마감일
    state boolean default false   ,         -- 할일 상태 [ true:완료 / false:진행중 ]
    constraint todo_pk_id primary key(id)    -- 식별키
);
select * from todo;

drop table if exists article;            -- 스프링부트 교재 사용하는 테이블
create table article(
   id bigint auto_increment ,
    title varchar(255) ,
    content varchar(255 ) ,
    constraint article_pk_id primary key (id)
);
select * from article;


drop tables if exists member;
create table member(
   no bigint auto_increment ,            -- 회원번호
    id varchar(30) not null unique ,      -- 회원 아이디
    pw varchar(30) not null ,            -- 회원 비밀번호
    name varchar(20) not null ,            -- 회원 이름
    email varchar(50) ,                  -- 회원 이메일
    phone varchar(13) not null unique,      -- 회원 핸드폰 번호
    img text ,                         -- 프로필 사진 경로( 사진데이터 아닌 사진이 저장된 서버 경로 )
    constraint member_no_pk primary key(no ) -- 회원 번호 pk
);
select * from member;


# 1. 게시물 카테고리 
drop table if exists bcategory;
create table  bcategory(
   bcno int unsigned auto_increment , 
    bcname varchar( 30 ) not null unique,
   bcdate datetime default now() not null  ,
    constraint bcategory_bcno_pk primary key ( bcno ) 
);
insert into bcategory( bcname ) values ('자유'),('노하우');
select * from bcategory;

# 2. 게시물 
drop table if exists board;
create table board(
   bno bigint unsigned auto_increment , 
    btitle varchar( 255 ) not null ,
    bcontent longtext ,    
    bfile varchar( 255 ) ,
    bview int unsigned default 0 not null , 
    bdate datetime  default now() not null  ,
    mno  bigint ,
    bcno int unsigned,
    constraint board_bno_pk primary key( bno ) , 
    constraint board_mno_fk foreign key( mno) references member( no ) on update cascade on delete cascade ,
    constraint board_bcno_fk foreign key( bcno ) references bcategory( bcno ) on update cascade on delete cascade
);





# 3. 게시물 댓글
drop table if exists breply;
create table breply(
    brno bigint unsigned auto_increment , 
	brcontent varchar(255) not null,
	brdate datetime default now() not null,
	brindex bigint unsigned default 0 not null,
	mno bigint ,
	bno bigint unsigned,
	constraint breply_brno_fk primary key( brno ) , 
	constraint breply_mno_fk foreign key(mno) references member( no ) on update cascade on delete cascade , 
	constraint breply_bno_fk foreign key(bno) references board( bno ) on update cascade on delete cascade 
);
select * from breply;


# ============= 03/05 페이징 처리 ===============
# 1. 게시물 전체출력
select * from board;

# 2. 게시물 정렬 출력
select * from board order by bdate desc;

# 3. [작성자 아이디, 프로필 출력] 게시물과 회원테이블 조인한 결과 출력 (테이블 합치기) select + select vs join
select * from board b inner join member m on b.mno=m.no order by b.bdate desc;
# 특정 페이지
select * from board b inner join member m on b.mno=m.no where b.bno=1;
# - 중복 필드 금지 (pk-fk 제외), - 통계 필드 금지 (내역, 로그처리**)

# **중요** 키워드 작성 순서 : select 필드명 from 테이블명 inner join 테이블명 on 조인조건 where 일반조건 group by 그룹필드명 having 그룹조건 order by 정렬필드 desc/asc limit 시작번호, 개수;

# 4. 페이지별 개수 제한 # 하나의 페이지에 5개씩 게시물 출력
/*
	1페이지 : limit 0, 5		1*5=>5		(1-1)*5=>0
    2페이지 : limit 5, 5		2*5=>10		(2-1)*5=>5
    3페이지 : limit 10, 5		3*5=>15		(3-1)*5=>10	
    4페이지 : limit 15, 5		4*5=>20		(4-1)*5=>15
    startRow = (currentPage - 1) * 페이지당 게시물 수
*/
select * from board b inner join member m on b.mno=m.no order by b.bdate desc limit 0, 5;
select * from board b inner join member m on b.mno=m.no order by b.bdate desc limit 5, 5;
# select * from board b inner join member m on b.mno=m.no order by b.bdate desc limit ?, ?;

# 총(전체) 페이지 수 : 전체게시물수 / 페이지당 게시물수
select count(*) from board b inner join member m on b.mno=m.no;
# 2. page Boardsize=5
# = 10/5 -> totalPage => 2, 13/5 -> totalPage => 2(몫) + 1(나머지 게시물을 출력하기 위한 1페이지 추가)



#============ 03/06 카테고리 별 게시물 출력 ==============#
# 총 게시물 수 : select count(*) from board;
# 제한된 개수만큼 게시물 출력 : select * from board b inner join member m on b.mno=m.no order by b.bdate desc limit ?, ?;

# 1. [조건추가] 카테고리 만큼의 레코드수 
select count(*) from board where bcno=1;	# 1. bcno=1(자유) 만 레코드 수
select count(*) from board where bcno=2;	# 2. bcno=2(노하우) 만 레코드 수
select count(*) from board where bcno=0;	# 0 없음, 전체출력

# 2. [조건추가] 카테고리 조건이 포함된 제한된 개수만큼 게시물 출력
	# 1. 자유 카테고리의 1페이지
select * from board b inner join member m on b.mno = m.no where bcno = 1 order by b.bdate desc limit 0, 5;
	# 2. 노하우 카테고리의 1페이지
select * from board b inner join member m on b.mno = m.no where bcno = 2 order by b.bdate desc limit 0, 5;
	# 3. 전체
select * from board b inner join member m on b.mno = m.no order by b.date desc limit 0, 5;

# 3. [검색 조건추가]
select count(*) from board where bcno=1 and btitle like '%하%';	#제목에 'java'가 포함되어있는 게시물 출력
select count(*) from board where bcno=1 and bcontent like '%하%';	#내용에 'java'가 포하되어있는 게시물 출력
select count(*) 
	from board b inner join member m
	on b.mno=m.no
    where b.bcno=1 and m.id='qweqwe';	# 작성자 아이디로 검색 시 회원테이블과 조인이 필요하다.

/*
	select count(*) 
	from board b inner join member m
	on b.mno=m.no
    where b.bcno=1 and (java)key like '%하%';
*/















