package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.UserHistoryEntity;
import java.util.List;


@Repository
public interface UserHistoryRepository extends JpaRepository<UserHistoryEntity, String> {
	Optional<UserHistoryEntity> findByCompanyName(String company_name);
	List<UserHistoryEntity> findByUserUid(String userUid);
}
