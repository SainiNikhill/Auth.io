package in.nikhilsaini.authify.repository;

import in.nikhilsaini.authify.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional <User> findByEmail(String email);  // For Login and Verification
    boolean existsByEmail(String email); // Prevent Duplicate Registration
    Optional<User> findByOtp(String otp);
    Optional<User>findByResetOtp(String resetOtp);
}
