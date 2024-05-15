package ssau.graduatework.attractionsearchservice.role;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "roles")
public class Role implements GrantedAuthority {
    @Id
    @JoinTable(name = "users_roles",
            joinColumns = {@JoinColumn(name = "role_id")})
    private Integer id;

    private String name;
    @Override
    public String getAuthority() {
        return name;
    }
}
