package com.example.demo.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LetterAndUserDTO {
	Long letter_uid;
	String user_uid;
}
