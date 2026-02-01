package com.org.jayanth.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.org.jayanth.dto.ForgotPasswordDto;
import com.org.jayanth.dto.GenericSucessResponseDto;
import com.org.jayanth.dto.LoginRequestDto;
import com.org.jayanth.dto.RegisterUserDto;
import com.org.jayanth.dtobestprac.AuthResponseDto;
import com.org.jayanth.dtobestprac.ForgotPasswordResponseDto;
import com.org.jayanth.dtobestprac.RegistrationSuccessfullResponseDto;
import com.org.jayanth.dtobestprac.ResetPasswordDto;
import com.org.jayanth.helper.MaskingUtil;
import com.org.jayanth.service.AuthService;
import com.org.jayanth.service.UserService;

import jakarta.validation.Valid;



@RestController
@RequestMapping("/api/auth")
public class AuthController {

	
	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;
    
    
    @PostMapping("/register")
    public  ResponseEntity<RegistrationSuccessfullResponseDto> registerUser(@Valid @RequestBody RegisterUserDto dto) {
       
    	logger.info("Registration reqeust started for {}", MaskingUtil.maskEmail(dto.getEmail()));
    	RegistrationSuccessfullResponseDto response = authService.register(dto);
    
    	logger.info("Registration successfull for user {}",MaskingUtil.maskEmail(dto.getEmail()));
    	
    	return ResponseEntity.status(HttpStatus.CREATED).body(response);
    	
    }
    
    @PostMapping("/login")
    public  ResponseEntity<AuthResponseDto>  loginUser(@Valid @RequestBody LoginRequestDto payload) {
    	logger.info("login request started for user {}", MaskingUtil.maskEmail(payload.getEmail()));
        AuthResponseDto response =  authService.login(payload);
        logger.info("login sucessfull for user {}",MaskingUtil.maskEmail(payload.getEmail()));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
    @PostMapping("/forgot-password")
    public ResponseEntity<ForgotPasswordResponseDto> initiatePasswordReset(@Valid @RequestBody ForgotPasswordDto req) {

    	logger.info("forgotPassword Request started for user {}",MaskingUtil.maskEmail(req.getEmail()));
    	ForgotPasswordResponseDto dto = userService.forgotPassword(req.getEmail());
        
    	logger.info("forgotPassword Request is successfull for user {}",MaskingUtil.maskEmail(req.getEmail()));
    	return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<GenericSucessResponseDto> completePasswordReset(@Valid @RequestBody ResetPasswordDto dto) {
    	logger.info("ResetPassword Request started ");
        GenericSucessResponseDto response =  userService.resetPassword(dto.getToken(), dto.getNewPassword());
       logger.info("ResetPassword request is successfull");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
}

