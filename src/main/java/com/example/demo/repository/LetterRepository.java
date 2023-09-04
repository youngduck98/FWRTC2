package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.LetterEntity;
import com.example.demo.entity.UserHistoryEntity;
import com.example.demo.entity.UserEntity;



@Repository
public interface LetterRepository extends JpaRepository<LetterEntity, Long>{
	List<LetterEntity> findByUserAndHistory(UserEntity user, UserHistoryEntity history);
}
