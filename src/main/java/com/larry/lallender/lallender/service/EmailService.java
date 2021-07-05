package com.larry.lallender.lallender.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender emailSender;

    public void send(String to, String subject, String text) {
        MimeMessagePreparator preparator = message -> {
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setFrom("noreply@baeldung.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, true);
        };
        emailSender.send(preparator);
    }
}
