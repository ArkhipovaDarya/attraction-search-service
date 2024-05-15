package ssau.graduatework.attractionsearchservice.verificationToken;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ssau.graduatework.attractionsearchservice.user.User;

import java.util.Optional;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByUser(User user);
    Optional<VerificationToken> findByToken(String token);
}
