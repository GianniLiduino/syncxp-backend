package br.com.fiap.fintech.config;

import br.com.fiap.fintech.repository.UserRepository;
import br.com.fiap.fintech.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String authHeader = request.getHeader("Authorization");
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        String token = authHeader.substring(7);
        
        try {
            String userIdStr = tokenService.validateAndExtractUserId(token);
            UUID userId = UUID.fromString(userIdStr);
            
            if (!userRepository.existsById(userId)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return false;
            }
            
            request.setAttribute("userId", userId);
            return true;
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
    }
}
