package com.example.demo.service.serviceImpl;

import static org.hamcrest.CoreMatchers.nullValue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.attoparser.config.ParseConfiguration.UniqueRootElementPresence;
import org.springframework.data.repository.init.UnmarshallingResourceReader;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.example.demo.dto.LetterRequestDTO;
import com.example.demo.dto.talkRelatedDTO.Talk;
import com.example.demo.dto.talkRelatedDTO.TalkTalk;
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
	private final TempLetterRepository TLR;
	private static final String backdoor = "0000";
	private final static String default_kakao_uid = "fake_user";
	
	//start
	public Talk returnTalkFromLetter(LetterEntity letter, UserEntity user) {
		return Talk.builder()
				.title(letter.getTitle())//4
				.content(letter.getText())//4
				.letter_uid(letter.getUid())
				.profile_img_url(user.getProfile_img_path())
				.nickname(user.getNickname())
				.build();
	}
	
	public TalkTalk ReturnTalkTalkFromLetter(LetterEntity letter, UserEntity user) {
		List<LetterRecipientEntity> letterRecipientEntities = LRR.findAllByLetter(letter);
		List<Talk> otherTalks = new ArrayList<>();
		
		for(LetterRecipientEntity letterRecipient: letterRecipientEntities) {
			otherTalks.add(returnTalkFromLetter(letterRecipient.getReply(), user));
		}
		
		return TalkTalk.builder()
				.myTalk(returnTalkFromLetter(letter, user))
				.relatedTalk(otherTalks)
				.build();
	}
	
	public List<TalkTalk> ReturnTalkFromHistory(UserHistoryEntity history, UserEntity user) {
		List<TalkTalk> ret = new ArrayList<>();
		
		List<LetterEntity> letterEntities = LR.findByUserAndHistory(user, history);
		/*
		List<LetterRecipientEntity> letterRecipientEntities = LRR.findAllByHistory(history);//history가 letter 기준으로 update 되던가? ㅇㅇ
		Set<LetterEntity> letterSet = new HashSet<LetterEntity>();
		List<TalkTalk> ret = new ArrayList<>();
		
		for(LetterRecipientEntity LRE: letterRecipientEntities) {
			letterSet.add(LRE.getLetter());
		}
		
		log.error("customInfo: " + letterSet.size());
		
		for(LetterEntity letterUid: letterSet) {
			ret.add(ReturnTalkTalkFromLetter(letterUid, user));
		}
		*/
		for(LetterEntity letterUid: letterEntities) {
			ret.add(ReturnTalkTalkFromLetter(letterUid, user));
		}
		
		return ret;
	}
	
	@Transactional(rollbackOn = Exception.class)
	public Long saveReplyTransaction(UserEntity user, LetterRequestDTO letter){
		log.info("3. try to save reply to database");
		LetterEntity letterEntity = LetterEntity.builder()
				.designUid(letter.getLetter_design_uid())
				.user(user)
				.history(user.getHistory())
				.colorcode(letter.getColorcode())
				.title(letter.getTitle())
				.text(letter.getContent())
				.reply(true)
				.build();
		
		List<TempLetterEntity> letterEntities = TLR.findByRelatedUidAndUser(letter.getRelated_letter_uid(), user);
		for(TempLetterEntity tempLetterEntity : letterEntities) {
			TLR.delete(tempLetterEntity);
		}
		
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
				.history(relatedLetter.get().getUser().getHistory())
				.build());
		return letterEntity.getUid();
	}
}
