package in.nikhilsaini.authify.service.impl;

import in.nikhilsaini.authify.entity.User;
import in.nikhilsaini.authify.repository.UserRepository;
import in.nikhilsaini.authify.service.UserService;
import jakarta.validation.constraints.Null;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl  implements UserService {

    private final UserRepository userRepository;


    @Override
    public User findByEmail(String email){
        return userRepository.findByEmail(email).orElse(null);

    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }
    @Override
    public boolean existsByEmail(String email){
        return userRepository.existsByEmail(email);
    }

    @Override
    public void updateUser(User user) {
        userRepository.save(user);
    }
}
