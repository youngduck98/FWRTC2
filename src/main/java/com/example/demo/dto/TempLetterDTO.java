package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

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
	@JsonProperty("colorcode")
	String color_code;
	Long related_letter_uid; 
}
