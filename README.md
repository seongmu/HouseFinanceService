

# HouseFinanceService
카카오페이 사전 과제 주택 금융 서비스 API 개발

1. 개발 프레임워크 

   - 개발 툴 : Spring boot (STS 설치 https://spring.io/tools3/sts/all)

   - 개발 언어 : java (java 1.8 설치 https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)

   - 개발 DB : MariaDB (MariaDB 10.3 설치 https://downloads.mariadb.org/mariadb/10.3.13/)

   - 개발 실행도구 : Postman (크롬 확장프로그램 설치 https://chrome.google.com/webstore/detail/postman/fhbjgbiflinjbdggehcddcbncdddomop?hl=ko)

2. 문제 해결 전략

   2-1. 데이터 파일에서 각 레코드를 데이터베이스에 저장하는 API 

    - 서버에서 파일이 올라와 있는 위치를 찾은 후에 파일에서 데이터를 읽은 후에 1 라인 단위로 읽어서 저장
  
    - 데이터를 확인 해 보니 콤마(,)로만 구분자 가능한 것이 있고, 큰따옴표(")로 묶여 있고 안에 숫자가 콤마(,)로 단위 구분되어 있는 것 발견
  
    - 조건절을 걸어 토큰을 두개로 나눠서 각각 문자열 배열로 만들고, 테이블 엔터티 생성 후 arraylist 에 저장
  
    - 해당 arraylist 로 한방에 insert
    
   2-2. 주택 금융 공급 금융기관(은행) 목록을 출력하는 API

    - 제약 사항에 주택금융공급기관은 따로 디자인한다고 하여 테이블 새로 하나 생성(테이블명 : institute, 칼럼명 : institute_code, institute_name)
    
    - 주어진 데이터가 없어서 해당금융기관에 임의로 코드를 주어서 코드, 금융기관명으로 매핑된 데이터 쿼리로 인서트
            
      insert into institute(INSTITUTE_CODE, INSTITUTE_NAME) values('bnk3725', '주택도시기금');
      
      insert into institute(INSTITUTE_CODE, INSTITUTE_NAME) values('bnk3726', '국민은행');
      
      insert into institute(INSTITUTE_CODE, INSTITUTE_NAME) values('bnk3727', '우리은행');
      
      insert into institute(INSTITUTE_CODE, INSTITUTE_NAME) values('bnk3728', '신한은행');
      
      insert into institute(INSTITUTE_CODE, INSTITUTE_NAME) values('bnk3729', '한국시티은행');
      
      insert into institute(INSTITUTE_CODE, INSTITUTE_NAME) values('bnk3730', '하나은행');
      
      insert into institute(INSTITUTE_CODE, INSTITUTE_NAME) values('bnk3731', '농협은행/수협은행');
      
      insert into institute(INSTITUTE_CODE, INSTITUTE_NAME) values('bnk3732', '외환은행');
      
      insert into institute(INSTITUTE_CODE, INSTITUTE_NAME) values('bnk3733', '기타은행');
            
      
    - 금융기관 전체 select
    
   2-3. 연도별 각 금융기관의 지원금액 합계를 출력하는 API
  
    - 각 연도별로 group by 이후에 각 금융기관별 지원금액 sum 하여 select
    
    - Hibernate 에서 native query 이용
    
   2-4. 각 연도별 각 기관의 전체 지원금액 중에서 가장 큰 금액의 기관명을 출력하는 API
  
    - 연도별로 group by 해서 각 기관별로 해당 연도에 지원한 금액을 sum 하고, 가장 큰 금액을 지원한 기관 1개만 조회
    
    - 위의 작업을 시작년도부터 마지막년도까지 union 으로 합쳐서 쿼리 완성
    
    - Hibernate 에서 native query 이용
    
   2-5. 전체 년도에서 외환은행의 지원금액 평균 중에서 가장 작은 금액과 큰 금액을 출력하는 API
  
    - 연도별로 group by 하고 외환은행의 평균값을 조회 하는데 가장 작은 금액 1개 조회
    
    - 위와 같은 작업이지만 평균값 중 가장 큰 금액 1개 행 조회
    
    - 각각 뽑은 두개의 쿼리를 union 으로 합쳐서 select
    
    - Hibernate 에서 native query 이용
    
   2-6. API 인증을 위해 JWT(Json Web Token)를 이용해서 Token 기반 API 인증 기능
   
    - 주어진 데이터베이스가 없어 임의로 3개의 테이블을 생성(roles, user_roles, users)
    
    - 패스워드는 암호화 되서 저장 되도록 기능 생성
    
    - 로그인 시 토큰 생성하도록 요청된 userid 와 password 를 조사하여 올바르면 토큰 특정 secret으로 서명 하여 생성
    
    - refresh 토큰 생성시 Authorization 헤더에 이미 기존의 토큰이 들어가 있으므로 Bear Token 을 Authorization 에 입력한다면 기존의 토큰이 날아가서 회원 정보를 알 수 없으므로, 기존의 토큰 앞에 접두어로 붙이는 것으로 결정하고 접두어로 붙어서 온다면 refresh 토큰 생성
    
    - restful 서비스(위의 5개)를 호출할때 해당 토큰으로 유저 정보를 제대로 가져오는지 확인
    
    - restful 서비스(위의 5개)로 작성한 api 에 해당롤을 주어서 해당롤이 아니거나, 로그아웃, 토큰이 잘못되어 있다면 호출이 안되도록 수정
    
3. 빌드 및 실행 방법

   - 해당 소스 파일 다운로드 (github https://github.com/seongmu/HouseFinanceService.git) 
   
   - STS 실행
   
   - 다운로드 받은 소스 파일 import
   
   - 자동으로 maven 다운로드가 실행되지만 안되는 경우 maven update 하여 모든 jar 파일 다운로드 (제대로 다운 안되면 구동시 에러 발생)
   
   - STS 에 기본으로 탑재되어 있는 서버로 실행 (Boot Dashboard 라는 view를 띄워 놓으면 잘 보인다)
   
   - 서버가 중간에 올라가다 에러가 발생시 마리아디비에 해당 데이터베이스와 해당테이블을 create (필요한 쿼리문 sql 파일로 첨부함)
   
   - 서버가 구동되었으면 크롬에 설치한 Postman 실행
   
   - 테스트 순서는 기본데이터(롤) 인서트 --> 회원가입 --> 로그인 --> 해당 api (5개) 순서대로 수행 --> refresh 발급 수행
   
   - 롤 인서트 
   
      INSERT INTO roles(name) VALUES('ROLE_USER');
   
      INSERT INTO roles(name) VALUES('ROLE_PM');

      INSERT INTO roles(name) VALUES('ROLE_ADMIN');

   - 회원가입
   
      - Postman 에서 Post 방식
      
      - url : localhost:8080/house/auth/signup
      
      - headers에 Content-Type : application/json
      
      - Body에 raw 선택 json(application/json) 선택후 후 데이터는 아래처럼 세팅하고 Send (파일로 양식 첨부 )
      
      
      {
         "name":"Jack",
	 "username":"jackgkz",
	 "email":"jack@grokonez.com",
	 "role":["user"],
	 "password":"123456789",
	 "userid":"jack1"
      }
      
   - 로그인
      위와 같은 방식으로 url 과 데이터 세팅만 다르다 url : localhost:8080/house/auth/signin
      
      {
	 "username":"jackgkz",
	 "password":"123456789",
	 "userid":"jack1"
      }


   
