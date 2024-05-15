package ssau.graduatework.attractionsearchservice.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    String getMyName();

    Page<User> getUsersByPageable(Pageable pageable);
}
