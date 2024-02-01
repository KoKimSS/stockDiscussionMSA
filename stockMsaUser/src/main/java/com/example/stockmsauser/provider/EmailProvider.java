package com.example.stockmsauser.provider;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;


@Component
@RequiredArgsConstructor
public class EmailProvider {

    private final JavaMailSender javaMailSender;
    private final String SUBJECT = "[StockDiscussionRoom 서비스] 인증메일 입니다.";

    public boolean sendCertificationMail(String email,String certificationNumber) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);
            String htmlContent = getCertificationMessage(certificationNumber);
            messageHelper.setTo(email);
            messageHelper.setSubject(SUBJECT);
            messageHelper.setText(htmlContent,true);
            javaMailSender.send(message);
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
        return true;
    }

    private String getCertificationMessage(String certificationNumber) {
        String certificationMessage = "";
        certificationMessage += "<h1 style='text-align: center;'>[StockDiscussionRoom 서비스] 인증 메일</h1>";
        certificationMessage += "<h3 style='text-align: center;'> 인증코드 : <strong style='font-size:32px; letter-spacing: 8px;'>";
        certificationMessage += certificationNumber + "</strong></h3>";

        return certificationMessage;
    }
}
