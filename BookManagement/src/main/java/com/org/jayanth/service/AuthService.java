package com.org.jayanth.service;

import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.org.jayanth.dto.LoginRequestDto;
import com.org.jayanth.dto.RegisterUserDto;
import com.org.jayanth.dto.UserDto;
import com.org.jayanth.dtobestprac.AuthResponseDto;
import com.org.jayanth.dtobestprac.RegistrationSuccessfullResponseDto;

public interface AuthService {

    RegistrationSuccessfullResponseDto register(RegisterUserDto dto);

    AuthResponseDto login(LoginRequestDto payload);
}
