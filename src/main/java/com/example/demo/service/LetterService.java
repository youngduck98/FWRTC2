package com.example.demo.service;

import com.example.demo.dto.LetterDTO;
import com.example.demo.dto.LetterRequestDTO;

public interface LetterService {
	public Long saveLetter(String token, LetterRequestDTO letter);
	public Long saveReply(String token, LetterRequestDTO letter);
}
