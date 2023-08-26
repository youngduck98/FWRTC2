package com.example.demo.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TempLetterDTO {
	long Temp_letter_uid;
	String title;
	String content;
	long letter_design_uid;
	String color_code;
	Long related_letter_uid;
}
