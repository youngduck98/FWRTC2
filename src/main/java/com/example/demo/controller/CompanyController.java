package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.LetterAndUserDTO;
import com.example.demo.dto.UserDTO;
import com.example.demo.service.LetterService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/company", produces = {MediaType.APPLICATION_JSON_VALUE})
public class CompanyController {
	@Autowired
	LetterService ls;
	
	@GetMapping("/send_persons")
	public List<LetterAndUserDTO> getPeopleWhoSend(@RequestHeader(value="firebase_token") String token ){
		return ls.getLetterAndSenderUid(token);
	}
}
