package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.AbstractLetterDTO;
import com.example.demo.service.LetterService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/main", produces = {MediaType.APPLICATION_JSON_VALUE})
public class MainpageController {
	@Autowired
	LetterService ls;
	
	@GetMapping("/upper")
	public List<AbstractLetterDTO> get_main_upper(@RequestHeader(value="firebase_token") String token){
		return ls.getLetterAndSender(token);
	}
}
