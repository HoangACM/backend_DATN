package code.service.more;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {
  private final JavaMailSender mailSender;

  public MailService(JavaMailSender mailSender) {
    this.mailSender = mailSender;
  }

  public void sendEmail(String recipient, String subject, String body) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(recipient); // Địa chỉ email người nhận
    message.setSubject(subject); // Chủ đề email
    message.setText(body); // Nội dung email
    message.setFrom("doantotnghiepspring@gmail.com"); // Địa chỉ email người gửi

    mailSender.send(message); // Gửi email
  }
}
