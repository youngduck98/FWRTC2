package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.dto.AbstractLetterDTO;
import com.example.demo.entity.LetterEntity;
import com.example.demo.entity.LetterRecipientEntity;
import com.example.demo.entity.UserEntity;

@Repository
public interface LetterRecipientRepository extends JpaRepository<LetterRecipientEntity, Long>{
	List<LetterRecipientEntity> findAllBySender(UserEntity sender);
}
