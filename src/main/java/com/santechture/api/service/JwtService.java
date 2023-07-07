package com.santechture.api.service;

import com.santechture.api.entity.Admin;
import com.santechture.api.entity.User;
import com.santechture.api.exception.BusinessExceptions;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.impl.crypto.DefaultJwtSignatureValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;

@Service
public class JwtService {

  @Value("${secret.value}")
  public   String SECRET_KEY;

  private static final long EXPIRE_DURATION = 24 * 60 * 60 * 1000;

  public boolean validateToken(String token)  {
       try {
         Jwts.parser()
             .setSigningKey(SECRET_KEY)
             .parseClaimsJws(token);
         return true;
       }catch (Exception ex){
          return  false;
       }
  }

  public String generateAccessToken(Admin admin) {
    return Jwts.builder()
               .setSubject(admin.getUsername())
               .setIssuer("santechture")
               .setIssuedAt(new Date())
               .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_DURATION))
               .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
               .compact();

  }

  public String generateAccessTokenForUser(User user) {
    return Jwts.builder()
               .setSubject(user.getUsername())
               .setIssuer("santechture")
               .setIssuedAt(new Date())
               .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_DURATION))
               .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
               .compact();

  }
  public String getUserName(String token) {
    return parseClaims(token).getSubject();
  }

  private Claims parseClaims(String token) {
    return Jwts.parser()
               .setSigningKey(SECRET_KEY)
               .parseClaimsJws(token)
               .getBody();
  }
}
