package br.com.achimid.notfy.service;

import br.com.achimid.notfy.mail.MailModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Async
    private void sendSimpleHtmlMail(MailModel mail, String html) throws MessagingException, IOException {
        MimeMessage message = emailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
       //helper.addAttachment("logo.png", new ClassPathResource("memorynotfound-logo.png"));

        if(html == null) {
            Context context = new Context();
            context.setVariables(mail.getTemplate().getModel());
            html = templateEngine.process(mail.getTemplate().getTemplatePath(), context);
        }

        helper.setTo(mail.getTo());
        helper.setText(html, true);
        helper.setSubject(mail.getSubject());
        helper.setFrom(mail.getFrom());

        emailSender.send(message);
        log.info("Email enviado para {} ................................................", mail.getTo());
    }

    @Async
    public void sendSimpleHtmlMail(MailModel mail) throws MessagingException, IOException {
        this.sendSimpleHtmlMail(mail, null);
    }

}