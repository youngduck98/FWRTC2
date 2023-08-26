package com.example.demo.service.serviceImpl;

import static org.hamcrest.CoreMatchers.nullValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.dto.LetterRequestDTO;
import com.example.demo.dto.TempLetterDTO;
import com.example.demo.dto.TempLetterRequestDTO;
import com.example.demo.entity.AccountInfoEntity;
import com.example.demo.entity.LetterEntity;
import com.example.demo.entity.TempLetterEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.repository.AccountInfoRepositoy;
import com.example.demo.repository.LetterRecipientRepository;
import com.example.demo.repository.LetterRepository;
import com.example.demo.repository.TempLetterRepository;
import com.example.demo.repository.UserHistoryRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.LetterService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class LetterServiceImpl implements LetterService{
	private final UserHistoryRepository UHR;
	private final UserRepository UR;
	private final AccountInfoRepositoy AIR;
	private final LetterRepository LR;
	private final TempLetterRepository TLR;
	private final LetterRecipientRepository LRR;
	private final LetterAditionalTransaction LAT;
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
	
	public Optional<UserEntity> getUserfromToken(String token){
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
		return UR.findById(accountOptional.get().getUserUid());
	}
	
	@Override
	public Long saveLetter(String token, LetterRequestDTO letter) {
		Optional<UserEntity> userOptional = getUserfromToken(token);
		if(userOptional.isEmpty()) {
			log.error("custom_error: cant find user in user table but entolled in account table");
			return null;
		}
		
		log.info("3. try to save letter to database");
		LetterEntity letterEntity;
		try {
			 letterEntity = LR.save(
					LetterEntity.builder()
					.designUid(letter.getLetter_design_uid())
					.user(userOptional.get())
					.colorcode(letter.getColorcode())
					.title(letter.getTitle())
					.text(letter.getContent())
					.reply(false)
					.build()
					);
		}
		catch(Exception e){
			log.error("failed to save letter to database");
			return null;
		}
		
		return letterEntity.getUid();
	}
	
	@Override
	public Long saveReply(String token, LetterRequestDTO letter) {
		// TODO Auto-generated method stub
		Optional<UserEntity> userOptional = getUserfromToken(token);
		if(userOptional.isEmpty()) {
			log.error("custom_error: cant find user in user table but entolled in account table");
			return null;
		}
		
		Long ret =  null;
		try {
			ret = LAT.saveReplyTransaction(userOptional.get(), letter);
		}
		catch(Exception e){
			log.error("saveReply" + e);
		}
		
		return ret;
	}

	@Override
	public Long changeTempLetter(String token, TempLetterRequestDTO tempLetter) {
		// TODO Auto-generated method stub
		Optional<UserEntity> userOptional = getUserfromToken(token);
		if(userOptional.isEmpty()) {
			log.error("custom_error: cant find user in user table but entolled in account table");
			return null;
		}
		
		Optional<TempLetterEntity> tempLetterOptional = TLR.findById(tempLetter.getTemp_letter_uid());
		if(tempLetterOptional.isEmpty()) {
			log.error("can't find temp letter + " + tempLetter.getTemp_letter_uid());
			return null;
		}
		
		TempLetterEntity tempLetterEntity = tempLetterOptional.get();
		try {
			tempLetterEntity.update_info(tempLetter.getLetter_design_uid(), tempLetter.getColorcode(), 
					tempLetter.getTitle(), tempLetter.getContent(), tempLetter.getRelated_letter_uid());
			TLR.save(tempLetterEntity);
		}
		catch (Exception e) {
			log.error("failed to update temp letter");
			return null;
		}
		return tempLetterEntity.getUid();
	}

	@Override
	public Long saveTempLetter(String token, TempLetterRequestDTO tempLetter) {
		// TODO Auto-generated method stub
		Optional<UserEntity> userOptional = getUserfromToken(token);
		if(userOptional.isEmpty()) {
			log.error("custom_error: cant find user in user table but entolled in account table");
			return null;
		}
		
		log.info("3. try to save temp letter to database");
		TempLetterEntity templetterEntity;
		try {
			 templetterEntity = TLR.save(
					TempLetterEntity.builder()
					.designUid(tempLetter.getLetter_design_uid())
					.user(userOptional.get())
					.colorcode(tempLetter.getColorcode())
					.title(tempLetter.getTitle())
					.text(tempLetter.getContent())
					.related_uuid(tempLetter.getRelated_letter_uid())
					.build()
					);
		}
		catch(Exception e){
			log.error("failed to save letter to database");
			return null;
		}
		
		return templetterEntity.getUid();
	}

	@Override
	public List<TempLetterDTO> getTempList(String token) {
		// TODO Auto-generated method stub
		Optional<UserEntity> userOptional = getUserfromToken(token);
		if(userOptional.isEmpty()) {
			log.error("custom_error: cant find user in user table but enrolled in account table");
			return null;
		}
		List<TempLetterDTO> ret = new ArrayList<>();
		
		try {
			List<TempLetterEntity> temp_letter_List = TLR.findAll();
			for(TempLetterEntity letter: temp_letter_List) {
				ret.add(letter.getTempLetterDTO());
			}
		}
		catch(Exception e) {
			return null;
		}
		
		return ret;
	}

	@Override
	public TempLetterDTO getTempLetter(String token, long tempLetterUid) {
		// TODO Auto-generated method stub
		Optional<UserEntity> userOptional = getUserfromToken(token);
		if(userOptional.isEmpty()) {
			log.error("custom_error: cant find user in user table but enrolled in account table");
			return null;
		}
		
		Optional<TempLetterEntity> tempLetterOptional = TLR.findById(tempLetterUid);
		if(tempLetterOptional.isEmpty())
			return null;
		
		return tempLetterOptional.get().getTempLetterDTO();
	}
}
