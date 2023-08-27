package com.example.demo.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.demo.dto.KakaoVo;
import com.example.demo.entity.AccountInfoEntity;
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
public class KakaoAditionalTransaction {
	
	private final AccountInfoRepositoy air;
	private final UserRepository Ur;
	private final UserHistoryRepository UHER;
	
	@Transactional(rollbackOn = Exception.class)
	public Optional<AccountInfoEntity> joinMemberToDatabase(KakaoVo kakaoInfo, Optional<AccountInfoEntity> userAccount) {
		userAccount = Optional.ofNullable(air.save(AccountInfoEntity.builder()
				.userUid(UUID.randomUUID().toString())
    			.kakaoUid(kakaoInfo.getUid())
    			.build()));
		UserHistoryEntity userHistory = UHER.save(UserHistoryEntity.builder()
				.uid(UUID.randomUUID().toString())
				.userUid(userAccount.get().getUserUid())
				.companyName(null)
				.build());
    	Ur.save(UserEntity.builder()
    			.uid1(userAccount.get())
    			.nickname(kakaoInfo.getNickname())
    			.history(userHistory)
    			.profile_img_path(kakaoInfo.getImg_path())
    			.build());
    	return userAccount;
	}
}
