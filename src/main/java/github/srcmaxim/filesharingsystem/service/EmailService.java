package github.srcmaxim.filesharingsystem.service;

import github.srcmaxim.filesharingsystem.annotation.Loggable;
import github.srcmaxim.filesharingsystem.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@Loggable
public class EmailService {

    private JavaMailSender  mailSender;

    @Autowired
    public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendPasswordTo(User user) throws MessagingException {
        String emailMessage = "Dear " + user.getFirstName() + " "
                + user.getLastName()
                + ", your new password is "
                + user.getPassword();

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper;
        helper = new MimeMessageHelper(message, true);
        helper.setTo(user.getEmail());
        helper.setSubject("New password for File Sharing System");
        helper.setText(emailMessage, true);
        mailSender.send(message);
    }

}
