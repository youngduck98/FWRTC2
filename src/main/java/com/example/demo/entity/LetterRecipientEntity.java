package com.example.demo.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
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
@Table(name = "letter_recipient")
public class LetterRecipientEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "letter_recipientUid")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	long letterRecipientUid;
	
	@ManyToOne
	@JoinColumn(name = "recipient_uid", columnDefinition = "CHAR(36)")
	UserEntity recipient;
	
	@ManyToOne
	@JoinColumn(name = "sender_uid", columnDefinition = "CHAR(36)")
	UserEntity sender;
	
	@ManyToOne
	@JoinColumn(name = "letter_uid")
	LetterEntity letter;
	
	@OneToOne
	@JoinColumn(name = "reply_letter_uid")
	LetterEntity reply;
	
	@ManyToOne
	@JoinColumn(name = "history_uid")
	UserHistoryEntity history;
	
	@Column(name = "datetime")
	LocalDateTime replyTime;
	
	@PrePersist
	public void setdate() {
		this.replyTime = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
	}
}
