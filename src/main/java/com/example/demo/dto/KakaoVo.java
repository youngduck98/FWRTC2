package com.example.demo.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class KakaoVo {
	private String uid;
    private String email;
    private String nickname;
    private String img_path;
}