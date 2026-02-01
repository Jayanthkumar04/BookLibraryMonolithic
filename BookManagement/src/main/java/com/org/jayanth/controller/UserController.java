package com.org.jayanth.controller;

import com.org.jayanth.dto.UpdatePasswordDto;
import com.org.jayanth.dto.UserUpdateDto;
import com.org.jayanth.dtobestprac.MessageDto;
import com.org.jayanth.dtobestprac.UserInfoDto;
import com.org.jayanth.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {	
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;

    // ------------------------- GET USER PROFILE -----------------------------
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<UserInfoDto> getUserById(@PathVariable Long id) {
    	
    	logger.info("get UserById request is initiated");
        UserInfoDto dto = userService.getUserById(id);
        logger.info("get UserById request is successfull");
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    // ------------------------- UPDATE PROFILE -------------------------------
    @PutMapping("/{id}")
    @PreAuthorize("#id == authentication.principal.id or hasRole('ADMIN')")
    public ResponseEntity<UserInfoDto>  updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateDto dto) {

    	 logger.info("Update user request for user {} is initialted",dto.getName());
         UserInfoDto info = userService.updateUser(id, dto);
         logger.info("update user request is successfull");
         return ResponseEntity.status(HttpStatus.OK).body(info);
    }

    // ------------------------- UPDATE PASSWORD ------------------------------
    @PutMapping("/{id}/password")
    @PreAuthorize("#id == authentication.principal.id or hasAnyRole('ADMIN','USER')")
    public ResponseEntity<MessageDto>  updatePassword(
            @PathVariable Long id,
            
            @Valid @RequestBody UpdatePasswordDto dto
    		) {
        
    	logger.info("update password request is initiated");
    	
        MessageDto msg = userService.updatePassword(id, dto.getOldPassword(), dto.getNewPassword());
         
        logger.info("update password request is successfull");
        
        return ResponseEntity.status(HttpStatus.OK).body(msg);
        
    }

    // ------------------------- DELETE USER (ADMIN ONLY) ---------------------
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageDto> deleteUser(@PathVariable Long id) {
        
    logger.info("delete user request is initiated"); 	
    MessageDto msg =	userService.deleteUser(id);
    logger.info("delete user request is successfull");
        return ResponseEntity.ok(msg);
        
    }

    // ------------------------- LIST USERS (ADMIN ONLY) ----------------------
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserInfoDto>> listUsers() {     
    	
    	logger.info("List all users request is initiated");
    	List<UserInfoDto> users = userService.listAllUsers();
    	logger.info("List all users request is successfull");
    	return ResponseEntity.status(HttpStatus.OK).body(users);
    }
    @PutMapping("/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageDto> updateUserRole(
            @PathVariable Long id,
            @RequestParam String role) {

    	logger.info("Update user role request is initiated");
        MessageDto msg=  userService.updateRole(id, role);
        logger.info("Update user role request is successfull");
        return ResponseEntity.status(HttpStatus.OK).body(msg);
        
       
    }
    
    


    

}
