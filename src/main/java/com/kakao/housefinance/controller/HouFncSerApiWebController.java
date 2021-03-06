package com.kakao.housefinance.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
//import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.Document;

import com.kakao.housefinance.model.HouFncSuppStat;
import com.kakao.housefinance.model.Institute;
import com.kakao.housefinance.repo.HouFncSuppStatRepository;
import com.kakao.housefinance.repo.InstituteRepository;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/house")
public class HouFncSerApiWebController {
	private HouFncSuppStatRepository houFncSuppStatRepository;
	
	// 1. 데이터 파일에서 각 레코드를 데이터베이스에 저장하는 API 를 개발
	@GetMapping("/saveAllHouFncSuppStat")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public String saveHouFncSuppStatAll() {
		// 서버에 파일이 올라가 있는 경로
    	String filepath = "files/2019경력공채_개발_사전과제3_주택금융신용보증_금융기관별_공급현황.csv";
    	
    	// 해당 경로에서 리소스 얻어오기
    	Resource resource = new ClassPathResource(filepath);
    	
    	// 파일에서 읽어온 정보를 모두 저장한 후에 디비로 넘길 배열리스트
    	ArrayList<HouFncSuppStat> al = new ArrayList<HouFncSuppStat>();
    	
    	Document document;
    	
        try {
        	// 작업내용 : 데이터를 처음부터 끝까지 라인단위로 읽고 arraylist 에 담는다
            InputStream dbAsStream = resource.getInputStream();
          
            BufferedReader br = new BufferedReader(new InputStreamReader(dbAsStream, "EUC-KR"));
            
            String line = null;
            
            int nColNmFlg = 0;
            
            while ((line = br.readLine()) != null) {
                // 첫 행인경우 칼럼명이기 때문에 패스하고 다음행부터 엔터티 생성
                if (nColNmFlg == 0) {
                	nColNmFlg++;
				} else {
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

        houFncSuppStatRepository.saveAll(al);
        
		return "{\"result\":\"success\"}";
	}

	private InstituteRepository instituteRepository;
	
	// 2. 주택 금융 공급 금융기관(은행) 목록을 출력하는 API 를 개발
	@GetMapping("/findAllInstitute")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public String findInstituteAll() {

		// JSON 형식으로 변환
		String result = String.format("{\"name\":\"%s\",%n\"supply_finance_list\":[", "주택 금융 공급 금융기관(은행) 목록");

		for (Institute objects : instituteRepository.findAllByOrderByIdAsc()) {
			result += String.format("{\"code\":\"%s\",\"name\":\"%s\"},", objects.getInstituteCode(), objects.getInstituteName());
		}
				
		result = String.format("%s%n]}", result.substring(0, result.lastIndexOf(",")));
		
		return result;

	}
	
	// 3. 연도별 각 금융기관의 지원금액 합계를 출력하는 API 를 개발
	@GetMapping("/findAllSumHouFncSuppStat")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public String findAllSum() {

		// JSON 형식으로 변환
		String result = String.format("{\"name\":\"%s\",%n\"year_support_total_list\":[", "연도별 각 금융기관의 지원금액 합계");

		for (Object[] objects : houFncSuppStatRepository.findAllSumHouFncSuppStat()) {
						
			result += String.format("{\"year\":\"%s년\",\"total_amount\":\"%s\",\"detail_amount\":{\"주택도시기금\":\"%s\",\"국민은행\":\"%s\",\"우리은행\":\"%s\","
					+ "\"신한은행\":\"%s\",\"한국시티은행\":\"%s\",\"하나은행\":\"%s\",\"농협은행/수협은행\":\"%s\",\"외환은행\":\"%s\",\"기타은행\":\"%s\"}},%n",
					objects[0], objects[1].toString(), objects[2].toString(), objects[3].toString(), objects[4].toString(), objects[5].toString(), objects[6].toString(), objects[7].toString(), objects[8].toString(), objects[9].toString(), objects[10].toString());
		}
				
		result = String.format("%s%n]}", result.substring(0, result.lastIndexOf(",")));
		
		return result;

	}
		
	// 4. 각 연도별 각 기관의 전체 지원금액 중에서 가장 큰 금액의 기관명을 출력하는 API 개발
	@GetMapping("/findMaxSuppAmtInst")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public String findMaxSuppAmtInst() {
		
		// JSON 형식으로 변환
		String result = "";

		for (Object[] objects : houFncSuppStatRepository.findMaxSuppAmtInst()) {
						
			result = String.format("{\"year\":\"%s\",\"bank\":\"%s\"}",objects[0], objects[1]);
		}
				
		return result;		
		
	}
	
	// 5. 전체 년도에서 외환은행의 지원금액 평균 중에서 가장 작은 금액과 큰 금액을 출력하는 API 개발
	@GetMapping("/findMinMaxKeAvg")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public String findMinMaxKeAvg() {
		
		// JSON 형식으로 변환
		String result = String.format("{\"bank\":\"외환은행\",\"support_amount\":[%n");

		for (Object[] objects : houFncSuppStatRepository.findMinMaxKeAvg()) {
						
			result += String.format("{\"year\":\"%s\",\"amount\":\"%s\"},",objects[0], objects[1]);
		}
			
		result = String.format("%s%n]}", result.substring(0, result.lastIndexOf(",")));
		
		return result;		
		
	}
	
	// 6. 특정 은행의 특정 달에 대해서 2018 년도 해당 달에 금융지원 금액을 예측하는 API 개발
	@PostMapping("/findPredSuppAmount")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public String findPredSuppAmount(HttpServletRequest request, @RequestParam() Map<String, Object> paramMap) {
		
		StringBuffer json = new StringBuffer();
	    String line = null;
	 
	    try {
	        BufferedReader reader = request.getReader();
	        while((line = reader.readLine()) != null) {
	            json.append(line);
	        }
	 
	    }catch(Exception e) {
	        System.out.println("Error reading JSON string: " + e.toString());
	    }

	    
	    Object obj = null;
		JSONObject jsonObj = null;
		
	    try {
	    	JSONParser parser = new JSONParser();
			obj = parser.parse( json.toString() );
			jsonObj = (JSONObject) obj;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
		
		String bank = (String) jsonObj.get("bank".toString());
		String month = (String) jsonObj.get("month");
				
		List<HouFncSuppStat> list = houFncSuppStatRepository.findPredSuppAmountByYearAsc(month);
			
		ArrayList<Long> alDelMinMax = new ArrayList<Long>();
		ArrayList<Long> alDelMinMax2 = new ArrayList<Long>();
		
		// 해당월 해당금융기관 합계 구하기
		for (int i = 0; i < list.size(); i++) {
			switch (bank) {
			case "주택도시기금":
				alDelMinMax.add( list.get(i).getJutaeck() );
				alDelMinMax2.add( list.get(i).getJutaeck() );
				break;
			case "국민은행":
				alDelMinMax.add( list.get(i).getKookmin() );
				alDelMinMax2.add( list.get(i).getKookmin() );
				break;

			case "우리은행":
				alDelMinMax.add( list.get(i).getWoori() );
				alDelMinMax2.add( list.get(i).getWoori() );
				break;

			case "신한은행":
				alDelMinMax.add( list.get(i).getShinhan() );
				alDelMinMax2.add( list.get(i).getShinhan() );
				break;

			case "한국시티은행":
				alDelMinMax.add( list.get(i).getCiti() );
				alDelMinMax2.add( list.get(i).getCiti() );
				break;

			case "하나은행":
				alDelMinMax.add( list.get(i).getHana() );
				alDelMinMax2.add( list.get(i).getHana() );
				break;

			case "농협은행/수협은행":
				alDelMinMax.add( list.get(i).getNonghyup() );
				alDelMinMax2.add( list.get(i).getNonghyup() );
				break;

			case "기타은행":
				alDelMinMax.add( list.get(i).getEtc() );
				alDelMinMax2.add( list.get(i).getEtc() );
				break;
			default:
				break;
			}
		}
		
		// 이제 기하평균 구하기
		BigDecimal pre = new BigDecimal(alDelMinMax.get(alDelMinMax.size() - 1).toString());
		BigDecimal next = new BigDecimal(alDelMinMax.get(0).toString());
		BigDecimal first = pre.divide(next, MathContext.DECIMAL32);
		
		BigDecimal second = (new BigDecimal("1".toString())).divide(new BigDecimal("10".toString()), MathContext.DECIMAL32); 
		
		BigDecimal lg = new BigDecimal(String.valueOf(Math.pow(first.doubleValue(), second.doubleValue()))).subtract(new BigDecimal("1"));
		
		// 예상값 구하기
		BigDecimal re = (new BigDecimal(alDelMinMax.get(0).toString())).multiply(((new BigDecimal("1")).add(lg)).pow(13));
		
		re = re.setScale(0, BigDecimal.ROUND_HALF_UP);
		
		List<Institute> list2 = instituteRepository.findAllByOrderByIdAsc();
		String sCode = "";
		for (Institute institute : list2) {
			if (institute.getInstituteName().equals(bank)) {
				sCode = institute.getInstituteCode(); 
				break;
			}
		}
		
		System.out.println(re);
		
		// JSON 형식으로 변환
		String resultJson = String.format("{\"bank\":\"%s\",\"year\":\"%s\",\"month\":\"%s\",\"amount\":\"%s\"}",
				sCode, "2018", month, re);
		
		return resultJson;		
		
	}
	
	// 세이브 이후에 전체 조회 쿼리(정상적으로 인서트 되었나 확인용)
	@GetMapping("/findAllHouFncSuppStat")
	public String findHouFncSuppStatAll() {

		String result = "";

		for (HouFncSuppStat houFncSuppStat : houFncSuppStatRepository.findAll()) {

			result += houFncSuppStat + "</br>";

		}

		return result;

	}
	
	
	
	
}
