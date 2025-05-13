package aeterraes.app.notification;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final JavaMailSender mailSender;
    Dotenv dotenv = Dotenv.load();
    private final String email = dotenv.get("MAIL");

    public void sendNotification(int taskId, String newStatus) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(email);
        message.setTo(email);
        message.setSubject("Changed status");
        message.setText(String.format("Status #%d changed to: %s", taskId, newStatus));
        try {
            mailSender.send(message);
            log.info("Info status {} for {}", email, taskId);
        } catch (Exception e) {
            log.error("Error{}", taskId, e);
        }
    }
}
