package com.example.demo.dto.talkRelatedDTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Talk {
	String talk_title;
	String talk_text;
	long letter_uid;
	String profile_img_url;
	String nickname;
}
