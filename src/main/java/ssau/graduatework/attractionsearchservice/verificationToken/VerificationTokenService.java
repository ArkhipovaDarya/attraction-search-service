package ssau.graduatework.attractionsearchservice.verificationToken;

import ssau.graduatework.attractionsearchservice.user.User;

public interface VerificationTokenService {
    VerificationToken updateAndGetVerificationTokenByUser(User user);

    void verifyToken(String token);
}
