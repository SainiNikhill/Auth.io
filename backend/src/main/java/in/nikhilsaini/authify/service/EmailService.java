package in.nikhilsaini.authify.service;

public interface EmailService {

    void sendVerificationOtp(String toEmail , String otp);
    void sendPasswordResetOtp(String toEmail,String otp);
    void sendWelcomeEmail(String toEmail, String name);

}
