package com.example.demo.dto.talkRelatedDTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Talk {
	String title;
	String content;
	long letter_uid;
	String profile_img_url;
	String nickname;
}
