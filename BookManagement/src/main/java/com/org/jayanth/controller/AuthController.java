//package com.org.jayanth.controller;
//
//import java.util.Map;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.web.bind.annotation.*;
//
//import com.org.jayanth.dto.UserDto;
//import com.org.jayanth.entity.User;
//import com.org.jayanth.exceptions.UserNotFoundException;
//import com.org.jayanth.security.JwtUtil;
//import com.org.jayanth.service.UserService;
//
//@RestController
//@RequestMapping("/api/auth")
//public class AuthController {
//
//	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private JwtUtil jwtUtil;
//
//    @Autowired
//    private AuthenticationManager authenticationManager;
//
//    // registration (sends temp password via email inside service)
//    @PostMapping("/register")
//    public ResponseEntity<?> register(@RequestBody UserDto dto) {
//    	logger.info("user registration started for {}",dto.getEmail() );
//        User created = userService.register(dto);
//        logger.info("user registration {} is successfull",dto.getEmail());
//        return ResponseEntity.ok(Map.of("message", "User registered. Temporary password sent to email", "email", created.getEmail()));
//    }
//
//    // login - returns JWT
//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody Map<String, String> payload) {
//        String email = payload.get("email");
//        String password = payload.get("password");
//        logger.info("user login has started ==>{} ",email);
//    	
//        try {
//            // authenticate using spring authentication manager (this checks encoded pwd)
//            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
//        } catch (AuthenticationException ex) {
//            // If authentication fails, it may be because user uses temp password (no encoded password yet).
//            // We'll fallback to custom login using UserService (which allows temp password).
//            User user = userService.findByEmail(email);
//            System.out.println(user);
//            if (user == null) {
//            	throw new UserNotFoundException("user : "+email+" not found");
//            }
//            // check temp password flow
//            if (user.isFirstLogin()) {
//                if (userService.validateTempPassword(email, password)) {
//                    // return a token but mark that client must force reset
//                    String token = jwtUtil.generateToken(user.getEmail(), user.getRole());
//                    return ResponseEntity.ok(Map.of("token", token, "forcePasswordReset", true));
//                } else {
//                    return ResponseEntity.status(401).body(Map.of("error", "Invalid temporary password"));
//                }
//            }
//            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
//        }
//
//        // if reached here, authentication succeeded (normal encoded password)
//        User user = userService.findByEmail(email);
//        String token = jwtUtil.generateToken(user.getEmail(), user.getRole());
//        return ResponseEntity.ok(Map.of("token", token, "forcePasswordReset", user.isFirstLogin()));
//    }
//}
package com.org.jayanth.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.org.jayanth.dto.UserDto;
import com.org.jayanth.dtobestprac.AuthResponseDto;
import com.org.jayanth.dtobestprac.ForgotPasswordResponseDto;
import com.org.jayanth.dtobestprac.RegistrationSuccessfullResponseDto;
import com.org.jayanth.dtobestprac.ResetPasswordDto;
import com.org.jayanth.entity.User;
import com.org.jayanth.service.AuthService;
import com.org.jayanth.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;
    
    @PostMapping("/register")
    public RegistrationSuccessfullResponseDto register(@RequestBody UserDto dto) {
       
    	return authService.register(dto);
    
    }
    
    @PostMapping("/login")
    public AuthResponseDto login(@RequestBody Map<String, String> payload) {
        return authService.login(payload);
    }
    
    @PostMapping("/forgot-password")
    public ForgotPasswordResponseDto forgotPassword(@RequestBody User req) {

		System.out.println("hii i am cmng  till here");
    	return userService.forgotPassword(req.getEmail());
       
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordDto dto) {
    	
    	
        userService.resetPassword(dto.getToken(), dto.getNewPassword());
        return ResponseEntity.ok("Password reset successful");
    }
    
}

