package com.org.jayanth.controller;

import com.org.jayanth.dto.UserDto;
import com.org.jayanth.dto.UserUpdateDto;
import com.org.jayanth.dtobestprac.ForgotPasswordResponseDto;
import com.org.jayanth.dtobestprac.MessageDto;
import com.org.jayanth.dtobestprac.RegistrationSuccessfullResponseDto;
import com.org.jayanth.dtobestprac.ResetPasswordDto;
import com.org.jayanth.dtobestprac.UserInfoDto;
import com.org.jayanth.entity.User;
import com.org.jayanth.service.UserService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // ------------------------- GET USER PROFILE -----------------------------
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public UserInfoDto getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    // ------------------------- UPDATE PROFILE -------------------------------
    @PutMapping("/{id}")
    @PreAuthorize("#id == authentication.principal.id or hasRole('ADMIN')")
    public UserInfoDto updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateDto dto) {

        return userService.updateUser(id, dto);
    }

    // ------------------------- UPDATE PASSWORD ------------------------------
    @PutMapping("/{id}/password")
    @PreAuthorize("#id == authentication.principal.id or hasAnyRole('ADMIN','USER')")
    public MessageDto updatePassword(
            @PathVariable Long id,
            @RequestParam String oldPassword,
            @RequestParam String newPassword) {
        
    	System.out.println(oldPassword+"   "+newPassword);
        return userService.updatePassword(id, oldPassword, newPassword);
         
    }

    // ------------------------- DELETE USER (ADMIN ONLY) ---------------------
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully!");
    }

    // ------------------------- LIST USERS (ADMIN ONLY) ----------------------
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> listUsers() {
        return ResponseEntity.ok(userService.listAllUsers());
    }
    @PutMapping("/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public MessageDto updateUserRole(
            @PathVariable Long id,
            @RequestParam String role) {

        return  userService.updateRole(id, role);
       
    }
    
    


    

}
