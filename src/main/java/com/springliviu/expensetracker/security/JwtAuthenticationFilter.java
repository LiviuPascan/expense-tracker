package com.springliviu.expensetracker.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider,
                                   UserDetailsServiceImpl userDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getServletPath();
        String authHeader = request.getHeader("Authorization");

        // Логируем входящий запрос
        System.out.printf("🔍 Incoming request: %s %s%n", request.getMethod(), path);
        System.out.printf("🔐 Authorization header: %s%n", authHeader);

        // Пути, исключённые из фильтрации
        if (path.startsWith("/auth")
                || path.startsWith("/swagger-ui")
                || path.startsWith("/v3/api-docs")
                || path.startsWith("/swagger-resources")
                || path.startsWith("/swagger-ui.html")
                || path.startsWith("/webjars")
                || path.startsWith("/api")
                || path.equals("/"))      {


            System.out.println("✅ Path is whitelisted, skipping JWT filter.");
            filterChain.doFilter(request, response);
            return;
        }

        // Обработка токена
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("🚫 Missing or invalid Authorization header.");
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);
        String username = jwtTokenProvider.extractUsername(token);
        System.out.printf("👤 Extracted username from token: %s%n", username);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (jwtTokenProvider.isTokenValid(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                System.out.println("✅ JWT validated. Authentication set.");
            } else {
                System.out.println("❌ Invalid JWT token.");
            }
        }

        filterChain.doFilter(request, response);
    }

}
