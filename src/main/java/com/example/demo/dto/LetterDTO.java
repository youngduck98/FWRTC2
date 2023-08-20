package com.example.demo.dto;

import java.io.File;

import com.google.cloud.ByteArray;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LetterDTO {
	String letter_uid;
	String title;
	String content;
	long letter_design_uid;
	String color_code;
	boolean reply;
}
