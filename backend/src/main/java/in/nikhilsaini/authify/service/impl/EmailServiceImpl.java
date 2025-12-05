package in.nikhilsaini.authify.service.impl;

import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import in.nikhilsaini.authify.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.sendgrid.*;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    @Value("${app.mail.from}")
    private String fromEmail;

    @Value("${sendgrid.api.key}")
    private String sendGridApiKey;

    private final String APP_NAME = "Authify";

    @Async
    @Override
    public void sendVerificationOtp(String toEmail, String otp) {
        String subject = "Verify your email - " + APP_NAME;
        String htmlBody = "<p>Your verification OTP is: <b>" + otp + "</b></p>"
                + "<p>This OTP will expire in 10 minutes.</p>"
                + "<p>Thank you for registering with " + APP_NAME + "!</p>";

        sendHtmlEmail(toEmail, subject, htmlBody);
    }

    @Async
    @Override
    public void sendPasswordResetOtp(String toEmail, String otp) {
        String subject = "Reset your Password - " + APP_NAME;
        String htmlBody = "<p>Your password reset OTP is: <b>" + otp + "</b></p>"
                + "<p>This OTP will expire in 10 minutes.</p>"
                + "<p>If you did not request this, please ignore this email.</p>";

        sendHtmlEmail(toEmail, subject, htmlBody);
    }

    @Async
    @Override
    public void sendWelcomeEmail(String toEmail, String name) {
        String subject = "Welcome to " + APP_NAME + "! 🎉";
        String htmlBody = "<h3>Hello " + name + ",</h3>"
                + "<p>Your email has been successfully verified.</p>"
                + "<h1>Welcome to " + APP_NAME + " ❤️</h1>"
                + "<p>You can now log in and start using your account.</p>"
                + "<p>Regards,<br/>Authify Team</p>";

        sendHtmlEmail(toEmail, subject, htmlBody);
    }

    private void sendHtmlEmail(String to, String subject, String htmlBody) {

        Email from = new Email(fromEmail);
        Email toEmail = new Email(to);
        Content content = new Content("text/html", htmlBody);

        Mail mail = new Mail(from, subject, toEmail, content);
        SendGrid sg = new SendGrid(sendGridApiKey);

        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sg.api(request);

            System.out.println("Email sent via SendGrid! Status Code: " + response.getStatusCode());
            System.out.println("Response Body: " + response.getBody());

        } catch (IOException e) {
            System.err.println("Failed to send email via SendGrid!");
            e.printStackTrace();
        }
    }
}
