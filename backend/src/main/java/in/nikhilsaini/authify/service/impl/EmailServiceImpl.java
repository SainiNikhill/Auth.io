package in.nikhilsaini.authify.service.impl;

import in.nikhilsaini.authify.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final  String APP_NAME = "Authify";

    @Async
    @Override
    public void sendVerificationOtp(String toEmail, String otp) {
        String subject = "Verify your email-" + APP_NAME;
        String body = "Your verification OTP is: " + otp + "\n\n"+ "this OTP will expire in 10  minutes.\n"
                +"Thank you for registrating with "+ APP_NAME+ "!";

        sendEmail(toEmail,subject,body);

    }
    @Async
    @Override
    public void sendPasswordResetOtp(String toEmail, String otp) {
        String subject = "Reset your Password -" + APP_NAME;
        String body= "Your password reset OTP is: " + otp + "\n\n"
                + "This OTP will expire in 10 minutes.\n"
                + "If you did not request this, please ignore this email.";
        sendEmail(toEmail,subject,body);

    }
    @Async
    @Override
    public void sendWelcomeEmail(String toEmail, String name){
        String subject = "Welcome to " + APP_NAME + "!";
        String body = "Hello " + name + ",\n\n"
                + "Your email has been successfully verified.\n"
                + "Welcome to " + APP_NAME + "!\n\n"
                + "You can now log in and start using your account.\n\n"
                + "Regards,\nAuthify Team ❤️";

        sendEmail(toEmail, subject, body);

    }

    // --------------------------------
    // Private Helper Method
    // --------------------------------

    private void sendEmail(String to, String subjet, String body)
    {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subjet);
        message.setText(body);
        message.setFrom("no-reply@authify.com");

        mailSender.send(message);
    }


}
