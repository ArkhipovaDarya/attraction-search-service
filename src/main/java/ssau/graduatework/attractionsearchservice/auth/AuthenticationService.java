package ssau.graduatework.attractionsearchservice.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ssau.graduatework.attractionsearchservice.config.JwtService;
import ssau.graduatework.attractionsearchservice.role.Role;
import ssau.graduatework.attractionsearchservice.role.RoleRepository;
import ssau.graduatework.attractionsearchservice.user.User;
import ssau.graduatework.attractionsearchservice.user.UserRepository;
import ssau.graduatework.attractionsearchservice.verificationToken.VerificationToken;
import ssau.graduatework.attractionsearchservice.verificationToken.VerificationTokenService;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final VerificationTokenService verificationTokenService;

    public AuthenticationResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            //throw new UserAlreadyExistsException();
        }
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(Set.of(roleRepository.findByName("USER").get()))
                .isVerified(false)
                .build();
        userRepository.save(user);

        // generate and send verificationToken
        VerificationToken verificationToken = verificationTokenService.updateAndGetVerificationTokenByUser(user);
        /*try {
            emailService.sendDefaultRegistrationEmail(user.getEmail(), user.getName(), verificationToken);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }*/

        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .access(jwtToken)
                .name(user.getName())
                .email(user.getEmail())
                .roles(user.getRoles().stream().map(Role::getName).toList())
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();
        if (!user.getIsVerified()) {
            //throw new UserIsNotVerifiedException();
        }
        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .access(jwtToken)
                .name(user.getName())
                .email(user.getEmail())
                .roles(user.getRoles().stream().map(Role::getName).toList())
                .build();
    }
}
