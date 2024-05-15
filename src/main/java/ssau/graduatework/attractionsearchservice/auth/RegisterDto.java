package ssau.graduatework.attractionsearchservice.auth;

import lombok.Data;

@Data
public class RegisterDto {
    private String name;
    private String email;
    private String password;
}
