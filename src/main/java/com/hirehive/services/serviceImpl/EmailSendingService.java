package com.hirehive.services.serviceImpl;

import com.hirehive.constants.ProjectConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSendingService {

    @Autowired
    private JavaMailSender javaMailSender;
//    String subject = "Successfully created";
//    String text = "Thankyou for creating an account at Our Platform";

    public String sendEmail(String to){
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(ProjectConstants.subject);
        simpleMailMessage.setText(ProjectConstants.text);
        javaMailSender.send(simpleMailMessage);
        return "Email send successfully!";
    }

}
