package com.example.demo.service;

import java.util.Optional;

import com.example.demo.dto.DateDTO;
import com.example.demo.dto.UserVo;

public interface UserService {
	public String change_user_company(String user_uid, String company_name);
	public boolean changeUserNickname(String token, String user_nickname);
	public boolean changeUserEnddate(String token, DateDTO date);
	public Optional<UserVo> userInfo(String token);
}
