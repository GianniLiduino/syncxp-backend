package br.com.fiap.fintech.service;

import br.com.fiap.fintech.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.UUID;

@Service
public class TokenService {

    @Value("${auth.secret-key}")
    private String secretKey;

    @Value("${auth.token-expiration-hours}")
    private int expirationHours;

    private static final String ALGORITHM = "AES";

    public String generateToken(User user) {
        try {
            long expirationTime = LocalDateTime.now()
                    .plusHours(expirationHours)
                    .atZone(ZoneId.systemDefault())
                    .toInstant()
                    .toEpochMilli();

            String rawData = user.getId().toString() + "|" + expirationTime;
            
            SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            
            byte[] encryptedBytes = cipher.doFinal(rawData.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar o token", e);
        }
    }

    public String validateAndExtractUserId(String token) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            
            byte[] decodedBytes = Base64.getDecoder().decode(token);
            byte[] decryptedBytes = cipher.doFinal(decodedBytes);
            
            String decryptedData = new String(decryptedBytes, StandardCharsets.UTF_8);
            String[] parts = decryptedData.split("\\|");
            
            if (parts.length != 2) {
                throw new RuntimeException("Token inválido");
            }
            
            String userId = parts[0];
            long expirationTime = Long.parseLong(parts[1]);
            
            if (System.currentTimeMillis() > expirationTime) {
                throw new RuntimeException("Token expirado");
            }
            
            return userId;
        } catch (Exception e) {
            throw new RuntimeException("Token inválido ou expirado", e);
        }
    }
}
