package com.example.demo.dto;

import static org.hamcrest.CoreMatchers.nullValue;

import java.time.LocalDate;

import com.example.demo.entity.UserEntity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserVo {
	String user_uid;
	String nickname;
	String profileImg;
	String company;
	LocalDate enddate;
	
	@Builder
	public UserVo(UserEntity user) {
		this.user_uid = user.getUid();
		this.nickname = user.getNickname();
		this.profileImg = user.getProfile_img_path();
		this.company = user.getHistory().getUid();
		if(user.getHistory().getVaild())
			this.enddate = user.getHistory().getDate();
		else
			this.enddate = null;
	}
}
