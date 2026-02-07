package com.org.jayanth.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import com.org.jayanth.dto.LoginRequestDto;
import com.org.jayanth.dto.RegisterUserDto;
import com.org.jayanth.dtobestprac.AuthResponseDto;
import com.org.jayanth.dtobestprac.RegistrationSuccessfullResponseDto;
import com.org.jayanth.dtobestprac.UserInfoDto;
import com.org.jayanth.entity.User;
import com.org.jayanth.exceptions.UserNotFoundException;
import com.org.jayanth.exceptions.WrongUserCredentialsException;
import com.org.jayanth.helper.MaskingUtil;
import com.org.jayanth.security.JwtUtil;

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
    public RegistrationSuccessfullResponseDto register(RegisterUserDto dto) {

    	 logger.info("Auth | Registration started for email={}", MaskingUtil.maskEmail(dto.getEmail()));

        RegistrationSuccessfullResponseDto created = userService.register(dto);

        logger.info("Auth | Registration completed successfully for email={}",
                MaskingUtil.maskEmail(dto.getEmail()));
        return new RegistrationSuccessfullResponseDto(created.getEmail(), "registration successfull.Please reset password");
    }

    @Override
    public AuthResponseDto login(LoginRequestDto payload) { 
        String email = payload.getEmail();
        String password = payload.getPassword();
          
        logger.info("Auth | Login attempt started for email={}",
                MaskingUtil.maskEmail(email));
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );
        } catch (AuthenticationException ex) {

            logger.warn("Auth | AuthenticationManager failed for email={}",
                    MaskingUtil.maskEmail(email));
            // fallback temp-password login
            User user = userService.findByEmail(email);

            if (user == null) {
            	logger.warn("Auth | Login failed - user not found for email={}",
                        MaskingUtil.maskEmail(email));
            	throw new UserNotFoundException("User " + email + " not found");
            }

            if (user.isFirstLogin()) {
            	  logger.debug("Auth | First-time login detected for email={}",
                          MaskingUtil.maskEmail(email));
            	if (userService.validateTempPassword(email, password)) {

            		logger.info("Auth | Temporary password validated for email={}",
                            MaskingUtil.maskEmail(email));
                    String token = jwtUtil.generateToken(
                            user.getEmail(),
                            user.getRole()
                    );
 
                    Long expiry = jwtUtil.getExpirationDateFromToken(token).getTime();
                    logger.info("Auth | Login successful (temporary password) for userId={}",
                            user.getId());

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
        
        logger.info("Auth | Login successful for userId={}", user.getId());

        return new AuthResponseDto(token,"Bearer" ,expiry, new UserInfoDto(user.getId(),user.getEmail(), user.getRole().toString(), user.isFirstLogin(),user.getName()) );
        
    }
}
