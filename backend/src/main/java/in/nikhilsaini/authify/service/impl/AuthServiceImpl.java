package in.nikhilsaini.authify.service.impl;

import in.nikhilsaini.authify.dto.*;
import in.nikhilsaini.authify.entity.TempUser;
import in.nikhilsaini.authify.entity.User;
import in.nikhilsaini.authify.enums.AuthProvider;
import in.nikhilsaini.authify.enums.Role;
import in.nikhilsaini.authify.repository.TempUserRepository;
import in.nikhilsaini.authify.repository.UserRepository;
import in.nikhilsaini.authify.security.JwtUtil;
import in.nikhilsaini.authify.service.AuthService;
import in.nikhilsaini.authify.service.EmailService;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;


@Service
@RequiredArgsConstructor
@Builder
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final TempUserRepository tempUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;

    private String generateOtp(){
        return String.valueOf(100000 + new Random().nextInt(900000));
    }

    private LocalDateTime otpExpiry(){
        return LocalDateTime.now().plusMinutes(10);
    }

    private UserDto mapToUserDto(User user){
        return UserDto.builder()
                .id(user.getId()).name(user.getName()).email(user.getEmail()).role(user.getRole().name()).build();
    }


    // REGISTER

    @Override
    public AuthResponse register(RegisterRequest request) {
        if(userRepository.existsByEmail(request.getEmail())){
            return AuthResponse.builder()
                    .success(false)
                    .message("User with this email address already exists!")
                    .build();
        }

        TempUser temp = tempUserRepository.findById(request.getEmail()).orElse(null);
        String otp = generateOtp();

        if(temp!=null){
            temp.setOtp(otp);
            temp.setOtpGeneratedAt(LocalDateTime.now());
            temp.setOtpExpiry(otpExpiry());

            tempUserRepository.save(temp);
            emailService.sendVerificationOtp(temp.getEmail(),otp);
            return AuthResponse.builder()
                    .success(true)
                    .message("OTP sent again! Please verify your email.")
                    .build();

        }
        TempUser tempUser = TempUser.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .otp(otp)
                .otpGeneratedAt(LocalDateTime.now())
                .otpExpiry(otpExpiry())
                .build();

        tempUserRepository.save(tempUser);

        emailService.sendVerificationOtp(tempUser.getEmail(), otp);

        return AuthResponse.builder().success(true).message("User Registered ! Please Verify your email.")
                .build();
    }



    // Verify OTP


    @Override
    public AuthResponse verifyOtp(OtpRequest request) {
        TempUser temp = tempUserRepository.findById(request.getEmail()).orElse(null);
        if(temp == null){
            return AuthResponse.builder().success(false).message("user not found").build();
        }
        if(!temp.getOtp().equals(request.getOtp())){
            return AuthResponse.builder().success(false).message("Invalid OTP!").build();
        }
        if(temp.getOtpExpiry().isBefore(LocalDateTime.now())){
            return AuthResponse.builder().success(false).message("OTP Expired!").build();
        }

        //create a real user
        User user = User.builder()
                .email(temp.getEmail())
                        .name(temp.getName())
                                .password(temp.getPassword())
                                        .emailVerified(true)
                                                .provider(AuthProvider.LOCAL)
                                                        .role(Role.USER)
                                                                .accountNonLocked(true)

                                                                        .build();
        userRepository.save(user);

        //delete temp user
        tempUserRepository.delete(temp);
        // SEND WELCOME EMAIL
        emailService.sendWelcomeEmail(user.getEmail(),user.getName());

        //Generate Tokens
        String access = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
        String refresh = jwtUtil.generateRefreshToken(user.getEmail());
        return AuthResponse.builder()
                .success(true)
                .message("Email Verified Successfully")
                .token(access)
                .refreshToken(refresh)
                .role(user.getRole().name())
                .user(mapToUserDto(user))
                .build();

    }
    // -------------------------------------
    // RESEND OTP
    // -------------------------------------

    @Override
    public AuthResponse resendOtp(String email) {
       User user = userRepository.findByEmail(email).orElse(null);
       if(user == null) return AuthResponse.builder().success(false).message("user not found").build();

       String newOtp = generateOtp();
       user.setOtp(newOtp);
       user.setOtpExpiry(otpExpiry());
       userRepository.save(user);

       emailService.sendVerificationOtp(email, newOtp);
       return AuthResponse.builder()
               .success(true)
               .message("OTP RESENT SUCCESSFULLY")
               .build();

    }

    // ----------------------------------------
    //      LOGIN
    // ----------------------------------------

    @Override
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail()).orElse(null);

        if(user==null)
            return AuthResponse.builder().success(false).message("user  not found ").build();
        if(!passwordEncoder.matches(request.getPassword(),user.getPassword()))
            return AuthResponse.builder().success(false).message("invalid password").build();
        if(!user.isEmailVerified())
            return AuthResponse.builder().success(false).message("Please verify your email first").build();

        String  access = jwtUtil.generateToken(user.getEmail(),user.getRole().name());
        String refresh = jwtUtil.generateRefreshToken((user.getEmail()));

        return AuthResponse.builder()
                .success(true)
                .message("Login successful!")
                .token(access)
                .refreshToken(refresh)
                .role(user.getRole().name())
                .user(mapToUserDto(user))
                .build();
    }
    // ---------------------------------------------
    // FORGOT EMAIL
    // ---------------------------------------------
    @Override
    public AuthResponse forgotPassword(ForgotPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail()).orElse(null);

        if(user == null )
            return AuthResponse.builder().success(false).message("user not found").build();

        String resetOtp = generateOtp();
        user.setResetOtp(resetOtp);
        user.setResetOtpExpiry(otpExpiry());
        userRepository.save(user);

        emailService.sendPasswordResetOtp(user.getEmail(),resetOtp);

        return AuthResponse.builder()
                .success(true)
                .message("reset otp sent to your mail")
                .build();

    }

    // -------------------------------------------------
    //               RESET PASSWORD
    // ------------------------------------------------

    @Override
    public AuthResponse resetPassword(ResetPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail()).orElse(null);

        if(user == null)
            return AuthResponse.builder().success(false).message("user not found").build();

        if(!request.getOtp().equals(user.getResetOtp()))
            return AuthResponse.builder().success(false).message("invalid OTP").build();
        if(user.getResetOtpExpiry().isBefore(LocalDateTime.now()))
            return AuthResponse.builder().success(false).message("OTP Expired").build();

        user.setPassword((passwordEncoder.encode((request.getNewPassword()))));
        user.setResetOtp(null);
        user.setResetOtpExpiry(null);
        userRepository.save(user);

        return AuthResponse.builder()
                .success(true)
                .message("Password reset successfully")
                .build();
    }

    // ------------------------------------------------
    // REFRESH TOKEN
    // -----------------------------------------------


    @Override
    public AuthResponse refreshToken(String refreshToken) {
        String email = jwtUtil.extractEmail(refreshToken);
        User user = userRepository.findByEmail(email).orElse(null);
        if(user == null)
            return AuthResponse.builder().success(false).message("user not found").build();
        if(!jwtUtil.isTokenValid(refreshToken , user.getEmail()))
            return AuthResponse.builder().success(false).message("Invalid refresh token").build();

        String newAccess = jwtUtil.generateToken(email, user.getRole().name());
        return AuthResponse.builder()
                .success(true)
                .message("Token refreshed!")
                .token(newAccess)
                .refreshToken(refreshToken)
                .role(user.getRole().name())
                .user(mapToUserDto(user))
                .build();

    }
}
