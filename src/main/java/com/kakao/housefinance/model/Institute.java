package com.kakao.housefinance.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Institute {
	private static final long serialVersionUID = -3009157732242241606L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(name = "instituteName")
	private String instituteName;      
	
	@Column(name = "instituteCode")
	private String instituteCode;      
	
	
	// 디비에 넘길 데이터 세팅
	@Builder
	public Institute(String instituteCode, String instituteName) {
		this.instituteCode = instituteCode;
		this.instituteName = instituteName;
	}
}
