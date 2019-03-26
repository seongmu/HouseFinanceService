package com.kakao.housefinance.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.kakao.housefinance.model.Institute;;

public interface InstituteRepository extends CrudRepository<Institute, Long> {

	List<Institute> findAllByOrderByIdAsc();
	
	List<Institute> findAllByOrderByIdDesc();	
	
}