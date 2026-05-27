package br.com.fiap.fintech.service;

import br.com.fiap.fintech.dto.LoginRequestDTO;
import br.com.fiap.fintech.dto.LoginResponseDTO;
import br.com.fiap.fintech.dto.RegisterRequestDTO;
import br.com.fiap.fintech.model.User;
import br.com.fiap.fintech.repository.UserRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    public User register(RegisterRequestDTO registerRequest) {
        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new RuntimeException("E-mail já cadastrado no sistema");
        }

        String hashedPassword = BCrypt.hashpw(registerRequest.getPassword(), BCrypt.gensalt());

        User user = new User(
                registerRequest.getName(),
                registerRequest.getEmail(),
                hashedPassword
        );

        return userRepository.save(user);
    }

    public LoginResponseDTO login(LoginRequestDTO loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!BCrypt.checkpw(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Senha incorreta");
        }

        String token = tokenService.generateToken(user);
        return new LoginResponseDTO(token);
    }
}
