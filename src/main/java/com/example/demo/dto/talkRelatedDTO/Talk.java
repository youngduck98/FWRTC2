package com.example.demo.dto.talkRelatedDTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Talk {
	String title;///4
	String content;//4
	long letter_uid;
	String profile_img_url;
	String nickname;
}
