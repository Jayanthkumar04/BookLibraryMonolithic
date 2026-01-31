package com.org.jayanth.service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import com.org.jayanth.dto.UserDto;
import com.org.jayanth.dtobestprac.AuthResponseDto;
import com.org.jayanth.dtobestprac.RegistrationSuccessfullResponseDto;
import com.org.jayanth.dtobestprac.UserInfoDto;
import com.org.jayanth.entity.User;
import com.org.jayanth.exceptions.UserNotFoundException;
import com.org.jayanth.exceptions.WrongUserCredentialsException;
import com.org.jayanth.security.JwtUtil;
import com.org.jayanth.service.AuthService;
import com.org.jayanth.service.UserService;

@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public RegistrationSuccessfullResponseDto register(UserDto dto) {
        logger.info("User registration started for {}", dto.getEmail());

        RegistrationSuccessfullResponseDto created = userService.register(dto);

        logger.info("User registration successful for {}", dto.getEmail());

        return new RegistrationSuccessfullResponseDto(created.getEmail(), "registration successfull.Please reset password");
    }

    @Override
    public AuthResponseDto login(Map<String, String> payload) {
        String email = payload.get("email");
        String password = payload.get("password");
          
        logger.info("User login started ==> {}", email);

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );
        } catch (AuthenticationException ex) {

            // fallback temp-password login
            User user = userService.findByEmail(email);

            if (user == null) {
                throw new UserNotFoundException("User " + email + " not found");
            }

            if (user.isFirstLogin()) {
                if (userService.validateTempPassword(email, password)) {

                    String token = jwtUtil.generateToken(
                            user.getEmail(),
                            user.getRole()
                    );
 
                    Long expiry = jwtUtil.getExpirationDateFromToken(token).getTime();
                    return new AuthResponseDto(token,"Bearer" ,expiry, new UserInfoDto(user.getId(),user.getEmail(), user.getRole().toString(), user.isFirstLogin(),user.getName()) );
                }

                throw new WrongUserCredentialsException("wrong credentials");
            }

            throw new WrongUserCredentialsException("wrong credentials");
        }

        // normal login success
        User user = userService.findByEmail(email);

        String token = jwtUtil.generateToken(
                user.getEmail(),
                user.getRole()
        );
        Long expiry = jwtUtil.getExpirationDateFromToken(token).getTime();
        
        return new AuthResponseDto(token,"Bearer" ,expiry, new UserInfoDto(user.getId(),user.getEmail(), user.getRole().toString(), user.isFirstLogin(),user.getName()) );
        
    }
}
