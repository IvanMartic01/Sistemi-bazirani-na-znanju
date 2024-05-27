package com.ftn.sbnz.app.core.service;

public interface MailService {
    void sendTextEmail(String subject, String receiver, String emailContent);
}
