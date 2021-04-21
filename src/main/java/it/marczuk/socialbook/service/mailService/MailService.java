package it.marczuk.socialbook.service.mailService;

public interface MailService {

    void sendEmail(String to, String subject, String text);
}
