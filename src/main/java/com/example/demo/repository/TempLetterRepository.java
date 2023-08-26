package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.TempLetterEntity;

@Repository
public interface TempLetterRepository extends JpaRepository<TempLetterEntity, Long>{
	
}
