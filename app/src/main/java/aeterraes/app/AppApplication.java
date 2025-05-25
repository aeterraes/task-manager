package aeterraes.app;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class AppApplication {
    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();
        System.setProperty("MAIL", dotenv.get("MAIL"));
        System.setProperty("PASS", dotenv.get("PASS"));
        SpringApplication.run(AppApplication.class, args);
    }
}
