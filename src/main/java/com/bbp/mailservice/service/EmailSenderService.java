package com.bbp.mailservice.service;

import com.bbp.mailservice.integration.EmailDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class EmailSenderService {

    private final JavaMailSender emailSender;

    private final SpringTemplateEngine templateEngine;

    private final Logger log = LoggerFactory.getLogger(EmailSenderService.class);

    @Value("${spring.mail.username}")
    private String from;

    private Map<String, String> emailTypes = Map.of("activation", "mail/activationEmail", "creation", "mail/creationEmail");
    private Map<String, String> emailSubjects = Map.of("activation", "Please activate your account", "creation", "Account was successfully created");

    public void sendEmailFromTemplate(EmailDto emailDto) {
        var to = emailDto.getEmailAddress();
        var subject = getSubject(emailDto.getEmailType());
        var content = getContent(emailDto.getEmailType(), emailDto.getContentProperties());
        sendEmail(to, subject, content, false, true);
    }

    private void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        log.debug("Send email[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}",
                isMultipart, isHtml, to, subject, content);

        // Prepare message using a Spring helper
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, StandardCharsets.UTF_8.name());
            message.setTo(to);
            message.setFrom(from);
            message.setSubject(subject);
            message.setText(content, true);
            emailSender.send(mimeMessage);
            log.debug("Sent email to User '{}'", to);
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.warn("Email could not be sent to address '{}'", to, e);
            } else {
                log.warn("Email could not be sent to address '{}': {}", to, e.getMessage());
            }
        }
    }

    private String getContent(String emailType, Map<String, Object> contentProperties) {
        Context context = new Context();
        context.setVariables(contentProperties);
        String templateName = emailTypes.get(emailType);
        return templateEngine.process(templateName, context);
    }

    private String getSubject(String emailType) {
        return emailSubjects.get(emailType);
    }
}
