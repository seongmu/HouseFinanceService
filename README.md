
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
