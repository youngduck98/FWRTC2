package com.example.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.LetterDTO;
import com.example.demo.dto.LetterRequestDTO;
import com.example.demo.dto.LetterSaveDTO;
import com.example.demo.dto.talkRelatedDTO.CompanyDTO;
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
	public LetterSaveDTO changeCompany(@RequestBody LetterRequestDTO letter, @RequestHeader(value="firebase_token") String token){
		LetterSaveDTO ret = new LetterSaveDTO();
		log.info(letter.toString());
		if(letter.getRelated_letter_uid() == null)
			ret = ls.saveLetter(token, letter);
		else {
			ret = ls.saveReply(token, letter);
		}
		return ret;
	}
	
	@GetMapping("/design")
	public List<String> get_color(@RequestHeader(value="firebase_token") String token){
		return ls.getColor(token);
	}
	
	@GetMapping("/get_one")
	public LetterDTO getOne(@RequestHeader(value="firebase_token") String token,
			@RequestParam("letter_uid") long letterUid) {
		LetterDTO ret = ls.getOneLetter(token, letterUid);
		return ret;
	}
	
	@GetMapping("/open_talk")
	public List<CompanyDTO> get_talk(@RequestHeader(value="firebase_token") String token){
		return ls.getTalk(token);
	}
}
