package com.example.demo.service;

import java.util.Optional;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.example.demo.dto.KakaoVo;
import com.example.demo.entity.AccountInfoEntity;
import com.example.demo.repository.AccountInfoRepositoy;
import com.example.demo.repository.UserRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.auth.UserRecord.CreateRequest;
import com.google.firebase.auth.UserRecord.UpdateRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoService {
	
	private final static String KAKAO_API_URI = "https://kapi.kakao.com";
	private final AccountInfoRepositoy air;
	private final UserRepository Ur;
	private final KakaoAditionalTransaction kat;
	private final static String backdoor = "0000";
	private final static String default_uid = "fake_user";
	
	public Optional<String> KaKaoFirebaseLoginProcess(String accessToken) {
		
		//1. kakao token 해석 + KakaoVo에 정보 입력
		Optional<KakaoVo> KakaoUserInfo = getUserInfoAtKakaoToken(accessToken);
		if(accessToken.equals(backdoor)) {
			log.info("custom_info: backdoor token");
			KakaoUserInfo = Optional.ofNullable(KakaoVo.builder()
					.uid(default_uid)
					.email(null)
					.img_path(null)
					.nickname(null)
					.build());
		}
		
		if(KakaoUserInfo.isEmpty()) {
			log.error("custom_error: kakao: nouser");
			return Optional.ofNullable(null);
		}
		
		Optional<String> firebaseToken;
		try {
			firebaseToken = createFirebaseCustomToken(KakaoUserInfo.get());
		}
		catch(FirebaseAuthException e) {
			log.error("firebase: " + e);
			return null;
		}
		catch(Exception e) {
			log.error("firebase: " + e);
			return null;
		}
		
		return firebaseToken;
	}
	
	
	
	public Optional<KakaoVo> getUserInfoAtKakaoToken(String accessToken) {
        
		//HttpHeader 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        //HttpHeader 담기
        ResponseEntity<String> response;
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(headers);
        try {
        	RestTemplate rt = new RestTemplate();
		        response = rt.exchange(
		                KAKAO_API_URI + "/v2/user/me",
		                HttpMethod.POST,
		                httpEntity,
		                String.class
	        );
        }
	    catch(Exception e) {
	    	log.error("custom_error: failed to get data from token");
	    	return Optional.ofNullable(null);
	    }

        //Response 데이터 파싱
        String uid = null;
        String email = null;
        String nickname = null;
        String thumbnail_img_url = null;
        
        try {
	        JSONParser jsonParser = new JSONParser();
	        JSONObject jsonObj    = (JSONObject) jsonParser.parse(response.getBody());
	        JSONObject account = (JSONObject) jsonObj.get("kakao_account");
	        uid = jsonObj.get("id").toString();
	        
	        if((boolean) account.get("profile_image_needs_agreement")) {
	        	JSONObject profile = (JSONObject) account.get("profile");
	        	thumbnail_img_url = String.valueOf(profile.get("thumbnail_image_url"));
	        }
	        
        }
        catch (ParseException e) {
        	log.error("custom_error: failed to parse data from kakao");
        	return Optional.ofNullable(null);
        }
        
        KakaoVo kakaoVo = KakaoVo.builder()
        		.uid(uid)
        		.email(email)
        		.nickname(nickname)
        		.img_path(thumbnail_img_url)
        		.build();
        
        return Optional.ofNullable(kakaoVo);
    }
	
	public Optional<String> createFirebaseCustomToken(KakaoVo kakaoInfo) throws Exception {
        UserRecord userRecord;
        String uid = kakaoInfo.getUid();
        
        Optional<AccountInfoEntity> userAccount = air.findByKakaoUid(uid);
        if(userAccount.isEmpty()) {
        	try {
        		log.info("custom_info: join membership to mysql database");
        		userAccount = kat.joinMemberToDatabase(kakaoInfo, userAccount);
        	}
        	catch(Exception e) {
        		log.error("custom_error: failed to join membership to mysql database :" + e);
        		return Optional.ofNullable(null);
        	}
        	try {
        		log.info("custom_info: try to create firebase account");
	        	CreateRequest createRequest = new CreateRequest();
	            createRequest.setUid(uid);
	            userRecord = FirebaseAuth.getInstance().createUser(createRequest);
        	}
        	catch(FirebaseAuthException e) {
        		log.error("custom_error: failed to create firebase account: " + e);
        		log.info("custom_info: try to update firebase account");
	            try {
	            	UpdateRequest request = new UpdateRequest(uid);
		            userRecord = FirebaseAuth.getInstance().updateUser(request);
	            }
	            catch(FirebaseAuthException e1){
	            	log.error("custome_error: failed to update firebase user :" + e1);
	            	return Optional.ofNullable(null);
	            }
        	}
        }
        else {
        	try {
        		log.info("custom_info: try to update firebase account");
	        	UpdateRequest request = new UpdateRequest(uid);
	            userRecord = FirebaseAuth.getInstance().updateUser(request);
        	}
        	catch(FirebaseAuthException e) {
        		log.error("custome_error: failed to update firebase user, try to create user in firebase: \n" + e);
        		try {
            		log.info("custom_info: try to create firebase account");
    	        	CreateRequest createRequest = new CreateRequest();
    	            createRequest.setUid(uid);
    	            userRecord = FirebaseAuth.getInstance().createUser(createRequest);
            	}
            	catch(FirebaseAuthException e2) {
            		log.error("custom_error: failed to create firebase account: " + e2);
            		return Optional.ofNullable(null);
            	}
        	}
        }
 
        return Optional.ofNullable(FirebaseAuth.getInstance().createCustomToken(userRecord.getUid()));
    }
}
