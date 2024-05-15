package ssau.graduatework.attractionsearchservice.user.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ssau.graduatework.attractionsearchservice.user.User;
import ssau.graduatework.attractionsearchservice.user.UserRepository;
import ssau.graduatework.attractionsearchservice.user.UserService;
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    @Override
    public String getMyName() {
        User user = (User)(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        return user.getName();
    }

    @Override
    public Page<User> getUsersByPageable(Pageable pageable) {
        return userRepository.findAll(pageable);
    }
}
