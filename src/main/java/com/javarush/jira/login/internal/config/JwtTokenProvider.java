//package com.javarush.jira.login.internal.config;
//
//import com.javarush.jira.login.User;
//import io.jsonwebtoken.*;
//import org.springframework.security.core.Authentication;
//import org.springframework.stereotype.Component;
//
//import java.util.Date;
//
//@Component
//public class JwtTokenProvider {
//
//    private static final String JWT_SECRET = "jwt_secret_key";
//    private static final long JWT_EXPIRATION = 24 * 60 * 60 * 1000; // 24 hour
//
//    public String createToken(Authentication authentication) {
//        User principal = (User) authentication.getPrincipal();
//        Date now = new Date();
//        Date expirationDate = new Date(now.getTime() + JWT_EXPIRATION);
//        return Jwts.builder()
////                .setSubject(principal.getUsername())
//                .setIssuedAt(now)
//                .setExpiration(expirationDate)
//                .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
//                .compact();
//    }
//
//    public boolean validateToken(String token) {
//        try {
//            Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token);
//            return true;
//        } catch (SignatureException ex) {
//            System.out.println("Invalid JWT signature");
//        } catch (MalformedJwtException ex) {
//            System.out.println("Invalid JWT token");
//        } catch (ExpiredJwtException ex) {
//            System.out.println("Expired JWT token");
//        } catch (UnsupportedJwtException ex) {
//            System.out.println("Unsupported JWT token");
//        } catch (IllegalArgumentException ex) {
//            System.out.println("JWT claims string is empty.");
//        }
//        return false;
//    }
//
//    public String getUsernameFromToken(String token) {
//        Claims claims = Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token).getBody();
//        return claims.getSubject();
//    }
//}
