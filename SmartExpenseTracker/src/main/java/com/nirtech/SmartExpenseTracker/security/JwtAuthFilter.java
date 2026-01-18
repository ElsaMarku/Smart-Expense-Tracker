package com.nirtech.SmartExpenseTracker.security;

import com.nirtech.SmartExpenseTracker.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
            public JwtAuthFilter(JwtUtil jwtUtil){
                this.jwtUtil = jwtUtil;
            }

            @Override
            protected  void doFilterInternal(HttpServletRequest request,
                                             HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
                String path = request.getRequestURI();

                // ✅ Skip filter for /auth endpoints
                if (path.startsWith("/auth")) {
                    filterChain.doFilter(request, response);
                    return;
                }

                System.out.println("===== Incoming Headers =====");
                request.getHeaderNames().asIterator()
                        .forEachRemaining(name -> System.out.println(name + ": " + request.getHeader(name)));
                System.out.println("============================");
                String authHeader = request.getHeader("Authorization");
                String token = null;
                String username = null;

                // ✅ Extract token if present
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    token = authHeader.substring(7);
                    try {
                        username = jwtUtil.extractUsername(token);
                        System.out.println("UserName = " + username);
                    } catch (Exception e) {
                        System.out.println("Invalid JWT Token: " + e.getMessage());
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.getWriter().write("Invalid JWT Token");
                        return;
                    }
                }

                // ✅ Validate token and set SecurityContext
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                    if (jwtUtil.validateToken(token)) {
                        UsernamePasswordAuthenticationToken authToken =
                                new UsernamePasswordAuthenticationToken(username, null, List.of());
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    } else {
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.getWriter().write("Invalid JWT Token");
                        return;
                    }
                }

                filterChain.doFilter(request, response);
            }
}
