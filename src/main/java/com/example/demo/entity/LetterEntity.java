package com.example.demo.entity;

import java.time.LocalDate;

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
	
	@Column(name = "colorcode", columnDefinition = "CHAR(36)")
	String colorcode;
	
	@Column(name = "title")
	String title;
	
	@Column(name = "text")
	String text;
	
	@Column(name = "reply", columnDefinition = "TINYINT")
	Boolean reply;
}
