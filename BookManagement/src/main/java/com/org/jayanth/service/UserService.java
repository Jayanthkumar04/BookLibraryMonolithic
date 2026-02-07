package com.org.jayanth.service;

import java.util.List;

import com.org.jayanth.dto.GenericSucessResponseDto;
import com.org.jayanth.dto.RegisterUserDto;
import com.org.jayanth.dto.UserUpdateDto;
import com.org.jayanth.dtobestprac.ForgotPasswordResponseDto;
import com.org.jayanth.dtobestprac.MessageDto;
import com.org.jayanth.dtobestprac.RegistrationSuccessfullResponseDto;
import com.org.jayanth.dtobestprac.UserInfoDto;
import com.org.jayanth.entity.User;

public interface UserService {
    
	RegistrationSuccessfullResponseDto register(RegisterUserDto userDto);
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
	
	public GenericSucessResponseDto resetPassword(String token,String newPassword);
	
	
}
