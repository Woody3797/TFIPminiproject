package ibf2022.miniproject.server.controller;

import ibf2022.miniproject.server.common.ApiResponse;
import ibf2022.miniproject.server.common.Utility;
import ibf2022.miniproject.server.emailSetting.EmailSettingBag;
import ibf2022.miniproject.server.emailSetting.SettingService;
import ibf2022.miniproject.server.exception.UserNotFoundException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ibf2022.miniproject.server.service.UserService;

@RestController
@RequestMapping(path = "/api")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private SettingService settingService;

    @GetMapping("/forget_password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email, HttpServletRequest request) {
        System.out.println(email);
        try {
            String token = userService.updateResetPasswordToken(email);
            String link =  Utility.getSiteURL(request)+"/api/reset_password?token=" + token;
            try {
                sendEmail(link, email);
            } catch (MessagingException | UnsupportedEncodingException e) {
                return new ResponseEntity<>("Failed to send request", HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(new ApiResponse(true, "Sent request successfully!"),
                HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>("User not found", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/reset_password")
    public ResponseEntity<?> resetPassword(@RequestParam String token,
        @RequestParam String newPassword) {
        boolean reseted = false;
        try {
            reseted = userService.resetPassword(token, newPassword);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
        if (reseted) {
            return new ResponseEntity<>(new ApiResponse(true, "changed password successfully!"),
                HttpStatus.OK);
        }
        return new ResponseEntity<>("Failed to change password!", HttpStatus.BAD_REQUEST);
    }

    private void sendEmail(String link, String email)
        throws MessagingException, UnsupportedEncodingException {
        EmailSettingBag emailSetting = settingService.getEmailSettings();
        JavaMailSenderImpl mailSender = Utility.prepareMailSender(emailSetting);

        String toAddress = email;
        String subject = "Here's the link to reset your password";

        String content = "<p>Hello, </p>"
            + "<p>You have requested to reset your password.</p>"
            + "<p>Click the link below to change your password:</p>"
            + "<p><a href=\"" + link + "\">Change your password</a></p>"
            + "<br>"
            + "<p>Ignore this email if you remember your password, "
            + "or you have not made the request.</p ";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(emailSetting.getFromAddress(), emailSetting.getSenderName());
        helper.setTo(toAddress);
        helper.setSubject(subject);
        helper.setText(content, true);
        mailSender.send(message);
    }

}
