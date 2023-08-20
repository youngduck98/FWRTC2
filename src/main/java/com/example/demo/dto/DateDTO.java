package com.example.demo.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class DateDTO {
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate date;
	
	private boolean vaild;
	
	public boolean getVaild() {
		return vaild;
	}
}
