package com.example.demo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.LetterRequestDTO;
import com.example.demo.service.LetterService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/letter", produces = {MediaType.APPLICATION_JSON_VALUE})
public class LetterController {
	@Autowired
	LetterService ls;
	
	@PostMapping("/store")
	public Map<String, Object> changeCompany(@RequestBody LetterRequestDTO letter, @RequestHeader(value="firebase_token") String token){
		Map<String, Object> result = new HashMap<>();
		log.info(letter.toString());
		if(letter.getRelated_letter_uid() == null)
			result.put("output", ls.saveLetter(token, letter));
		else {
			result.put("output", ls.saveReply(token, letter));
		}
		return result;
	}
}