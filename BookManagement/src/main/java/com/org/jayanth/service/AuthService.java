package com.org.jayanth.service;

import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.org.jayanth.dto.UserDto;
import com.org.jayanth.dtobestprac.AuthResponseDto;
import com.org.jayanth.dtobestprac.RegistrationSuccessfullResponseDto;

public interface AuthService {

    RegistrationSuccessfullResponseDto register(UserDto dto);

    AuthResponseDto login(Map<String, String> payload);
}
