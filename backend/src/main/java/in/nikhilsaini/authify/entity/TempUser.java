package in.nikhilsaini.authify.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TempUser {

    @Id
    private String email;
    private String name;
    private String password;
    private String otp;
    private LocalDateTime otpGeneratedAt;
    private LocalDateTime otpExpiry;
}
