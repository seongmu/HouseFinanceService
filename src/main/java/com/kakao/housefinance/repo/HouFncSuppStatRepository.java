package com.kakao.housefinance.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.kakao.housefinance.model.HouFncSuppStat;

public interface HouFncSuppStatRepository extends JpaRepository<HouFncSuppStat, Long>, JpaSpecificationExecutor<HouFncSuppStat> {

	@Query(value = "select   YEAR" + 
					"      , SUM(JUTAECK) + SUM(KOOKMIN) + SUM(WOORI) + SUM(SHINHAN) + SUM(CITI) + SUM(HANA) + SUM(NONGHYUP) + SUM(KE) + SUM(ETC) AS ALL_SUM" + 
					"      , SUM(JUTAECK) JUTAECK_SUM" + 
					"      , SUM(KOOKMIN) KOOKMIN_SUM" + 
					"      , SUM(WOORI) WOORI_SUM" + 
					"      , SUM(SHINHAN) SHINHAN_SUM" + 
					"      , SUM(CITI) CITI_SUM" + 
					"      , SUM(HANA) HANA_SUM" + 
					"      , SUM(NONGHYUP) NONGHYUP_SUM" + 
					"      , SUM(KE) KE_SUM" + 
					"      , SUM(ETC)  ETC_SUM" + 
					" FROM hou_fnc_supp_stat" + 
					" group by YEAR" + 
					" order by YEAR", nativeQuery = true)
	List<Object[]> findAllSumHouFncSuppStat();
	
	@Query(value = " SELECT B.YEAR" +
			"       ,B.INSTITUTE AS BANK" +
			"       ,B.JUTAECK_SUM AS MAX_AMOUNT" +
			" FROM" +
			" (" +
			"   (SELECT A.YEAR" +
			"         ,'주택도시기금' AS INSTITUTE" +
			"         ,A.JUTAECK_SUM" +
			"   FROM" +
			"   (" +
			"   SELECT YEAR" +
			"         , SUM(JUTAECK) JUTAECK_SUM" +
			"         , SUM(KOOKMIN) KOOKMIN_SUM" +
			"         , SUM(WOORI) WOORI_SUM" +
			"         , SUM(SHINHAN) SHINHAN_SUM" +
			"         , SUM(CITI) CITI_SUM" +
			"         , SUM(HANA) HANA_SUM" +
			"         , SUM(NONGHYUP) NONGHYUP_SUM" +
			"         , SUM(KE) KE_SUM" +
			"         , SUM(ETC)  ETC_SUM" +
			"   FROM HOU_FNC_SUPP_STAT" +
			"   GROUP BY YEAR" +
			"   ORDER BY YEAR" +
			"   ) A" +
			"   ORDER BY A.JUTAECK_SUM DESC" +
			"   LIMIT 1)" +
			"   UNION" +
			"   (SELECT A.YEAR" +
			"         ,'국민은행' AS INSTITUTE" +
			"         ,A.KOOKMIN_SUM" +
			"   FROM" +
			"   (" +
			"   SELECT YEAR" +
			"         , SUM(JUTAECK) JUTAECK_SUM" +
			"         , SUM(KOOKMIN) KOOKMIN_SUM" +
			"         , SUM(WOORI) WOORI_SUM" +
			"         , SUM(SHINHAN) SHINHAN_SUM" +
			"         , SUM(CITI) CITI_SUM" +
			"         , SUM(HANA) HANA_SUM" +
			"         , SUM(NONGHYUP) NONGHYUP_SUM" +
			"         , SUM(KE) KE_SUM" +
			"         , SUM(ETC)  ETC_SUM" +
			"   FROM HOU_FNC_SUPP_STAT" +
			"   GROUP BY YEAR" +
			"   ORDER BY YEAR" +
			"   ) A" +
			"   ORDER BY A.KOOKMIN_SUM DESC" +
			"   LIMIT 1)" +
			"   UNION" +
			"   (SELECT A.YEAR" +
			"         ,'우리은행' AS INSTITUTE" +
			"         ,A.WOORI_SUM" +
			"   FROM" +
			"   (" +
			"   SELECT YEAR" +
			"         , SUM(JUTAECK) JUTAECK_SUM" +
			"         , SUM(KOOKMIN) KOOKMIN_SUM" +
			"         , SUM(WOORI) WOORI_SUM" +
			"         , SUM(SHINHAN) SHINHAN_SUM" +
			"         , SUM(CITI) CITI_SUM" +
			"         , SUM(HANA) HANA_SUM" +
			"         , SUM(NONGHYUP) NONGHYUP_SUM" +
			"         , SUM(KE) KE_SUM" +
			"         , SUM(ETC)  ETC_SUM" +
			"   FROM HOU_FNC_SUPP_STAT" +
			"   GROUP BY YEAR" +
			"   ORDER BY YEAR" +
			"   ) A" +
			"   ORDER BY A.WOORI_SUM DESC" +
			"   LIMIT 1)" +
			"   UNION" +
			"   (SELECT A.YEAR" +
			"         ,'신한은행' AS INSTITUTE" +
			"         ,A.SHINHAN_SUM" +
			"   FROM" +
			"   (" +
			"   SELECT YEAR" +
			"         , SUM(JUTAECK) JUTAECK_SUM" +
			"         , SUM(KOOKMIN) KOOKMIN_SUM" +
			"         , SUM(WOORI) WOORI_SUM" +
			"         , SUM(SHINHAN) SHINHAN_SUM" +
			"         , SUM(CITI) CITI_SUM" +
			"         , SUM(HANA) HANA_SUM" +
			"         , SUM(NONGHYUP) NONGHYUP_SUM" +
			"         , SUM(KE) KE_SUM" +
			"         , SUM(ETC)  ETC_SUM" +
			"   FROM HOU_FNC_SUPP_STAT" +
			"   GROUP BY YEAR" +
			"   ORDER BY YEAR" +
			"   ) A" +
			"   ORDER BY A.SHINHAN_SUM DESC" +
			"   LIMIT 1)" +
			"   UNION" +
			"   (SELECT A.YEAR" +
			"         ,'한국시티은행' AS INSTITUTE" +
			"         ,A.CITI_SUM" +
			"   FROM" +
			"   (" +
			"   SELECT YEAR" +
			"         , SUM(JUTAECK) JUTAECK_SUM" +
			"         , SUM(KOOKMIN) KOOKMIN_SUM" +
			"         , SUM(WOORI) WOORI_SUM" +
			"         , SUM(SHINHAN) SHINHAN_SUM" +
			"         , SUM(CITI) CITI_SUM" +
			"         , SUM(HANA) HANA_SUM" +
			"         , SUM(NONGHYUP) NONGHYUP_SUM" +
			"         , SUM(KE) KE_SUM" +
			"         , SUM(ETC)  ETC_SUM" +
			"   FROM HOU_FNC_SUPP_STAT" +
			"   GROUP BY YEAR" +
			"   ORDER BY YEAR" +
			"   ) A" +
			"   ORDER BY A.CITI_SUM DESC" +
			"   LIMIT 1)" +
			"   UNION" +
			"   (SELECT A.YEAR" +
			"         ,'하나은행' AS INSTITUTE" +
			"         ,A.HANA_SUM" +
			"   FROM" +
			"   (" +
			"   SELECT YEAR" +
			"         , SUM(JUTAECK) JUTAECK_SUM" +
			"         , SUM(KOOKMIN) KOOKMIN_SUM" +
			"         , SUM(WOORI) WOORI_SUM" +
			"         , SUM(SHINHAN) SHINHAN_SUM" +
			"         , SUM(CITI) CITI_SUM" +
			"         , SUM(HANA) HANA_SUM" +
			"         , SUM(NONGHYUP) NONGHYUP_SUM" +
			"         , SUM(KE) KE_SUM" +
			"         , SUM(ETC)  ETC_SUM" +
			"   FROM HOU_FNC_SUPP_STAT" +
			"   GROUP BY YEAR" +
			"   ORDER BY YEAR" +
			"   ) A" +
			"   ORDER BY A.HANA_SUM DESC" +
			"   LIMIT 1)" +
			"   UNION" +
			"   (SELECT A.YEAR" +
			"         ,'농협은행/수협은행' AS INSTITUTE" +
			"         ,A.NONGHYUP_SUM" +
			"   FROM" +
			"   (" +
			"   SELECT YEAR" +
			"         , SUM(JUTAECK) JUTAECK_SUM" +
			"         , SUM(KOOKMIN) KOOKMIN_SUM" +
			"         , SUM(WOORI) WOORI_SUM" +
			"         , SUM(SHINHAN) SHINHAN_SUM" +
			"         , SUM(CITI) CITI_SUM" +
			"         , SUM(HANA) HANA_SUM" +
			"         , SUM(NONGHYUP) NONGHYUP_SUM" +
			"         , SUM(KE) KE_SUM" +
			"         , SUM(ETC)  ETC_SUM" +
			"   FROM HOU_FNC_SUPP_STAT" +
			"   GROUP BY YEAR" +
			"   ORDER BY YEAR" +
			"   ) A" +
			"   ORDER BY A.NONGHYUP_SUM DESC" +
			"   LIMIT 1)" +
			"   UNION" +
			"   (SELECT A.YEAR" +
			"         ,'외환은행' AS INSTITUTE" +
			"         ,A.KE_SUM" +
			"   FROM" +
			"   (" +
			"   SELECT YEAR" +
			"         , SUM(JUTAECK) JUTAECK_SUM" +
			"         , SUM(KOOKMIN) KOOKMIN_SUM" +
			"         , SUM(WOORI) WOORI_SUM" +
			"         , SUM(SHINHAN) SHINHAN_SUM" +
			"         , SUM(CITI) CITI_SUM" +
			"         , SUM(HANA) HANA_SUM" +
			"         , SUM(NONGHYUP) NONGHYUP_SUM" +
			"         , SUM(KE) KE_SUM" +
			"         , SUM(ETC)  ETC_SUM" +
			"   FROM HOU_FNC_SUPP_STAT" +
			"   GROUP BY YEAR" +
			"   ORDER BY YEAR" +
			"   ) A" +
			"   ORDER BY A.KE_SUM DESC" +
			"   LIMIT 1)" +
			"   UNION" +
			"   (SELECT A.YEAR" +
			"         ,'기타은행' AS INSTITUTE" +
			"         ,A.ETC_SUM" +
			"   FROM" +
			"   (" +
			"   SELECT YEAR" +
			"         , SUM(JUTAECK) JUTAECK_SUM" +
			"         , SUM(KOOKMIN) KOOKMIN_SUM" +
			"         , SUM(WOORI) WOORI_SUM" +
			"         , SUM(SHINHAN) SHINHAN_SUM" +
			"         , SUM(CITI) CITI_SUM" +
			"         , SUM(HANA) HANA_SUM" +
			"         , SUM(NONGHYUP) NONGHYUP_SUM" +
			"         , SUM(KE) KE_SUM" +
			"         , SUM(ETC)  ETC_SUM" +
			"   FROM HOU_FNC_SUPP_STAT" +
			"   GROUP BY YEAR" +
			"   ORDER BY YEAR" +
			"   ) A" +
			"   ORDER BY A.ETC_SUM DESC" +
			"   LIMIT 1)" +
			" ) B " +
			" ORDER BY B.JUTAECK_SUM DESC" +
			" LIMIT 1", nativeQuery = true)
	List<Object[]> findMaxSuppAmtInst();
	
	@Query(value = " (SELECT YEAR"+
			"       , SUM(KE)/12 KE_AVG"+
			"       , '외환은행' BANK"+
			" FROM HOU_FNC_SUPP_STAT"+
			" GROUP BY YEAR"+
			" ORDER BY SUM(KE)/12 ASC"+
			" LIMIT 1)"+
			" UNION"+
			" (SELECT YEAR"+
			"       , SUM(KE)/12 KE_AVG"+
			"       , '외환은행' BANK"+
			" FROM HOU_FNC_SUPP_STAT"+
			" GROUP BY YEAR"+
			" ORDER BY SUM(KE)/12 DESC"+
			" LIMIT 1)", nativeQuery = true)
	List<Object[]> findMinMaxKeAvg();
	
}