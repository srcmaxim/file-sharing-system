package github.srcmaxim.filesharingsystem.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import java.util.Properties;

import static github.srcmaxim.filesharingsystem.util.PropertyHolder.getVariableOrException;

@Component
@PropertySource("classpath:mail.properties")
public class MailConfig {

    @Value("${mail.protocol}")
    private String protocol;
    @Value("${mail.host}")
    private String host;
    @Value("${mail.port}")
    private int port;

    private String username;
    private String password;

    @Autowired
    public MailConfig(Environment env) {
        this.username = getVariableOrException(env,"MAIL_USER", "mail.username");
        this.password = getVariableOrException(env,"MAIL_PWD", "mail.password");
    }

    @Bean
    public JavaMailSender mailSender() {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setProtocol(protocol);
        sender.setHost(host);
        sender.setPort(port);
        sender.setUsername(username);
        sender.setPassword(password);
        sender.setJavaMailProperties(mailProperties());
        return sender;
    }

    private static Properties mailProperties() {
        Properties mailProps = new Properties();
        mailProps.put("mail.smtps.auth", "true");
        mailProps.put("mail.smtp.starttls.enable", "true");
        mailProps.put("mail.smtp.debug", "true");
        return mailProps;
    }

}
