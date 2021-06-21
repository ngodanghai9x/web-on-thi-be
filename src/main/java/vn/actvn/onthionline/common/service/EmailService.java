package vn.actvn.onthionline.common.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import vn.actvn.onthionline.service.dto.EmailDto;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.stream.Collectors;

@Service
public class EmailService {

    private final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender emailSender;

    /**
     * Method for sending simple e-mail message.
     * @param emailDTO - data to be send.
     */
    public Boolean sendMessage(EmailDto emailDTO) throws MessagingException {
        MimeMessage mailMessage = emailSender.createMimeMessage();
        boolean multipart = true;
        MimeMessageHelper helper = new MimeMessageHelper(mailMessage, multipart, "UTF-8");

        helper.setTo(emailDTO.getRecipients().stream().collect(Collectors.joining(",")));
        mailMessage.setSubject(emailDTO.getSubject());
        mailMessage.setContent(emailDTO.getBody(), "text/html;charset=UTF-8");

        Boolean isSent = false;
        try
        {
            emailSender.send(mailMessage);
            isSent = true;
        }
        catch (Exception e) {
            LOGGER.error("Sending e-mail error: {}", e.getMessage());
        }
        return isSent;
    }


}
