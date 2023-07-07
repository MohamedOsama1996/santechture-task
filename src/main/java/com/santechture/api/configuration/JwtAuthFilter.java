package com.santechture.api.configuration;

import com.santechture.api.entity.User;
import com.santechture.api.exception.BusinessExceptions;
import com.santechture.api.service.JwtService;
import com.santechture.api.utility.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {


  @Autowired
  JwtService jwtService;

  @Autowired
  @Qualifier("adminDetailsService")
  UserDetailsService userDetailsService;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

     String token = request.getHeader(Utils.AUTH_HEADER);
     //check if token exists and in the correct format
    if(token == null || !token.startsWith("Bearer ")){
        filterChain.doFilter(request,response);
        return;
    }
    String jwt = token.substring(7);
    //check the token validity
    if(!jwtService.validateToken(jwt)){
      filterChain.doFilter(request,response);
      return;
    }
    String email = jwtService.getUserName(jwt);

    if(email !=null && SecurityContextHolder.getContext().getAuthentication()==null){
      // check if email exists in our database
      UserDetails userDetails = userDetailsService.loadUserByUsername(email);
      setAuthenticationContext(userDetails);
    }

    filterChain.doFilter(request, response);

  }


  private void setAuthenticationContext(UserDetails userDetails) {

    UsernamePasswordAuthenticationToken
        authentication = new UsernamePasswordAuthenticationToken(userDetails, null, null);

    SecurityContextHolder.getContext().setAuthentication(authentication);
  }

}
