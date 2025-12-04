package in.nikhilsaini.authify.service;

import in.nikhilsaini.authify.dto.*;

public interface AuthService {
   AuthResponse register (RegisterRequest request);
   AuthResponse  verifyOtp(OtpRequest request);
   AuthResponse resendOtp(String email);
   AuthResponse login(LoginRequest request);
   AuthResponse forgotPassword(ForgotPasswordRequest request);
   AuthResponse resetPassword(ResetPasswordRequest request);
   AuthResponse refreshToken(String refreshToken);
}
