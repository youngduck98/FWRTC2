package com.example.demo.service.serviceImpl;

import static org.hamcrest.CoreMatchers.nullValue;

import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.example.demo.dto.LetterRequestDTO;
import com.example.demo.entity.LetterEntity;
import com.example.demo.entity.LetterRecipientEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.repository.AccountInfoRepositoy;
import com.example.demo.repository.LetterRecipientRepository;
import com.example.demo.repository.LetterRepository;
import com.example.demo.repository.UserHistoryRepository;
import com.example.demo.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class LetterAditionalTransaction {
	
	private final UserHistoryRepository UHR;
	private final UserRepository UR;
	private final AccountInfoRepositoy AIR;
	private final LetterRepository LR;
	private final LetterRecipientRepository LRR;
	private static final String backdoor = "0000";
	private final static String default_kakao_uid = "fake_user";
	
	@Transactional(rollbackOn = Exception.class)
	public Long saveReplyTransaction(UserEntity user, LetterRequestDTO letter){
		log.info("3. try to save reply to database");
		LetterEntity letterEntity = LetterEntity.builder()
				.designUid(letter.getLetter_design_uid())
				.user(user)
				.colorcode(letter.getColorcode())
				.title(letter.getTitle())
				.text(letter.getContent())
				.reply(true)
				.build();
		
		if(letterEntity.getUid() == Long.valueOf(letter.getLetter_design_uid())) {
			return null;
		}
		LR.save(letterEntity);
		
		log.info("4. try to relate letter and reply");
		Optional<LetterEntity> relatedLetter = LR.findById(letter.getRelated_letter_uid());
		LRR.save(LetterRecipientEntity.builder()
				.sender(relatedLetter.get().getUser())
				.recipient(user)
				.letter(relatedLetter.get())
				.reply(letterEntity)
				.build());
		return letterEntity.getUid();
	}
}
