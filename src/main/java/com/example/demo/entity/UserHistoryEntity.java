package com.example.demo.entity;

import java.time.LocalDate;

import com.example.demo.dto.DateDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter // getter 메소드 생성
@Builder // 빌더를 사용할 수 있게 함
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "user_history")
public class UserHistoryEntity {
	@Id
	@Column(name = "history_uid")
	String uid;
	
	@Column(name = "company_name")
	String companyName;
	
	@Column(name = "end_date")
	LocalDate date;
	
	@Column(name = "user_uid")
	String userUid;
	
	@Column(name = "vaild", columnDefinition = "TINYINT")
	boolean vaild;
	
	@PrePersist//PreUpdate
	protected void onCreate() {
		this.date = LocalDate.now();
		this.vaild = false;
	}
	
	public void updateEnddate(DateDTO date) {
		this.date = date.getDate();
		this.vaild = date.getVaild();
	}
	
	public void updateCompanyName(String companyName) {
		this.companyName = companyName;
	}
	
	public boolean getVaild() {
		return this.vaild;
	}
}
