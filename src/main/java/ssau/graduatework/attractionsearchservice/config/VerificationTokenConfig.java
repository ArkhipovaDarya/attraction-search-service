package ssau.graduatework.attractionsearchservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.keygen.BytesKeyGenerator;
import org.springframework.security.crypto.keygen.KeyGenerators;

@Configuration
public class VerificationTokenConfig {

    @Bean
    public BytesKeyGenerator bytesKeyGenerator() {
        return KeyGenerators.secureRandom(20);
    }
}
