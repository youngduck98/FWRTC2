package com.example.demo.entity;

import com.example.demo.dto.TempLetterDTO;

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
@Table(name = "temp_letter")
public class TempLetterEntity {
	@Id
	@Column(name = "letter_uid")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	long uid;
	
	@Column(name = "desing_uid")
	long designUid;
	
	@ManyToOne
	@JoinColumn(name = "user_uid")
	UserEntity user;
	
	@Column(name = "colorcode", columnDefinition = "CHAR(36)")
	String colorcode;
	
	@Column(name = "title")
	String title;
	
	@Column(name = "text")
	String text;
	
	@Column(name = "related_uuid")
	Long related_uuid;
	
	public void update_info(long designUid, String colorcode, String title, String text, Long related_uuid) {
		this.designUid = designUid;
		this.colorcode = colorcode;
		this.title = title;
		this.text = text;
		this.related_uuid = related_uuid;
	}
	
	public TempLetterDTO getTempLetterDTO() {
		TempLetterDTO temp_Letter = new TempLetterDTO();
		temp_Letter.setColor_code(this.colorcode);
		temp_Letter.setContent(this.text);
		temp_Letter.setLetter_design_uid(this.designUid);
		temp_Letter.setRelated_letter_uid(this.related_uuid);
		temp_Letter.setTemp_letter_uid(this.uid);
		temp_Letter.setTitle(this.title);
		if(temp_Letter.getRelated_letter_uid().longValue() == 0) {
			temp_Letter.setRelated_letter_uid(null);
		}
		return temp_Letter;
	}
}
