package com.kakao.housefinance.junit;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringRunner;
import org.w3c.dom.Document;

import com.kakao.housefinance.model.HouFncSuppStat;
import com.kakao.housefinance.model.Institute;
import com.kakao.housefinance.repo.HouFncSuppStatRepository;
import com.kakao.housefinance.repo.InstituteRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HouFncSuppStatRepositoryTest {
//	@Autowired
//	CustomerRepository postsRepository;

	@Autowired
	HouFncSuppStatRepository houFncSuppStatRepository;
	
    @After
    public void cleanup() {
        /** 
        이후 테스트 코드에 영향을 끼치지 않기 위해 
        테스트 메소드가 끝날때 마다 respository 전체 비우는 코드
        **/
//        postsRepository.deleteAll();
        
        houFncSuppStatRepository.deleteAll();
        
        instituteRepository.deleteAll();
    }

//    public static Document readXml(InputStream is) throws SAXException, IOException,
//	    ParserConfigurationException {
//	    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//	
//	    dbf.setValidating(false);
//	    dbf.setIgnoringComments(false);
//	    dbf.setIgnoringElementContentWhitespace(true);
//	    dbf.setNamespaceAware(true);
//	    DocumentBuilder db = null;
//	    db = dbf.newDocumentBuilder();
//	
//	    return db.parse(is);
//    }
    
    // 테스트 데이터
    private class TestData {
    	public String[][] sData = {
    			{"2005","1","1019","846","82","95","30","157","57","80","99"},
                {"2005","2","1144","864","91","97","35","168","36","111","114"},
                {"2005","3","1828","1234","162","249","54","260","112","171","149"},
                {"2005","4","2246","1176","209","167","66","291","101","220","111"},
                {"2005","5","2106","1145","251","164","94","273","150","181","116"},
                {"2008","3","3117","1428","171","98","5","181","85","89","97"},
                {"2008","4","3394","1369","177","156","10","178","389","98","68"},
                {"2008","5","2840","1501","499","106","8","183","918","144","56"},
                {"2015","2","6035","3906","5525","4075","6","3984","1138","946","2186"},
                {"2015","3","7066","5965","6165","3067","2","2786","1075","801","4776"}
		};    	
    }
    
    // 1. 데이터 파일에서 각 레코드를 데이터베이스에 저장 단위테스트
    //@Test
    public void saveHouFncSuppStatAll() throws Exception {
    	// 서버에 파일이 올라가 있는 경로
    	String filepath = "files/2019경력공채_개발_사전과제3_주택금융신용보증_금융기관별_공급현황.csv";
    	
    	// 해당 경로에서 리소스 얻어오기
    	Resource resource = new ClassPathResource(filepath);
    	
    	// 파일에서 읽어온 정보를 모두 저장한 후에 엔터티 클래스에 넘길 배열리스트
    	ArrayList<HouFncSuppStat> al = new ArrayList<HouFncSuppStat>();
    	
    	Document document;
    	
    	int nColNmFlg = 0;
    	
        try {
        	// 데이터를 처음부터 끝까지 라인단위로 읽고 arraylist 에 담는다
            InputStream dbAsStream = resource.getInputStream();
          
            BufferedReader br = new BufferedReader(new InputStreamReader(dbAsStream, "EUC-KR"));
            
            String line = null;
            
            while ((line = br.readLine()) != null) {
                // 첫 행인경우 칼럼명이기 때문에 패스하고 다음행부터 엔터티 생성
                if (nColNmFlg == 0) {
                	nColNmFlg++;
				} else {
					nColNmFlg++; // 나중에 데이터 갯수 검증 위해서 계속 수량 증가
	            	if (line.contains("\"")) { // 금액부분이 큰 따옴표로 묶여 있는 경우
	            		int index= 0;
	            		ArrayList<String> oneRowAL = new ArrayList<String>();
	            		String sAmount = "";
	            		
	            		for (int i = 0; i < line.length(); i++) {
							if (line.charAt(i) == ',') {
								sAmount = line.substring(index, i);
								index = i + 1;
								
								oneRowAL.add(sAmount);
							} else if (line.charAt(i) == '"') {
								for (int j = i + 1; j < line.length(); j++) {
									if (line.charAt(j) == '"') {
										sAmount = line.substring(i + 1, j).replace(",", "");
										
										i = j + 1; // 문자열 검색 인덱스를, 찾은 " 만큼 이동시킨다
										index = i + 1; // 이동된 문자열 검색 인덱스를, 예전 인덱스로 넣는다
										break;
									}
								}
								
								oneRowAL.add(sAmount);
							} else {
								sAmount = line.substring(index, i + 1);
								//index = i + 1;
							}
						}
	            		al.add( new HouFncSuppStat( oneRowAL.toArray(new String[oneRowAL.size()]) ) );
					} else { // 구분자가 , 이고 "" 로 묶이지 않은 경우
						String[] oneRowData = oneRowData = line.split(","); // ,로 구분
						al.add(new HouFncSuppStat(oneRowData));
					}					
				}
            }
        } catch (IOException e) {
            e.printStackTrace();
        }        
        
		// given
		houFncSuppStatRepository.saveAll(al);
				
        // when
        List<HouFncSuppStat> postsList = (List<HouFncSuppStat>) houFncSuppStatRepository.findAll();

        // then
        int nCnt = 0;
        for (HouFncSuppStat houFncSuppStat : postsList) {
			System.out.println(nCnt + " 번째 : " + houFncSuppStat.getYear() + ", " + houFncSuppStat.getMonth() + ", " + houFncSuppStat.getKookmin()
			+ ", " + houFncSuppStat.getJutaeck() + ", " + houFncSuppStat.getWoori());
			
			nCnt++;
        }
        
        if (nCnt == (nColNmFlg - 1)) {
			System.out.println("파일에서 읽어서 디비에 입력한 행의 갯수가 " + (nColNmFlg - 1) + ", 디비에서 읽은 행의 갯수가 " + nCnt + " 서로 일치 합니다");
		} else {
			throw new Exception("파일에서 읽은 행의 갯수랑 디비에서 읽은 행의 갯수 불일치");
		}
    }
           
    @Autowired
    InstituteRepository instituteRepository;
    
    // 2. 주택 금융 공급 금융기관(은행) 목록을 출력 단위테스트
    //@Test
    public void findInstituteAll() throws Exception {
    	// given
    	instituteRepository.save(Institute.builder()
              .instituteCode("bnk3725")
              .instituteName("주택도시기금")
              .build());
    	
    	instituteRepository.save(Institute.builder()
                .instituteCode("bnk3728")
                .instituteName("신한은행")
                .build());
    	
    	instituteRepository.save(Institute.builder()
                .instituteCode("bnk3731")
                .instituteName("농협은행/수협은행")
                .build());
    	
    	instituteRepository.save(Institute.builder()
                .instituteCode("bnk3733")
                .instituteName("기타은행")
                .build());
    	
		// when
    	//List<Institute> instituteList = (List<Institute>) instituteRepository.findByInstituteCode();
		List<Institute> list = (List<Institute>) instituteRepository.findAllByOrderByIdAsc();

		// 실제 디비에 박혀 있는 데이터
//		String[][] sTest = { { "bnk3725", "주택도시기금" }, { "bnk3726", "국민은행" }, { "bnk3727", "우리은행" },
//				{ "bnk3728", "신한은행" }, { "bnk3729", "한국시티은행" }, { "bnk3730", "하나은행" }, { "bnk3731", "농협은행/수협은행" },
//				{ "bnk3732", "외환은행" }, { "bnk3733", "기타은행" } };

		// then
		if (list.size() == 4) {
			int i = 0;
			for (Institute objects : list) {
				if (i == 0) {
					assertThat(objects.getInstituteCode(), is("bnk3725"));
					assertThat(objects.getInstituteName(), is("주택도시기금"));
				} else if (i == 1) {
					assertThat(objects.getInstituteCode(), is("bnk3728"));
					assertThat(objects.getInstituteName(), is("신한은행"));
				} else if (i == 2) {
					assertThat(objects.getInstituteCode(), is("bnk3731"));
					assertThat(objects.getInstituteName(), is("농협은행/수협은행"));
				} else if (i == 3) {
					assertThat(objects.getInstituteCode(), is("bnk3733"));
					assertThat(objects.getInstituteName(), is("기타은행"));
				}

				i++;
			}
		} else {
			throw new Exception("행의 갯수 불일치. 금융기관 목록행 리턴이 잘못되었습니다. 쿼리를 확인하세요.");
		}
		
    }
    
    // 3. 연도별 각 금융기관의 지원금액 합계를 출력 단위테스트
    //@Test
    public void sumHouFncSuppStat() throws Exception {
    	// given
    	TestData td = new TestData();
    	
    	ArrayList<HouFncSuppStat> al = new ArrayList<HouFncSuppStat>();
    	
    	for (int i = 0; i < td.sData.length; i++) {
    		al.add(new HouFncSuppStat(td.sData[i]));
		}
    	
    	houFncSuppStatRepository.saveAll(al);
    	
    	// when
    	List<Object[]> list = (List<Object[]>) houFncSuppStatRepository.findAllSumHouFncSuppStat();
    	
    	// then
    	if (list.size() == 3) {
    		int i = 0;
    		for (Object[] objects : list) {
    			if (i == 0) {
    				assertThat(objects[0].toString(), is("2005"));
    				assertThat(objects[1].toString(), is("18411"));
    				assertThat(objects[2].toString(), is("8343"));
    				assertThat(objects[3].toString(), is("5265"));
    				assertThat(objects[4].toString(), is("795"));
    				assertThat(objects[5].toString(), is("772"));
    				assertThat(objects[6].toString(), is("279"));
    				assertThat(objects[7].toString(), is("1149"));
    				assertThat(objects[8].toString(), is("456"));
    				assertThat(objects[9].toString(), is("763"));
    				assertThat(objects[10].toString(), is("589"));
				} else if (i == 1) {
					assertThat(objects[0].toString(), is("2008"));
					assertThat(objects[1].toString(), is("17365"));
    				assertThat(objects[2].toString(), is("9351"));
    				assertThat(objects[3].toString(), is("4298"));
    				assertThat(objects[4].toString(), is("847"));
    				assertThat(objects[5].toString(), is("360"));
    				assertThat(objects[6].toString(), is("23"));
    				assertThat(objects[7].toString(), is("542"));
    				assertThat(objects[8].toString(), is("1392"));
    				assertThat(objects[9].toString(), is("331"));
    				assertThat(objects[10].toString(), is("221"));
				} else if (i == 2) {
					assertThat(objects[0].toString(), is("2015"));
					assertThat(objects[1].toString(), is("59504"));
    				assertThat(objects[2].toString(), is("13101"));
    				assertThat(objects[3].toString(), is("9871"));
    				assertThat(objects[4].toString(), is("11690"));
    				assertThat(objects[5].toString(), is("7142"));
    				assertThat(objects[6].toString(), is("8"));
    				assertThat(objects[7].toString(), is("6770"));
    				assertThat(objects[8].toString(), is("2213"));
    				assertThat(objects[9].toString(), is("1747"));
    				assertThat(objects[10].toString(), is("6962"));
				}
    			i++;
    		}
		} else {
			throw new Exception("행의 갯수 불일치. 합계 쿼리 행 리턴이 잘못 되었습니다. 쿼리를 확인하세요.");
		}
    }
    
    // 4. 각 연도별 각 기관의 전체 지원금액 중에서 가장 큰 금액의 기관명을 출력 단위테스트
    //@Test
    public void findMaxSuppAmtInst() throws Exception {
    	// given
    	TestData td = new TestData();
    	
    	ArrayList<HouFncSuppStat> al = new ArrayList<HouFncSuppStat>();
    	
    	for (int i = 0; i < td.sData.length; i++) {
    		al.add(new HouFncSuppStat(td.sData[i]));
		}
    	
    	houFncSuppStatRepository.saveAll(al);
    	
    	// when
    	List<Object[]> list = (List<Object[]>) houFncSuppStatRepository.findMaxSuppAmtInst();
    	
    	// then
    	// list 의 목록이 1이 아니면 에러
    	if (list.size() > 1 && list.size() != 0) {
			throw new Exception("가장 큰 지원금액의 기관명은 1개만 있어야 됩니다. 쿼리를 수정하시오");
		} else {
			for (Object[] objects : list) {
				assertThat(objects[0].toString(), is("2015"));
				assertThat(objects[1].toString(), is("주택도시기금"));
			}
		}
    }
    
    // 5. 전체 년도에서 외환은행의 지원금액 평균 중에서 가장 작은 금액과 큰 금액을 출력 단위테스트
    //@Test
    public void findMinMaxKeAvg() throws Exception {
    	// given
    	TestData td = new TestData();
    	
    	ArrayList<HouFncSuppStat> al = new ArrayList<HouFncSuppStat>();
    	
    	for (int i = 0; i < td.sData.length; i++) {
    		al.add(new HouFncSuppStat(td.sData[i]));
		}
    	
    	houFncSuppStatRepository.saveAll(al);
    	
    	// when
    	List<Object[]> list = (List<Object[]>) houFncSuppStatRepository.findMinMaxKeAvg();
    	
    	// then
    	if (list.size() == 2) {
    		int i = 0;
    		for (Object[] objects : list) {
    			if (i == 0) {
    				assertThat(objects[0].toString(), is("2008"));
    				assertThat(objects[1].toString(), is("27.5833"));    				
				} else if (i == 1) {
					assertThat(objects[0].toString(), is("2015"));
					assertThat(objects[1].toString(), is("145.5833"));    				
				} 
    			i++;
    		}
		} else {
			throw new Exception("외환은행의 가장 작은 금액, 가장 큰 금액 2개의 행만 나와야 됩니다. 쿼리를 확인하세요.");
		}
    }
    
    // 6. API 인증을 위해 JWT(Json Web Token)를 이용해서 Token 기반 API 인증 기능 단위테스트
    @Test
    public void testAuth() throws Exception {
    	String jwtString = Jwts.builder()
        		.setHeaderParam("typ", "JWT")
        		.setHeaderParam("issueDate", System.currentTimeMillis())
        		.setSubject("내용")
        		.signWith(SignatureAlgorithm.HS512, "aaaa")
        		.compact();
    	
    	System.out.println(jwtString);
    }
    
//    @Test
//    public void 게시글저장_불러오기() {
//        //given
//        postsRepository.save(Customer.builder()
//                .firstName("SeongMu")
//                .lastName("Hwang")
////                .author("jojoldu@gmail.com")
//                .build());
//
//        //when
//        List<Customer> postsList = (List<Customer>) postsRepository.findAll();
//
//        //then
//        Customer posts = postsList.get(0);
//        assertThat(posts.getFirstName(), is("SeongMu"));
//        assertThat(posts.getLastName(), is("Hwang"));
//    }
}
