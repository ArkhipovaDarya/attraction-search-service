package ssau.graduatework.attractionsearchservice.verificationToken;

import jakarta.persistence.*;
import lombok.*;
import ssau.graduatework.attractionsearchservice.user.User;

@Getter
@Setter
@EqualsAndHashCode(exclude = "id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "verification_token")
public class VerificationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String token;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
