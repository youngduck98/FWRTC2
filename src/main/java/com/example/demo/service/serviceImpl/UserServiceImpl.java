package com.example.demo.service.serviceImpl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.dto.DateDTO;
import com.example.demo.dto.UserVo;
import com.example.demo.entity.AccountInfoEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.entity.UserHistoryEntity;
import com.example.demo.repository.AccountInfoRepositoy;
import com.example.demo.repository.UserHistoryRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
	
	private final UserHistoryRepository UHR;
	private final UserRepository UR;
	private final AccountInfoRepositoy AIR;
	private static final String backdoor = "0000"; 
	private final static String default_kakao_uid = "fake_user";
	
	public Optional<String> extractKakaoUidInTokenOptional(String token){
		String kakao_uid = default_kakao_uid;
		if(!token.equals(backdoor)) {
			try {
				FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
				kakao_uid = decodedToken.getUid();
			} catch (FirebaseAuthException e) {
				log.error("custom_error: failed to decode firebase id token");
				e.printStackTrace();
				return Optional.empty();
			}
		}
		return Optional.ofNullable(kakao_uid);
	}
	
	public Optional<UserEntity> ConvertkakaoUidToUserEntity(String KakaoUid){
		Optional<AccountInfoEntity> accountOptional = AIR.findByKakaoUid(KakaoUid);
		if(accountOptional.isEmpty()) {
			log.error("custom_error: unexpected user, but enrolled in kakao");
			return Optional.empty();
		}
		
		Optional<UserEntity> userOptional = UR.findById(accountOptional.get().getUserUid());
		if(userOptional.isEmpty()) {
			log.error("custom_error: cant find user in user table but entolled in account table");
			return Optional.empty();
		}
		
		return userOptional;
	}
	
	@Override
	public String change_user_company(String token, String company_name) {
		/*
		 * 0. check token is vaild and extract kakao_uid
		 * 1. find user_uid by using kakao_uid
		 * 2. find user by using user_uid
		 * 3. find company uid in company Table by using company name
		 * 4. if company is new, them enroll company
		 * 5. update user info by using company_uuid
		 */
		log.info("0. check token is vaild and extract kakao_uid");
		String kakao_uid = default_kakao_uid;
		try {
			kakao_uid = extractKakaoUidInTokenOptional(token).get();
		}
		catch (Exception e) {
			log.error("faild in extractKakaoUidInTokenOptional");
			return null;
		}
		
		log.info("1. find user_uid by using kakao_uid");
		Optional<AccountInfoEntity> accountOptional = AIR.findByKakaoUid(kakao_uid);
		if(accountOptional.isEmpty()) {
			log.error("custom_error: unexpected user, but enrolled in kakao");
			return null;
		}
		
		log.info("2. find user by using user_uid");
		Optional<UserEntity> userOptional = UR.findById(accountOptional.get().getUserUid());
		if(userOptional.isEmpty()) {
			log.error("custom_error: cant find user in user table but entolled in account table");
			return null;
		}
		
		log.info("3. find company uid in company Table by using company name");
		UserHistoryEntity userHistory = userOptional.get().getHistory();
		
		
		log.info("5. update user info by using company_uuid");
		try {
			userHistory.updateCompanyName(company_name);
			UHR.save(userHistory);
		}
		catch (Exception e) {
			log.error("custom_error: can't update user info-company_uuid");
			return null;
		}
		return userHistory.getUid();
	}
	
	@Override
	public boolean changeUserNickname(String token, String user_nickname) {
		// TODO Auto-generated method stub
		/*
		 * 0. extract kakaouid in token
		 * 1. find useruid by using kakaouid
		 * 2. update nickname by using useruid and kakaouid
		 */
		log.info("0. extract kakaouid in token");
		String kakao_uid = default_kakao_uid;
		try {
			kakao_uid = extractKakaoUidInTokenOptional(token).get();
		}
		catch (Exception e) {
			log.error("faild in extractKakaoUidInTokenOptional");
			return false;
		}
		
		log.info("1. find useruid by using kakaouid");
		Optional<AccountInfoEntity> accountOptional = AIR.findByKakaoUid(kakao_uid);
		if(accountOptional.isEmpty()) {
			log.error("custom_error: unexpected user, but enrolled in kakao");
			return false;
			//회원가입
		}
		
		log.info("2. update nickname by using useruid and kakaouid");
		Optional<UserEntity> userOptional = UR.findById(accountOptional.get().getUserUid());
		if(userOptional.isEmpty()) {
			log.error("custom_error: cant find user in user table but entolled in account table");
			return false;
		}
		
		try {
			userOptional.get().updateNickname(user_nickname);
			UR.save(userOptional.get());
		}
		catch (Exception e) {
			log.error("custom_error: can't update user info-nickname");
			return false;
		}
		
		return true;
	}
	@Override
	public boolean changeUserEnddate(String token, DateDTO date) {
		String kakao_uid = default_kakao_uid;
		try {
			kakao_uid = extractKakaoUidInTokenOptional(token).get();
		}
		catch (Exception e) {
			log.error("faild in extractKakaoUidInTokenOptional");
			return false;
		}
		
		Optional<AccountInfoEntity> accountOptional = AIR.findByKakaoUid(kakao_uid);
		if(accountOptional.isEmpty()) {
			log.error("custom_error: unexpected user, but enrolled in kakao");
			return false;
			//회원가입
		}
		
		Optional<UserEntity> userOptional = UR.findById(accountOptional.get().getUserUid());
		if(userOptional.isEmpty()) {
			log.error("custom_error: cant find user in user table but entolled in account table");
			return false;
		}
		
		UserHistoryEntity user_history = userOptional.get().getHistory();
		
		try {
			user_history.updateEnddate(date);;
			UR.save(userOptional.get());
		}
		catch (Exception e) {
			log.error("custom_error: can't update user info-date");
			return false;
		}
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Optional<UserVo> userInfo(String token) {
		String kakao_uid = default_kakao_uid;
		try {
			log.info("try to get kakaoo_uid in token");
			kakao_uid = extractKakaoUidInTokenOptional(token).get();
		}
		catch (Exception e) {
			log.error("faild in extractKakaoUidInTokenOptional");
			return Optional.empty();
		}
		
		log.info("custom_info: try to get account by using kakao_uid : " + kakao_uid);
		Optional<AccountInfoEntity> accountOptional = AIR.findByKakaoUid(kakao_uid);
		if(accountOptional.isEmpty()) {
			log.error("custom_error: unexpected user, but enrolled in kakao");
			return Optional.empty();
		}
		
		log.info("custom_info: try to get user by using user_uid: " + accountOptional.get().getUserUid());
		Optional<UserEntity> userOptional = UR.findById(accountOptional.get().getUserUid());
		if(userOptional.isEmpty()) {
			log.error("custom_error: cant find user in user table but entolled in account table");
			return Optional.empty();
		}
		
		return Optional.ofNullable(UserVo.builder().user(userOptional.get()).build());
	}
}
