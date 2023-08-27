package com.example.demo.service.serviceImpl;

import static org.hamcrest.CoreMatchers.nullValue;

import org.springframework.stereotype.Service;

import com.example.demo.entity.UserEntity;
import com.example.demo.entity.UserHistoryEntity;
import com.example.demo.repository.AccountInfoRepositoy;
import com.example.demo.repository.UserHistoryRepository;
import com.example.demo.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserAditionalTransaction {
	private final UserRepository UR;
	private static final String backdoor = "0000"; 
	private final static String default_kakao_uid = "fake_user";
	
	public String injectHistoryToUser(UserHistoryEntity history, UserEntity user) {
		user.updateCompanyuid(history);
		UR.save(user);
		return history.getUid();
	}
}
