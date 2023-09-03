package com.example.demo.entity;

import java.time.LocalDate;

import com.example.demo.dto.LetterDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "letter")
public class LetterEntity {
	@Id
	@Column(name = "letter_uid")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	long uid;
	
	@Column(name = "desing_uid")
	long designUid;
	
	@ManyToOne
	@JoinColumn(name = "user_uid")
	UserEntity user;
	
	@ManyToOne
	@JoinColumn(name = "history_uid")
	UserHistoryEntity history;
	
	@Column(name = "colorcode", columnDefinition = "CHAR(36)")
	String colorcode;
	
	@Column(name = "title")
	String title;
	
	@Column(name = "text")
	String text;
	
	@Column(name = "reply", columnDefinition = "TINYINT")
	Boolean reply;
	
	public LetterDTO getLetterDTO() {
		LetterDTO letter = new LetterDTO();
		letter.setSender_uid(this.user.getUid());//1
		letter.setColor_code(this.colorcode);
		letter.setContent(this.text);
		letter.setLetter_design_uid(this.designUid);
		letter.setReply(this.reply);
		letter.setTitle(this.title);
		//letter.setLetter_uid(this.uid);//7
		
		return letter;
	}
}
