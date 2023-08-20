package com.example.demo.config;

import java.io.FileInputStream;
import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration 
public class FirebaseInitializer {
	private final static String ubuntu_server_url = "/home/ubuntu/FWLTC/src/main/java/com/example/demo/global/config/FWLTC_firebase_team_sdkkey.json";
	private final static String local_url = "C:/Users/98tom/Downloads/FWLTC_firebase_team_sdkkey.json";
	
    @Bean
    public void init() throws IOException {
    	log.info("firebase initialize");
    	
    	FileInputStream serviceAccount =
		new FileInputStream(local_url);

		FirebaseOptions options = FirebaseOptions.builder()
		  .setCredentials(GoogleCredentials.fromStream(serviceAccount))
		  .build();
		try {
			FirebaseApp.initializeApp(options);
		}
		catch(Exception e){
			
		}
    }
}