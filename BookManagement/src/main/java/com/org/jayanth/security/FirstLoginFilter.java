package com.org.jayanth.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.org.jayanth.entity.User;
import com.org.jayanth.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FirstLoginFilter extends OncePerRequestFilter {

    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        // Allowed endpoints even if firstLogin = true
        boolean allowed = path.startsWith("/api/auth/login")
                       || (path.contains("/password")); // change password endpoint

        // If no authenticated user → continue
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            filterChain.doFilter(request, response);
            return;
        }

        // Get logged-in user email
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByEmail(email);

        if (user != null && user.isFirstLogin() && !allowed) {
            // BLOCK access until password is changed
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("You must change your password before accessing this resource.");
            return;
            }

        filterChain.doFilter(request, response);
    }
}
