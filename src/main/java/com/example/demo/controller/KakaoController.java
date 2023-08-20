package com.example.demo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.KakaoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/kakao", produces = {MediaType.APPLICATION_JSON_VALUE})
public class KakaoController {
	
	@Autowired
	private KakaoService ks;
	
	@PostMapping("/login")
	public  Map<String, Object>kakaoFirebaseLogin(@RequestBody Map<String, String> param) {
		Map<String, Object> result = new HashMap();
		result.put("firebase_custom_token", ks.KaKaoFirebaseLoginProcess(param.get("access_token")));
		return result;
	}
}
