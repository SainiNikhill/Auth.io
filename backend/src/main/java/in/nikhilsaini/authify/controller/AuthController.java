package in.nikhilsaini.authify.controller;


import in.nikhilsaini.authify.dto.*;
import in.nikhilsaini.authify.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    //Testing
    @GetMapping("/test")
    public String test(){
        return "works";
    }

    // Register
    @PostMapping("/register")
    public ResponseEntity<AuthResponse>register (@RequestBody RegisterRequest request){
        return ResponseEntity.ok(authService.register(request));
    }

    // Verify-otp
    @PostMapping("/verify-otp")
    public ResponseEntity<AuthResponse> verifyOtp(@RequestBody OtpRequest request){
        return ResponseEntity.ok(authService.verifyOtp(request));
    }

    // Resend OTP
    @PostMapping("/resend-otp")
    public ResponseEntity<AuthResponse> resendOtp(@RequestParam String email ){
        return ResponseEntity.ok(authService.resendOtp(email));

    }
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request){
        return ResponseEntity.ok(authService.login(request));
    }

    // Forgot Password
    @PostMapping("/forgot-password")
    public ResponseEntity<AuthResponse> forgotPassword(@RequestBody ForgotPasswordRequest request){
        return ResponseEntity.ok(authService.forgotPassword(request));
    }

    // Reset Password
    @PostMapping("/reset-password")
    public ResponseEntity<AuthResponse> resetPassword(@RequestBody ResetPasswordRequest request){
        return ResponseEntity.ok(authService.resetPassword(request));
    }

    // Refresh Token
    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponse> refreshToken(@RequestParam String refreshToken){
        return ResponseEntity.ok(authService.refreshToken(refreshToken));
    }
}