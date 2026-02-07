package com.org.jayanth.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.org.jayanth.dto.GenericSucessResponseDto;
import com.org.jayanth.dto.RegisterUserDto;
import com.org.jayanth.dto.UserUpdateDto;
import com.org.jayanth.dtobestprac.ForgotPasswordResponseDto;
import com.org.jayanth.dtobestprac.MessageDto;
import com.org.jayanth.dtobestprac.RegistrationSuccessfullResponseDto;
import com.org.jayanth.dtobestprac.UserInfoDto;
import com.org.jayanth.entity.Role;
import com.org.jayanth.entity.User;
import com.org.jayanth.exceptions.EmailAlreadyExistsException;
import com.org.jayanth.exceptions.IncorrectTokenException;
import com.org.jayanth.exceptions.InvalidRoleException;
import com.org.jayanth.exceptions.UserNotFoundException;
import com.org.jayanth.exceptions.WrongUserCredentialsException;
import com.org.jayanth.helper.MaskingUtil;
import com.org.jayanth.repo.UserRepo;

@Service
public class UserServiceImpl implements UserService{

	
	public static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private EmailService emailService;

	

	@Override
	public RegistrationSuccessfullResponseDto register(RegisterUserDto userDto) {
	
		
		logger.info("User registration started for user {}",MaskingUtil.maskEmail(userDto.getEmail()));
		if(existsByEmail(userDto.getEmail()))
		{
			logger.warn("User with mail {} already exists",MaskingUtil.maskEmail(userDto.getEmail()));
			throw new EmailAlreadyExistsException("Email: "+userDto.getEmail()+" already exists !!!1");
		}
		
		String tempPassword = generateTempPassword();
		
		User user = new User();
		
		user.setEmail(userDto.getEmail());
		user.setFirstLogin(true);
		user.setName(userDto.getName());
		user.setPassword(passwordEncoder.encode(tempPassword));
		user.setRole(Role.ROLE_USER);
		
		
		userRepo.save(user);
		
		logger.info("User Registration mail Sending Service has been started");
		emailService.sendEmail(user.getEmail(), "WELCOME E-COMMERCE BOOK STORE", "Your temporary password is "+tempPassword+" please change it");
		logger.info("mail sending is successfull");
		
		
		logger.info("user registered successfully");
		return new RegistrationSuccessfullResponseDto(user.getEmail(), "registration is successfull please reset password temporary password has been shared to registered email");
	
	}


	@Override
	public MessageDto updatePassword(Long userId, String oldPassword, String newPassword) {
		
		logger.info("update password request is initiated");
		
		 User user = userRepo.findById(userId)
	                .orElseThrow(() -> new UserNotFoundException("User not found!"));
	
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            logger.debug("password checking started...");	
        	throw new WrongUserCredentialsException("Old password does not match!");        
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setFirstLogin(false);
        userRepo.save(user);
		logger.info("password updation successfull");
        return new MessageDto("password has been updated succefully");
	}

	@Override
	public User findByEmail(String email) {
	
		logger.info("find User by email is initiated");
		 return userRepo.findByEmail(email).orElseThrow(()->new UserNotFoundException("user not found"));
		
	}

	@Override
	public UserInfoDto  getUserById(Long userId) {
	
		
		logger.info("get user by id is started");
		 User user = userRepo.findById(userId)
	                .orElseThrow(() -> new UserNotFoundException("User not found!"));
	
		 logger.info("get user by id is successfull");
		 return new UserInfoDto(userId,user.getEmail() ,user.getRole().toString(),user.isFirstLogin() , user.getName());
		 
	}

	@Override
	public UserInfoDto updateUser(Long userId, UserUpdateDto dto) {

		logger.info("update user request is initiated");
		User user = userRepo.findById(userId).orElseThrow(()->new UserNotFoundException("User not found"));

        if (dto.getName() != null) user.setName(dto.getName());
//        if (dto.getPhone() != null) user.setPhone(dto.getPhone());
//        if (dto.getAddress() != null) user.setAddress(dto.getAddress());
       userRepo.save(user);
       logger.info("update user request is successfull");
       return new UserInfoDto(userId, user.getEmail(),user.getRole().toString(),user.isFirstLogin(), user.getName());
	}

	@Override
	public MessageDto deleteUser(Long userId) {
		
		  logger.info("delete user request initiated");
	      userRepo.deleteById(userId);
	      logger.info("delete user request successfull");
	      return new MessageDto("user has been deleted successfully");
		
	}

	@Override
	public List<UserInfoDto> listAllUsers() {
	    
		
		logger.info("list all users request initiated");
	List<User> users = userRepo.findAll();
	
	List<UserInfoDto> u = new ArrayList<>();
	for(User user : users)
	{
		UserInfoDto dto = new UserInfoDto(user.getId(), user.getEmail(), user.getRole().toString(), user.isFirstLogin(), user.getName());
		u.add(dto);
	}
	logger.info("list all users request is successfull");
	
	return u;
	
	}

	@Override
	public boolean existsByEmail(String email) {
		
		 logger.debug("exists by email process is going on..");
		 return userRepo.existsByEmail(email);
		   
	}

	

	@Override
	public MessageDto assignRole(Long userId, String role) {
		 User user = userRepo.findById(userId)
	                .orElseThrow(() -> new UserNotFoundException("User not found!"));
	
		//        user.setRole(Role.);
        userRepo.save(user);
        
        return new MessageDto("role has been assigned succefully");
	}
	
	
	private String generateTempPassword()
	{
		Random random = new Random();
		
		return "TMP"+(10000+random.nextInt(90000));
	}

	@Override
	public boolean validateTempPassword(String email, String password) {
	    User user = findByEmail(email);
	    return passwordEncoder.matches(password, user.getPassword());
	}

	
	@Override
	public MessageDto updateRole(Long userId, String roleName) {
	    User user = userRepo.findById(userId)
	            .orElseThrow(() -> new UserNotFoundException("User not found!"));

	    Role newRole;

	    try {
	        newRole = Role.valueOf(roleName.toUpperCase());
	    } catch (Exception e) {
	        throw new InvalidRoleException("Invalid role. Allowed: USER, ADMIN");
	    }

	    user.setRole(newRole);
	    userRepo.save(user);
	    
	    return new MessageDto("role has been updated successfully");
	}

	@Override
	public ForgotPasswordResponseDto forgotPassword(String email) {
		
		User user = userRepo.findByEmail(email)
				.orElseThrow(()->new UserNotFoundException("user not found"));
	
		String token = UUID.randomUUID().toString();
		
	     user.setResetToken(token);
	     
	     user.setResetTokenExpiry(LocalDateTime.now().plusMinutes(15));
	
	     userRepo.save(user);
	     
	     
	     String resetLink = "http://localhost:3000/reset-password?token=" + token;
	     
	     emailService.sendEmail(
	    	        email,
	    	        "Reset your password",
	    	        "Click here to reset password: " + resetLink
	    	    );
	     
	     return new ForgotPasswordResponseDto(email, resetLink);
	}
	public GenericSucessResponseDto resetPassword(String token, String newPassword) {

	    User user = userRepo.findByResetToken(token).orElseThrow(()->new IncorrectTokenException("INCORRECT TOKEN IS ENTERED"));

	    if (user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
	        throw new IncorrectTokenException("Reset token expired");
	    }

	    user.setPassword(passwordEncoder.encode(newPassword));
	    user.setResetToken(null);
	    user.setResetTokenExpiry(null);
	    user.setFirstLogin(false);

	    userRepo.save(user);
	    
	    return new GenericSucessResponseDto("Password reset is successfull");
	}


}
