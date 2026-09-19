package com.tamisa.superadmin.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    // Temporary OTP Store (Development)
    private final Map<String, String> otpStore = new HashMap<>();

    // Existing HTML Mail
    public void sendHtmlMail(String to, String subject, String body) {

        try {

            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);

            mailSender.send(message);

        } catch (MessagingException e) {

            throw new RuntimeException(e.getMessage());

        }
    }

   
    public void sendOtp(String email, String partnerName) {

        try {

            String otp = String.valueOf(100000 + new Random().nextInt(900000));

            otpStore.put(email, otp);

            String subject = "Email Verification OTP";

            String body = """
            <html>

            <body style="margin:0;padding:0;background:#f4f6f9;font-family:Arial,sans-serif;">

            <div style="max-width:650px;margin:30px auto;background:#ffffff;
            border-radius:10px;overflow:hidden;
            box-shadow:0 4px 12px rgba(0,0,0,0.15);">

            <div style="background:#0d6efd;color:white;padding:20px;text-align:center;">
            <h2 style="margin:0;">Email Verification</h2>
            </div>

            <div style="padding:30px;">

            <p>Dear <b>%s</b>,</p>

            <p>
            Thank you for registering as a <b>BBB Plus Channel Partner</b>.
            </p>

            <p>
            Please use the following OTP to verify your email.
            </p>

            <div style="
            background:#eaf4ff;
            border-left:5px solid #0d6efd;
            padding:18px;
            margin:25px 0;
            border-radius:5px;
            text-align:center;">

            <p>Your Verification OTP</p>

            <h1 style="font-size:40px;
            letter-spacing:8px;
            color:#0d6efd;">

            %s

            </h1>

            <p>Valid for 5 Minutes</p>

            </div>

            <p>
            <b>Security Tips</b>
            </p>

            <ul>
            <li>Never share this OTP with anyone.</li>
            <li>BBB Plus will never ask for your OTP.</li>
            </ul>

            <br>

            <p>
            Regards,<br>
            <b>BBB Plus Admin Team</b>
            </p>

            </div>

            <div style="
            background:#f8f9fa;
            text-align:center;
            padding:15px;
            color:#777;
            font-size:13px;">

            © 2026 BBB Plus. All Rights Reserved.

            </div>

            </div>

            </body>

            </html>
            """.formatted(partnerName, otp);

            sendHtmlMail(email, subject, body);

        } catch (Exception e) {

            throw new RuntimeException(e.getMessage());

        }

    }

    
    public boolean verifyOtp(String email, String otp) {

        String savedOtp = otpStore.get(email);

        if (savedOtp == null) {
            return false;
        }

        return savedOtp.equals(otp);
    }

}