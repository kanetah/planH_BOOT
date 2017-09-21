package top.kanetah.planH.aspect;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;

@Component
public aspect SendMailServiceAspect {

    private static Logger logger = LoggerFactory.getLogger(SendMailServiceAspect.class);
    private String mailAddress;

    public SendMailServiceAspect() {
        try {
            InputStream in = null;
            try {
                Properties prop = new Properties();
                in = this.getClass().getClassLoader()
                        .getResourceAsStream("application.properties");
                prop.load(in);
                mailAddress = prop.getProperty("spring.mail.username");
            } finally {
                if (in != null)
                    in.close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    pointcut setMessageTo(String to):
            call(* org.springframework.mail.javamail.MimeMessageHelper.setTo(String)) && args(to);

    before(String to): setMessageTo(to) {
        try {
            logger.info("Into a join point for set the e-mail message from address.");
            MimeMessageHelper helper = (MimeMessageHelper) thisJoinPoint.getTarget();
            helper.setFrom(mailAddress);
            logger.info("Before send the e-mail to '" + to + "', set mail form '" + mailAddress + "'.");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    pointcut sendMessage(MimeMessage message):
            call(* org.springframework.mail.javamail.JavaMailSenderImpl.send(javax.mail.internet.MimeMessage)) && args(message);

    after(MimeMessage message): sendMessage(message) {
        try {
            JavaMailSenderImpl mailSender = (JavaMailSenderImpl) thisJoinPoint.getTarget();
            logger.info("Send e-mail '" + message.getSubject() +
                    "' to '" + Arrays.deepToString(message.getReplyTo()) + "', smtp server host: '" +
                    mailSender.getHost() + ":" + mailSender.getPort() + "'."
            );
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
