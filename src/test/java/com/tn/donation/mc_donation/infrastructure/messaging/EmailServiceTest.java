package com.tn.donation.mc_donation.infrastructure.messaging;

import com.tn.donation.mc_donation.application.security.JwtService;
import com.tn.donation.mc_donation.common.exception.EmailSendingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    JavaMailSender mailSender;

    @InjectMocks
    EmailService emailService;

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(emailService, "verifyUrl", "http://localhost/test");
    }

    @Test
    void sendVerificationEmail_ShouldSendEmailSuccessfully() {
        String token = "a81b2d73-5de4-4495-b851-98ab3c69ddb6";
        String email = "testuser@test.com";

        MimeMessage mimeMessage = mock(MimeMessage.class);

        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        emailService.sendVerificationEmail(email, token);

        verify(mailSender).createMimeMessage();
        verify(mailSender).send(mimeMessage);
    }

    @Test
    void sendVerificationEmail_ShouldThrowEmailSendingException() {
        String token = "a81b2d73-5de4-4495-b851-98ab3c69ddb6";
        String email = "testuser@test.com";

        MimeMessage mimeMessage = mock(MimeMessage.class);

        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        doThrow(new MailSendException("Simulated failure"))
                .when(mailSender)
                .send(mimeMessage);

        EmailSendingException ex = assertThrows(
                EmailSendingException.class,
                () -> emailService.sendVerificationEmail(email, token)
        );

        assertEquals("Failed to send verification email", ex.getMessage());

        verify(mailSender).createMimeMessage();
        verifyNoMoreInteractions(mailSender);
    }
}
