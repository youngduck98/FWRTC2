package com.example.demo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.DateDTO;
import com.example.demo.dto.UserVo;
import com.example.demo.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/user", produces = {MediaType.APPLICATION_JSON_VALUE})
public class UserController {
	@Autowired
	private UserService userService;
	private static final String backdoor = "0000"; 
	private final static String default_uid = "fake_user";
	
	@PutMapping("/company")
	public Map<String, Object> changeCompany(@RequestBody HashMap<String, String> map, @RequestHeader(value="firebase_token") String token){
		Map<String, Object> result = new HashMap<>();
		result.put("output", userService.change_user_company(token, map.get("company_name")));
		return result;
	}
	
	@PutMapping("/nickname")
	public Map<String, Object> changeNickname(@RequestBody HashMap<String, String> map, @RequestHeader(value="firebase_token") String token) {
		//나중에 filter로 넘어갈 내용.
		Map<String, Object> result = new HashMap();
		result.put("output", userService.changeUserNickname(token, map.get("nickname")));
		return result;
	}
	
	@PutMapping("/enddate")
	public Map<String, Object> changeEnddate(@RequestBody DateDTO date, @RequestHeader(value="firebase_token") String token){
		Map<String, Object> result = new HashMap();
		result.put("output", userService.changeUserEnddate(token, date));
		return result;
	}
	
	@GetMapping("/info")
	public UserVo getUserInfo(@RequestHeader(value="firebase_token") String token){
		return userService.userInfo(token).get();
	}
	
	
}
