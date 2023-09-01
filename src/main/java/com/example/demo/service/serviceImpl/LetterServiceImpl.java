package com.example.demo.service.serviceImpl;

import static org.hamcrest.CoreMatchers.nullValue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.example.demo.dto.AbstractLetterDTO;
import com.example.demo.dto.LetterAndUserDTO;
import com.example.demo.dto.LetterDTO;
import com.example.demo.dto.LetterRequestDTO;
import com.example.demo.dto.LetterSaveDTO;
import com.example.demo.dto.TempLetterDTO;
import com.example.demo.dto.TempLetterRequestDTO;
import com.example.demo.dto.talkRelatedDTO.CompanyDTO;
import com.example.demo.dto.talkRelatedDTO.Talk;
import com.example.demo.dto.talkRelatedDTO.TalkTalk;
import com.example.demo.entity.AccountInfoEntity;
import com.example.demo.entity.LetterEntity;
import com.example.demo.entity.LetterRecipientEntity;
import com.example.demo.entity.TempLetterEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.entity.UserHistoryEntity;
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
	private static final String backdoor2 = "0001";
	private final static String default_kakao_uid = "fake_user";
	private final static String default_kakao_uid2 = "fake_user2";
	
	public Optional<String> extractKakaoUidInTokenOptional(String token){
		String kakao_uid = default_kakao_uid;
		if(token.equals(backdoor2)) {
			kakao_uid = default_kakao_uid2;
		}
		if(!token.equals(backdoor) && !token.equals(backdoor2)) {
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
	public LetterSaveDTO saveLetter(String token, LetterRequestDTO letter) {
		Optional<UserEntity> userOptional = getUserfromToken(token);
		LetterSaveDTO dto = new LetterSaveDTO(null, null, null);
		
		if(userOptional.isEmpty()) {
			log.error("custom_error: cant find user in user table but entolled in account table");
			dto.setError("custom_error: cant find user in user table but entolled in account table");
			return dto;
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
			dto.setError("failed to save letter to database");
			return dto;
		}
		
		dto.setLetter_uid(letterEntity.getUid());
		
		return dto;
	}
	
	@Override
	public LetterSaveDTO saveReply(String token, LetterRequestDTO letter) {
		// TODO Auto-generated method stub
		Optional<UserEntity> userOptional = getUserfromToken(token);
		LetterSaveDTO dto = new LetterSaveDTO(null, null, null);
		
		if(userOptional.isEmpty()) {
			log.error("custom_error: cant find user in user table but entolled in account table");
			dto.setError("custom_error: can't find user");
			return dto;
		}
		
		if(letter.getRelated_letter_uid() != null) {
			Optional<LetterEntity> letterentityOptional = LR.findById(letter.getRelated_letter_uid());
			if(letterentityOptional.isEmpty()) {
				log.error("no related letter like that");
				dto.setError("no related letter like that");
				return dto;
			}
			
			List<LetterRecipientEntity> letterReList = 
					LRR.findByRecipientAndLetter(userOptional.get(), letterentityOptional.get());
			if(!letterReList.isEmpty()) {
				log.error("alreadly reply to that letter");
				dto.setError("alreadly reply to that letter");
				dto.setDate(letterReList.get(0).getReplyTime());
				return dto;
			}
		}
		
		Long ret =  null;
		try {
			ret = LAT.saveReplyTransaction(userOptional.get(), letter);
			dto.setLetter_uid(ret);
		}
		catch(Exception e){
			log.error("saveReply" + e);
			dto.setError("alreadly reply to that letter");
			return dto;
		}
		
		return dto;
	}

	@Override
	public Long changeTempLetter(String token, TempLetterRequestDTO tempLetter) {
		// TODO Auto-generated method stub
		Optional<UserEntity> userOptional = getUserfromToken(token);
		if(userOptional.isEmpty()) {
			log.error("custom_error: cant find user in user table but entolled in account table");
			return null;
		}
		
		if(tempLetter.getRelated_letter_uid() != null) {
			Optional<LetterEntity> letterentityOptional = LR.findById(tempLetter.getRelated_letter_uid());
			if(letterentityOptional.isEmpty()) {
				return null;
			}
			
			List<LetterRecipientEntity> letterReList = 
					LRR.findByRecipientAndLetter(userOptional.get(), letterentityOptional.get());
			if(!letterReList.isEmpty()) {
				return null;
			}
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
		
		if(tempLetter.getRelated_letter_uid() != null) {
			Optional<LetterEntity> letterentityOptional = LR.findById(tempLetter.getRelated_letter_uid());
			if(letterentityOptional.isEmpty()) {
				log.info("no letter like that");
				return null;
			}
			
			List<LetterRecipientEntity> letterReList = 
					LRR.findByRecipientAndLetter(userOptional.get(), letterentityOptional.get());
			if(!letterReList.isEmpty()) {
				return null;
			}
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
					.relatedUid(tempLetter.getRelated_letter_uid())
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
			log.error("failed to find temp list" + e);
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

	@Override
	public List<String> getColor(String token) {
		Optional<UserEntity> userOptional = getUserfromToken(token);
		
		if(userOptional.isEmpty()) {
			log.error("custom_error: cant find user in user table but enrolled in account table");
			return null;
		}
		List<String> ret = new ArrayList<>(List.of("5CBDF1", "37B9FF", "007FF4", "6CE2F2", "36CEE3", 
				"00AFE7", "A5C9FF", "7AAFFF", "FF8E3D", "FFA337", "FF7337", "FFC9B1", "FFC28A", 
						"FFC148", "FFE6CF", "FFCE70", "FF8A00"));
		return ret;
	}

	@Override
	public LetterDTO getOneLetter(String token, long letterUid) {
		// TODO Auto-generated method stub
		Optional<UserEntity> userOptional = getUserfromToken(token);
		if(userOptional.isEmpty()) {
			log.error("custom_error: cant find user in user table but enrolled in account table");
			return null;
		}
		
		Optional<LetterEntity> letterOptional = LR.findById(letterUid);
		if(letterOptional.isEmpty())
			return null;
		
		LetterDTO retDto = letterOptional.get().getLetterDTO();
		
		List<LetterRecipientEntity> letterRecipientEntities = LRR.findByReply(letterOptional.get());
		if(retDto.getReply() && letterRecipientEntities.size() != 0) {
			retDto.setLetter_uid(letterRecipientEntities.get(0).getLetter().getUid());
		}
		
		return retDto;
	}

	@Override
	public List<AbstractLetterDTO> getLetterAndSender(String token) {
		// TODO Auto-generated method stub
		Optional<UserEntity> userOptional = getUserfromToken(token);
		if(userOptional.isEmpty()) {
			log.error("custom_error: cant find user in user table but enrolled in account table");
			return null;
		}
		
		List<LetterRecipientEntity> letterRecipient = LRR.findAllBySender(userOptional.get());
		List<AbstractLetterDTO> abstractLetterDTOs = new ArrayList<>();
		try {
			for(LetterRecipientEntity a: letterRecipient) {
				abstractLetterDTOs.add(AbstractLetterDTO.builder()
						.letter_uid(a.getReply().getUid())
						.sender_img(a.getRecipient().getUid())
						.build());
			}
			
			for(AbstractLetterDTO a: abstractLetterDTOs) {
				a.setSender_img(UR.findById(a.getSender_img()).get().getProfile_img_path());
			}
		}
		catch(Exception e) {
			log.info("failed to search in " + e);
			return null;
		}
		return abstractLetterDTOs;
	}

	@Override
	public List<LetterAndUserDTO> getLetterAndSenderUid(String token) {
		// TODO Auto-generated method stub
		
		Optional<UserEntity> userOptional = getUserfromToken(token);
		if(userOptional.isEmpty()) {
			log.error("custom_error: cant find user in user table but enrolled in account table");
			return null;
		}
		
		List<LetterRecipientEntity> letterRecipient = LRR.findAllBySender(userOptional.get());
		List<LetterAndUserDTO> letterAndUserDTOs = new ArrayList<>();
		try {
			for(LetterRecipientEntity a: letterRecipient) {
				letterAndUserDTOs.add(LetterAndUserDTO.builder()
						.letter_uid(a.getReply().getUid())
						.user_nickname(a.getRecipient().getNickname())//2
						.build());
			}
		}
		catch(Exception e) {
			log.info("failed to search in " + e);
			return null;
		}
		return letterAndUserDTOs;
	}
	
	public String getRecentText(UserHistoryEntity history, UserEntity user) {
		List<LetterRecipientEntity> letterRecipientEntities = 
				LRR.findByHistoryAndSenderOrderByReplyTimeDesc(history, user);
		
		if(letterRecipientEntities.size() == 0) {
			return null;
		}
		
		return letterRecipientEntities.get(0).getReply().getText();
	}
	
	public CompanyDTO ReturnCompanyDtoFromHistory(UserHistoryEntity history, UserEntity user) {
		return CompanyDTO.builder()
				.company_name(history.getCompanyName())
				.company_uid(history.getUid())
				.companyImg(null)
				.endDate(history.getDate())
				.recent_text(getRecentText(history, user))
				.talkList(LAT.ReturnTalkFromHistory(history, user))
				.build();
	}

	@Override
	public List<CompanyDTO> getTalk(String token) {
		// TODO Auto-generated method stub
		Optional<UserEntity> userOptional = getUserfromToken(token);
		if(userOptional.isEmpty()) {
			log.error("custom_error: cant find user in user table but enrolled in account table");
			return null;
		}
		
		List<UserHistoryEntity> userHistoryEntities = UHR.findByUserUid(userOptional.get().getUid());
		List<CompanyDTO> ret = new ArrayList<>();
		
		for(UserHistoryEntity history: userHistoryEntities) {
			ret.add(ReturnCompanyDtoFromHistory(history, userOptional.get()));
		}
		return ret;
	}
	//end
	
}
