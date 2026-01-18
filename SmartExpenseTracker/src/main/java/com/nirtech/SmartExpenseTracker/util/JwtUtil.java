package com.nirtech.SmartExpenseTracker.util;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    long expiration = 1000L * 60 * 60 * 24;// 1 hour
    //✔ key → Secret key for signing tokens (HS256 algorithm).
    //✔ expiration → Token will expire after 1 hour.

    public String generateToken(String username){
        //create the JWT token string for the user
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username) // Store username in token
                .setIssuedAt(new Date()) // current Time
                .setExpiration(new Date(System.currentTimeMillis() + expiration)) // Expiry time
                .signWith(key) //sign token
                        .compact(); //Build token
    }
    public String extractUsername(String token){
        return Jwts.parserBuilder().setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
    public boolean validateToken(String token){
        try{
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        }catch(JwtException e){
            return false;
        }
    }
}
