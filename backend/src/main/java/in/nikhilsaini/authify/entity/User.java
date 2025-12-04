package in.nikhilsaini.authify.entity;

import in.nikhilsaini.authify.enums.AuthProvider;
import in.nikhilsaini.authify.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.time.LocalDateTime;


@Entity
@Table(name="tbl_users")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true , nullable = false)
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    private boolean emailVerified = false;

    private String otp; // valid for only few minutes
    private LocalDateTime otpGeneratedAt;
    private LocalDateTime otpExpiry;   // 5-10 minutes

    private String resetOtp;           // Password reset OTP
    private LocalDateTime resetOtpExpiry;


    @Enumerated(EnumType.STRING)
    private AuthProvider provider; // Local  , Google , GitHub


    private boolean accountNonLocked = true;


    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdAt;

    @UpdateTimestamp
    private Timestamp updatedAt;

}
