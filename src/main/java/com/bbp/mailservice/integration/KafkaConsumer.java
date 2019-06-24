package com.bbp.mailservice.integration;

import com.bbp.mailservice.service.EmailSenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class KafkaConsumer {

    private final EmailSenderService emailSenderService;

    @KafkaListener(topics = "${kafka.topics.email}",
            containerFactory = "kafkaListenerContainerFactory",
            groupId = "${kafka.topics.email}")
    public void emailListener(@Payload EmailDto emailDto) {
        emailSenderService.sendEmailFromTemplate(emailDto);
    }
}
