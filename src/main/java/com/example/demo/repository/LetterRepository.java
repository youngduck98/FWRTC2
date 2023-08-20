package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.LetterEntity;

@Repository
public interface LetterRepository extends JpaRepository<LetterEntity, Long>{
	
}
