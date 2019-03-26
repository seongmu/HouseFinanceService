package com.kakao.housefinance.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "houFncSuppStat")
public class HouFncSuppStat {
	private static final long serialVersionUID = -3009157732242241606L;

	@Id
//	@GeneratedValue(strategy = GenerationType.AUTO)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(name = "year")
	private String year;      
	
	@Column(name = "month")
	private String month;      
	
	@Column(name = "jutaeck")
	private long jutaeck;    
	
	@Column(name = "kookmin")
	private long kookmin;    
	
	@Column(name = "woori")
	private long woori;      
	
	@Column(name = "shinhan")
	private long shinhan;    
	
	@Column(name = "citi")
	private long citi;      
	
	@Column(name = "hana")
	private long hana;      
	
	@Column(name = "nonghyup")
	private long nonghyup;   
	
	@Column(name = "ke")
	private long ke;      
	
	@Column(name = "etc")
	private long etc;      

	// 디비에 넘길 데이터 세팅
	@Builder
	public HouFncSuppStat(String[] oneRowData) {
		this.year = oneRowData[0];
		this.month = oneRowData[1];
		this.jutaeck =  Long.parseLong(oneRowData[2]);
		this.kookmin = Long.parseLong(oneRowData[3]);
		this.woori = Long.parseLong(oneRowData[4]);
		this.shinhan = Long.parseLong(oneRowData[5]);
		this.citi = Long.parseLong(oneRowData[6]);
		this.hana = Long.parseLong(oneRowData[7]);
		this.nonghyup = Long.parseLong(oneRowData[8]);
		this.ke = Long.parseLong(oneRowData[9]);
		this.etc = Long.parseLong(oneRowData[10]);
	}

	@Override
	public String toString() {
		
		return this.getClass (). getSimpleName () + "-"+ getId ();
//		return String.format("House Finance Supply Satus[id=%d, year='%s', month='%s']", id, year, month);

	}
}
