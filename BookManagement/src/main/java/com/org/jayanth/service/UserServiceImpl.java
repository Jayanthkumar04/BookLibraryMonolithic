package com.org.jayanth.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.org.jayanth.dto.UserDto;
import com.org.jayanth.dto.UserUpdateDto;
import com.org.jayanth.dtobestprac.ForgotPasswordResponseDto;
import com.org.jayanth.dtobestprac.MessageDto;
import com.org.jayanth.dtobestprac.RegistrationSuccessfullResponseDto;
import com.org.jayanth.dtobestprac.UserInfoDto;
import com.org.jayanth.entity.Role;
import com.org.jayanth.entity.User;
import com.org.jayanth.exceptions.EmailAlreadyExistsException;
import com.org.jayanth.exceptions.InvalidRoleException;
import com.org.jayanth.exceptions.UserNotFoundException;
import com.org.jayanth.exceptions.WrongUserCredentialsException;
import com.org.jayanth.repo.UserRepo;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private EmailService emailService;

	

	@Override
	public RegistrationSuccessfullResponseDto register(UserDto userDto) {
	
		if(existsByEmail(userDto.getEmail()))
		{
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
		
//		emailService.sendEmail(user.getEmail(), "WELCOME E-COMMERCE BOOK STORE", "Your temporary password is "+tempPassword+" please change it");
		
		
		
		return new RegistrationSuccessfullResponseDto(user.getEmail(), "registration is successfull please reset password temporary password has been shared to registered email");
	
	}

//	@Override
//	public void resetPassword(String email, String newPassword) {
//	
//		User user = findByEmail(email);
//		
//		if(user == null) throw new UserNotFoundException("user not found");
//		
//		user.setPassword(passwordEncoder.encode(newPassword));
//		
//		user.setFirstLogin(false);
//		
//		userRepo.save(user);
//		
//	}

	@Override
	public MessageDto updatePassword(Long userId, String oldPassword, String newPassword) {
		
		 User user = userRepo.findById(userId)
	                .orElseThrow(() -> new UserNotFoundException("User not found!"));
	
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
        	
        	throw new WrongUserCredentialsException("Old password does not match!");        
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setFirstLogin(false);
        userRepo.save(user);
		
        return new MessageDto("password has been updated succefully");
	}

	@Override
	public User findByEmail(String email) {
	
		 return userRepo.findByEmail(email).orElseThrow(()->new UserNotFoundException("user not found"));
		
	}

	@Override
	public UserInfoDto  getUserById(Long userId) {
	
		
		
		 User user = userRepo.findById(userId)
	                .orElseThrow(() -> new UserNotFoundException("User not found!"));
	
		 return new UserInfoDto(userId,user.getEmail() ,user.getRole().toString(),user.isFirstLogin() , user.getName());
		 
	}

	@Override
	public UserInfoDto updateUser(Long userId, UserUpdateDto dto) {
		User user = userRepo.findById(userId).orElseThrow(()->new UserNotFoundException("User not found"));

        if (dto.getName() != null) user.setName(dto.getName());
//        if (dto.getPhone() != null) user.setPhone(dto.getPhone());
//        if (dto.getAddress() != null) user.setAddress(dto.getAddress());

       userRepo.save(user);
       return new UserInfoDto(userId, user.getEmail(),user.getRole().toString(),user.isFirstLogin(), user.getName());
	}

	@Override
	public MessageDto deleteUser(Long userId) {
		
	      userRepo.deleteById(userId);
		
	      return new MessageDto("user has been deleted successfully");
		
	}

	@Override
	public List<UserInfoDto> listAllUsers() {
	    
	List<User> users = userRepo.findAll();
	
	List<UserInfoDto> u = new ArrayList<>();
	for(User user : users)
	{
		UserInfoDto dto = new UserInfoDto(user.getId(), user.getEmail(), user.getRole().toString(), user.isFirstLogin(), user.getName());
		u.add(dto);
	}
	
	return u;
	
	}

	@Override
	public boolean existsByEmail(String email) {
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
		if(passwordEncoder.matches(password, user.getPassword())) return true;
		return false;
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
		
		System.out.println("hii i am cmng  till here");
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
	public void resetPassword(String token, String newPassword) {

	    User user = userRepo.findByResetToken(token).orElseThrow(()->new UserNotFoundException("user not found"));

	    if (user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
	        throw new RuntimeException("Reset token expired");
	    }

	    user.setPassword(passwordEncoder.encode(newPassword));
	    user.setResetToken(null);
	    user.setResetTokenExpiry(null);
	    user.setFirstLogin(false);

	    userRepo.save(user);
	}


}
