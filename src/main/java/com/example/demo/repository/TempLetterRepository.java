package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.TempLetterEntity;
import com.example.demo.entity.UserEntity;

import java.util.List;
import java.util.Optional;


@Repository
public interface TempLetterRepository extends JpaRepository<TempLetterEntity, Long>{
	List<TempLetterEntity> findByRelatedUidAndUser(Long relatedUid, UserEntity user);
	List<TempLetterEntity> findByUser(UserEntity user);//6
}
