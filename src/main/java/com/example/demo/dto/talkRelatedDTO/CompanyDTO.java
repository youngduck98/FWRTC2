package com.example.demo.dto.talkRelatedDTO;

import java.time.LocalDate;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CompanyDTO {
	String company_name;
	String company_uid;
	String companyImg;
	LocalDate endDate;
	String recent_text;
	List<TalkTalk> talkList;
}
