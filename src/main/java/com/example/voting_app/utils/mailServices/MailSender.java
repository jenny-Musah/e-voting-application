package com.example.voting_app.utils.mailServices;

import jakarta.mail.MessagingException;

public interface MailSender {

    void send(String to, String email, String subject) throws MessagingException;

    String buildEmail(long loginID, String password);

}
