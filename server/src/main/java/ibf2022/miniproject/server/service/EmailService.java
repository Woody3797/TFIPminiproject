package ibf2022.miniproject.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private SpringTemplateEngine springTemplateEngine;

    public void sendEmailFromTemplate(String to, String content) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        Context context = new Context();
        // insert data into fields in template
        context.setVariable("token", content);
        String html = springTemplateEngine.process("email", context);
        helper.setTo(to);
        helper.setSubject("TFIP Project Reset password");
        helper.setText(html, true);
        ClassPathResource image = new ClassPathResource("ogreyep.png");
        helper.addInline("ogreyep", image);

        emailSender.send(message);
    }

}
