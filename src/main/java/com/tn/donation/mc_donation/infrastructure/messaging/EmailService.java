package com.tn.donation.mc_donation.infrastructure.messaging;

import com.tn.donation.mc_donation.common.exception.EmailSendingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    @Value("${messaging.verification-url}")
    String verifyUrl;

    private final JavaMailSender mailSender;

    private void sendHtmlEmail(String to, String subject, String htmlContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            helper.setFrom("no-reply@donationflows.com");

            mailSender.send(message);
        } catch (Exception e) {
            throw new EmailSendingException("Failed to send verification email", e);
        }
    }

    public void sendVerificationEmail(String to, String token) {
        String url = verifyUrl + "?token=" + token;

        String html = """
                <html>
                <body style="font-family: Arial, sans-serif; padding: 20px;">
                    <h2>Welcome to DonationFlows!</h2>
                    <p>Please verify your email by clicking the button below:</p>
                    
                    <a href="%s"
                       style="background-color:#2563eb;color:white;padding:12px 20px;
                       border-radius:6px;text-decoration:none;font-size:16px;">
                        Verify Email
                    </a>
                    
                    <p style="margin-top:30px;">If you didn’t create this account, simply ignore this email.</p>
                </body>
                </html>
                """.formatted(url);

        sendHtmlEmail(to, "Verify your DonationFlows account", html);
    }
}
