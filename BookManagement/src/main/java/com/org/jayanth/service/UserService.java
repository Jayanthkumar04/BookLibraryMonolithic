package com.org.jayanth.service;

import java.util.List;

import com.org.jayanth.dto.UserDto;
import com.org.jayanth.dto.UserUpdateDto;
import com.org.jayanth.dtobestprac.ForgotPasswordResponseDto;
import com.org.jayanth.dtobestprac.MessageDto;
import com.org.jayanth.dtobestprac.RegistrationSuccessfullResponseDto;
import com.org.jayanth.dtobestprac.UserInfoDto;
import com.org.jayanth.entity.User;

public interface UserService {
    
//	User login(String email,String password);
	RegistrationSuccessfullResponseDto register(UserDto userDto);
//	void resetPassword(String email,String newPassword);
	MessageDto updatePassword(Long userId,String oldPassword,String newPassword);
	User findByEmail(String email);
	UserInfoDto getUserById(Long userId);
	UserInfoDto updateUser(Long userId,UserUpdateDto dto);
	
	MessageDto deleteUser(Long userId);
	List<UserInfoDto> listAllUsers();
	
	boolean existsByEmail(String email);
	
	MessageDto assignRole(Long userId,String role);
	
	boolean validateTempPassword(String email, String password);
	MessageDto updateRole(Long id, String role);
	
	
	public ForgotPasswordResponseDto forgotPassword(String email);
	
	public void resetPassword(String token,String newPassword);
	
	
}
