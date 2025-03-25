package com.example.demo.config;

import com.example.demo.annotation.CurrentUserId;
import com.example.demo.util.JwtUtils;
import com.example.demo.util.security.JwtTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.util.logging.Logger;

@Component
public class CurrentUserIdArgumentResolver implements HandlerMethodArgumentResolver {

    private static final Logger logger = Logger.getLogger(CurrentUserIdArgumentResolver.class.getName());

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private JwtTokenService jwtTokenService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CurrentUserId.class) &&
                parameter.getParameterType().equals(Long.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String authHeader = request.getHeader("Authorization");
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.warning("No valid authorization header found");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No valid authorization header found");
        }
        
        try {
            String token = authHeader.substring(7);
            logger.info("Processing token: " + token.substring(0, 20) + "...");
            Long userId = jwtUtils.getUserIdFromToken(token);
//              String userId = JwtTokenService.extractUserId(token);

            logger.info("Successfully extracted userId: " + userId);
            return userId;
        } catch (Exception e) {
            logger.severe("Error processing token: " + e.getMessage());
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid or expired token");
        }
    }
} 