 package com.example.demo.services;

import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class MailService {
	
    final static String username = "chieudantan@gmail.com";
    
    private final JavaMailSender javaMailSender ;
    
    public MailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml, String path) {
//    	String to : gui den dia chi
//    	String subject : tieu de mail
//    	String content : noi dungf mailk
//    	path link dan file 
        // Prepare message using a Spring helper
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
//        if(path!=null||path.equals("")) {
//        FileSystemResource file = new FileSystemResource(path);
//        	}
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, StandardCharsets.UTF_8.name());
            message.setTo(to);
            message.setFrom(username);
            message.setSubject(subject);
            System.out.println("subject: "+subject);
            message.setText(content, isHtml);
            System.out.println("content: "+content);
//            doctor send pdf to patients
            if(path!=null) {
                FileSystemResource file = new FileSystemResource(path);
                message.addAttachment("pdf", file);
            }
            javaMailSender.send(mimeMessage);
//            log.debug("Sent email to User '{}'", to);
        } catch (MailException | MessagingException e) {
//            log.warn("Email could not be sent to user '{}'", to, e);
        	e.printStackTrace();
        }
    }

}
