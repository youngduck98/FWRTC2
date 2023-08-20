package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
@Table(name = "account_info")
public class AccountInfoEntity {
	@Column(name = "kakao_uid", columnDefinition="CHAR(36)")
	String kakaoUid;
	
	@Id
	@Column(name = "user_uid", unique=true, columnDefinition = "CHAR(36)")
	String userUid;
}
