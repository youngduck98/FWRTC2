package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.AccountInfoEntity;

public interface AccountInfoRepositoy extends JpaRepository<AccountInfoEntity, String>{
	Optional<AccountInfoEntity> findByKakaoUid(String kakao_uid);
}
