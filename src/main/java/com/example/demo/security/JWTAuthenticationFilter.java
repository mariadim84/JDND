package com.example.demo.security;

import com.auth0.jwt.JWT;
import com.example.demo.controllers.OrderController;
import com.example.demo.model.persistence.repositories.UserRepository;
//import com.example.demo.model.persistence.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static com.example.demo.security.SecurityConstants.EXPIRATION_TIME;
import static com.example.demo.security.SecurityConstants.HEADER_STRING;
import static com.example.demo.security.SecurityConstants.SECRET;
import static com.example.demo.security.SecurityConstants.TOKEN_PREFIX;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;
    public static final Logger log = LoggerFactory.getLogger((JWTAuthenticationFilter.class));

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) throws AuthenticationException {
        try {
            com.example.demo.model.persistence.User creds = new ObjectMapper()
                    .readValue(req.getInputStream(), com.example.demo.model.persistence.User.class);
            //CreateUserRequest creds = new CreateUserRequest();
            //creds.setUsername(req.getParameter("username"));
            //creds.setPassword(req.getParameter("password"));
            log.info("User attempting authentication: {}", creds.getUsername());
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getUsername(),
                            creds.getPassword()
                               //     + new StringBuffer(creds.getUsername().toLowerCase()).reverse().toString()
                            ,new ArrayList<>())
            );

        } catch (IOException e) {
            log.info("User authentication failed.");
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth)  {

        String token = JWT.create()
                .withSubject(((User) auth.getPrincipal()).getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(HMAC512(SECRET.getBytes()));
        res.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
        log.info("User authenticated: {}",  ((User) auth.getPrincipal()).getUsername());

    }

    @Override
    protected void unsuccessfulAuthentication(javax.servlet.http.HttpServletRequest request,
                                              javax.servlet.http.HttpServletResponse response,
                                              AuthenticationException failed)
            throws IOException, javax.servlet.ServletException {
        log.info("User authentication failed {}.", failed.getMessage());
        super.unsuccessfulAuthentication(request, response, failed);
    }

}