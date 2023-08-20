package com.example.demo.entity;

import java.io.Serializable;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
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
@Table(name = "user")
public class UserEntity implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@JoinColumn(name = "user_uid", columnDefinition = "CHAR(36)")
	private String uid;
	
	@MapsId
	@OneToOne
	@JoinColumn(name = "user_uid")
	private AccountInfoEntity uid1;
	
	@Column(name = "nickname", columnDefinition = "CHAR(150)")
	private String nickname;
	
	@Column(name = "profile_img_path", length = 150)
	private String profile_img_path;
	
	@ManyToOne
	@JoinColumn(name = "history_uid")
	private UserHistoryEntity history;
	
	public void updateCompanyuid(UserHistoryEntity history) {
		this.history = history;
	}
	
	public void updateNickname(String nickname) {
		this.nickname = nickname;
	}
}
