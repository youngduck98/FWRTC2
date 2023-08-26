package com.example.demo.service;

import java.util.List;

import com.example.demo.dto.LetterRequestDTO;
import com.example.demo.dto.TempLetterDTO;
import com.example.demo.dto.TempLetterRequestDTO;

public interface LetterService {
	//letter
	public Long saveLetter(String token, LetterRequestDTO letter);
	public Long saveReply(String token, LetterRequestDTO letter);
	
	//temp letter
	public Long changeTempLetter(String token, TempLetterRequestDTO temp_letter);
	public Long saveTempLetter(String token, TempLetterRequestDTO temp_letter);
	public List<TempLetterDTO> getTempList(String token);
	public TempLetterDTO getTempLetter(String token, long tempLetterUid);
}
