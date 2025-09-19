package com.microservice.notification_service.service;


import com.microservice.notification_service.event.UserEvent;
import com.microservice.notification_service.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationService {

    @Autowired
    private JavaMailSender javaMailSender;

    @KafkaListener(topics = "user-topic")
    public void sendEmail(String message){

        var userEvent = JsonUtil.fromJson(message, UserEvent.class);

        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("rauljosema70@gmail.com");
            messageHelper.setTo(userEvent.email());
            messageHelper.setSubject("Probando kafka");
            messageHelper.setText("Hola "+userEvent.name()+ " tu cuenta se ha registrado correctamente");
        };
        try {
            javaMailSender.send(messagePreparator);
        }catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
