package com.smartcart.productservice.filter;

import com.smartcart.productservice.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getServletPath();

        // ✅ Allow Swagger endpoints
        if (path.contains("/swagger-ui")
                || path.contains("/v3/api-docs")) {

            filterChain.doFilter(request, response);
            return;
        }

        String authHeader =
                request.getHeader("Authorization");

        // ❌ No token
        if (authHeader == null
                || !authHeader.startsWith("Bearer ")) {

            response.setStatus(
                    HttpServletResponse.SC_FORBIDDEN
            );

            response.getWriter().write(
                    "JWT Token Missing"
            );

            return;
        }

        String token =
                authHeader.substring(7);

        // ❌ Invalid token
        if (!jwtUtil.isTokenValid(token)) {

            response.setStatus(
                    HttpServletResponse.SC_FORBIDDEN
            );

            response.getWriter().write(
                    "Invalid JWT Token"
            );

            return;
        }

        String username =
                jwtUtil.extractUsername(token);

        String role =
                jwtUtil.extractRole(token);

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        username,
                        null,
                        List.of(
                                new SimpleGrantedAuthority(
                                        "ROLE_" + role
                                )
                        )
                );

        SecurityContextHolder
                .getContext()
                .setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}