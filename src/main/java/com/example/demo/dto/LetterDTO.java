package com.example.demo.dto;

import java.io.File;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.cloud.ByteArray;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LetterDTO {
	Long letter_uid;
	String sender_uid;//1
	String title;
	String content;
	long letter_design_uid;
	@JsonProperty("colorcode")
	String color_code;
	Long related_letter_uid;//7
	boolean reply;
	
	public boolean getReply() {
		return this.reply;
	}
}
