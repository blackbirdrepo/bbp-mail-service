package com.bbp.mailservice.controller;

import com.bbp.mailservice.service.EmailSenderService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;


@AllArgsConstructor
@RestController("/api")
public class MailController {

    private EmailSenderService emailSenderService;

}
