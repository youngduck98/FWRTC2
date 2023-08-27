package com.example.demo.service;

import java.util.List;

import com.example.demo.dto.AbstractLetterDTO;
import com.example.demo.dto.LetterAndUserDTO;
import com.example.demo.dto.LetterDTO;
import com.example.demo.dto.LetterRequestDTO;
import com.example.demo.dto.TempLetterDTO;
import com.example.demo.dto.TempLetterRequestDTO;
import com.example.demo.dto.talkRelatedDTO.CompanyDTO;

public interface LetterService {
	//letter
	public Long saveLetter(String token, LetterRequestDTO letter);
	public Long saveReply(String token, LetterRequestDTO letter);
	public LetterDTO getOneLetter(String token, long letterUid);
	
	//temp letter
	public Long changeTempLetter(String token, TempLetterRequestDTO temp_letter);
	public Long saveTempLetter(String token, TempLetterRequestDTO temp_letter);
	public List<TempLetterDTO> getTempList(String token);
	public TempLetterDTO getTempLetter(String token, long tempLetterUid);
	
	//design
	public List<String> getColor(String token);
	
	//find letter(criteria = user)
	public List<AbstractLetterDTO> getLetterAndSender(String token);
	public List<LetterAndUserDTO> getLetterAndSenderUid(String token);
	
	//about talk
	public List<CompanyDTO> getTalk(String token);
}
