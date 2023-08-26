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

import com.example.demo.dto.TempLetterDTO;
import com.example.demo.dto.TempLetterRequestDTO;
import com.example.demo.service.LetterService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/temp_letter", produces = {MediaType.APPLICATION_JSON_VALUE})
public class TempLetterController {
	@Autowired
	LetterService ls;
	
	@PostMapping("/store")
	public Map<String, Object> TempLetterSave(@RequestBody TempLetterRequestDTO letter, @RequestHeader(value="firebase_token") String token){
		log.info("TempLetterSave");
		Map<String, Object> result = new HashMap<>();
		log.info(letter.toString());
		if(letter.getTemp_letter_uid() != null) {
			log.info("changeTempLetter");
			result.put("output", ls.changeTempLetter(token, letter));
		}
		else {
			log.info("savetempletter" + letter.getTemp_letter_uid());
			result.put("output", ls.saveTempLetter(token, letter));
		}
		return result;
	}
	
	@GetMapping("/temp_letter_list")
	public List<TempLetterDTO> getTempLetterList(@RequestHeader(value="firebase_token") String token){
		return ls.getTempList(token);
	}
	
	@GetMapping("/get_temp_letter")
	public TempLetterDTO geTempLetterDTO(@RequestHeader(value="firebase_token") String token,
			@RequestParam("temp_letter_uid") long tempUid) {
		return ls.getTempLetter(token, tempUid);
	}
}
