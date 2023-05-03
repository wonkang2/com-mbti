package com.commbti.global.mail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Random;
import java.util.UUID;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class MailService {
    private static final String PASSWORD_MAIL_TITLE = "[Com-MBTI] 임시 비밀번호 발급 안내입니다.";
    private static final String AUTH_NUMBER_MAIL_TITLE = "[Com-MBTI] 인증번호 안내입니다.";
    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;

    public void sendNewPassword(String to, String newPassword) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject(PASSWORD_MAIL_TITLE);
            mimeMessageHelper.setText(templateEngine.process("/mail/password", setContext(newPassword, "PASSWORD")), true);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.warn("메일 전송 실패! - {}", e);
            throw new RuntimeException(e);
        }
    }

    public String sendAuthNumber(String to) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        String authNumber = createAuthNumber();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject(AUTH_NUMBER_MAIL_TITLE);
            mimeMessageHelper.setText(templateEngine.process("/mail/authnumber", setContext(authNumber, "AUTH_NUMBER")), true);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.warn("메일 전송 실패! - {}", e);
            throw new RuntimeException(e);
        }
        return authNumber;
    }

    private String createAuthNumber() {
        Random random = new Random();
        StringBuilder authNumber = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            String num = String.valueOf(random.nextInt(9));
            authNumber.append(num);
        }
        return authNumber.toString();
    }

    protected Context setContext(String content, String type) {
        Context context = new Context();
        if (type.equals("PASSWORD")) {
            context.setVariable("newPassword", content);
        } else if (type.equals("AUTH_NUMBER")) {
            context.setVariable("authNumber", content);
        }
        return context;
    }
}
