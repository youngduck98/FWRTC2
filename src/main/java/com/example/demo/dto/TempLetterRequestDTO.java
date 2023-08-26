package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TempLetterRequestDTO {
	String title;
	String content;
	long letter_design_uid;
	String colorcode;
	
	@JsonProperty("related_letter_uid")
	Long related_letter_uid;
	
	@JsonProperty("temp_letter_uid")
	Long temp_letter_uid;
}
