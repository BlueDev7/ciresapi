package com.api.cires.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JWTUtil {
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final long expirationTime = 86400000;


    public String generateToken(String subject){
        Map<String,Object> claims = new HashMap<>();
        return buildToken(claims,subject);
    }
    private String buildToken(Map<String,Object> claims ,String subject){
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(key)
                .compact();

    }

    public boolean validateToken(String token,String subject){
        final String email = getClaims(token).getSubject();
        return (email.equals(subject) && !isTokenExpired(token));

    }

    public boolean isTokenExpired(String token){
        Date expiration = getClaims(token).getExpiration();
        return expiration.before(new Date());
    }
    public Claims getClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
