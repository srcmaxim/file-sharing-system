package github.srcmaxim.filesharingsystem.service;

import github.srcmaxim.filesharingsystem.annotation.Loggable;
import github.srcmaxim.filesharingsystem.model.User;
import github.srcmaxim.filesharingsystem.model.VerificationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@Loggable
public class EmailService {

    private JavaMailSender mailSender;

    @Autowired
    public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendPasswordTo(User user) throws MessagingException {
        send(user.getEmail(),
                "New password for File Sharing System",
                "Dear " + user.getFirstName() + " "
                        + user.getLastName()
                        + ", your new password is "
                        + user.getPassword());
    }

    public void sendVerificationToken(VerificationToken token) throws MessagingException {
        User user = token.getUser();
        send(user.getEmail(),
                "Verification token for File Sharing System",
                "Dear " + user.getFirstName() + " " + user.getLastName()
                        + ", your token is "
                        + token.getToken());
    }

    private void send(String email, String emailSubject, String emailMessage) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper;
        helper = new MimeMessageHelper(message, true);
        helper.setTo(email);
        helper.setSubject(emailSubject);
        helper.setText(emailMessage, true);
        mailSender.send(message);
    }

}
