package com.ftn.sbnz.app.core.service.impl;

import com.ftn.sbnz.app.core.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RequiredArgsConstructor
@Service
public class DefaultMailService implements MailService {

    private final JavaMailSender mailSender;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    private final String SENDER = "ubert472@gmail.com";

    @Override
    public void sendTextEmail(String subject, String receiver, String emailContent) {
        executorService.execute(() -> sendRegularMail(subject, receiver, emailContent));
    }

    private void sendRegularMail(String subject, String receiver, String emailContent) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(SENDER);
        message.setTo(receiver);
        message.setSubject(subject);
        message.setText(emailContent);
        mailSender.send(message);
    }
}
