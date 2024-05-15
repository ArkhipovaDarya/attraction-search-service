package ssau.graduatework.attractionsearchservice.verificationToken.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ssau.graduatework.attractionsearchservice.user.User;
import ssau.graduatework.attractionsearchservice.user.UserRepository;
import ssau.graduatework.attractionsearchservice.verificationToken.exception.TokenNotFoundException;
import ssau.graduatework.attractionsearchservice.verificationToken.VerificationToken;
import ssau.graduatework.attractionsearchservice.verificationToken.VerificationTokenRepository;
import ssau.graduatework.attractionsearchservice.verificationToken.VerificationTokenService;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class VerificationTokenServiceImpl implements VerificationTokenService {
    private final VerificationTokenRepository verificationTokenRepository;
    private final UserRepository userRepository;

    @Override
    public VerificationToken updateAndGetVerificationTokenByUser(User user) {
        String text = user.getEmail() + LocalDateTime.now();

        final MessageDigest digest;
        final byte[] hash;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            hash = digest.digest(text.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        final StringBuilder hexString = new StringBuilder();
            for (int i = 0; i < hash.length; i++) {
                final String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }
        String tokenValue = hexString.toString();

        Optional<VerificationToken> verificationTokenOptional = verificationTokenRepository.findByUser(user);
        if (verificationTokenOptional.isEmpty()) {
            VerificationToken verificationToken = VerificationToken.builder()
                    .token(tokenValue)
                    .user(user)
                    .build();
            return verificationTokenRepository.save(verificationToken);
        }
        VerificationToken verificationToken = verificationTokenOptional.get();
        verificationToken.setToken(tokenValue);
        return verificationTokenRepository.save(verificationToken);
    }

    @Override
    public void verifyToken(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new TokenNotFoundException());
        User user = verificationToken.getUser();
        user.setIsVerified(true);
        userRepository.save(user);
        verificationTokenRepository.delete(verificationToken);
    }
}
