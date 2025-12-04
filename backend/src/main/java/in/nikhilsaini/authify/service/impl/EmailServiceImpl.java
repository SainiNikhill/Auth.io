package in.nikhilsaini.authify.service.impl;

import in.nikhilsaini.authify.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper; // NEW IMPORT
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage; // NEW IMPORT
import jakarta.mail.MessagingException; // NEW IMPORT

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    @Value("${spring.mail.username}")
    private String fromEmail;

    private final JavaMailSender mailSender;
    private final String APP_NAME = "Authify";

    @Async
    @Override
    public void sendVerificationOtp(String toEmail, String otp) {
        String subject = "Verify your email - " + APP_NAME;
        // HTML Body for better formatting
        String htmlBody = "<p>Your verification OTP is: <b>" + otp + "</b></p>"
                + "<p>This OTP will expire in 10 minutes.</p>"
                + "<p>Thank you for registering with " + APP_NAME + "!</p>";

        sendHtmlEmail(toEmail, subject, htmlBody);
    }

    @Async
    @Override
    public void sendPasswordResetOtp(String toEmail, String otp) {
        String subject = "Reset your Password - " + APP_NAME;
        // HTML Body for better formatting
        String htmlBody = "<p>Your password reset OTP is: <b>" + otp + "</b></p>"
                + "<p>This OTP will expire in 10 minutes.</p>"
                + "<p>If you did not request this, please ignore this email.</p>";

        sendHtmlEmail(toEmail, subject, htmlBody);
    }

    @Async
    @Override
    public void sendWelcomeEmail(String toEmail, String name) {
        String subject = "Welcome to " + APP_NAME + "!";
        // HTML Body for better formatting and emojis
        String htmlBody = "<h3>Hello " + name + ",</h3>"
                + "<p>Your email has been successfully verified.</p>"
                + "<h1>Welcome to " + APP_NAME + " 🎉</h1>"
                + "<p>You can now log in and start using your account.</p>"
                + "<p>Regards,<br/>Authify Team ❤️</p>";

        sendHtmlEmail(toEmail, subject, htmlBody);
    }

    // --------------------------------
    // Private Helper Method (Updated for HTML)
    // --------------------------------

    private void sendHtmlEmail(String to, String subject, String htmlBody) {
        MimeMessage message = mailSender.createMimeMessage();

        try {
            // MimeMessageHelper handles complex headers and HTML content.
            // Pass 'true' to enable multipart mode (required for HTML/attachments)
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            // Pass 'true' to setText to indicate the content is HTML
            helper.setText(htmlBody, true);

            mailSender.send(message);
            System.out.println("HTML Email sent successfully to: " + to);

        } catch(MessagingException e) {
            // Log the specific JavaMail error
            System.err.println("Email failed to send due to MimeMessage issue!");
            e.printStackTrace();
        } catch(Exception e) {
            // General exception catching is important for connection/authentication failures
            System.err.println("Email failed to send! Check SMTP Configuration and App Password.");
            e.printStackTrace();
        }
    }
}