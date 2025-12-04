package in.nikhilsaini.authify.service;

import in.nikhilsaini.authify.entity.User;

public  interface UserService {

    User findByEmail(String email);
    User saveUser(User user);
    boolean existsByEmail(String email);

    void updateUser(User user);
}
