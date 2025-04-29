package aeterraes.app.aspect;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
public class LogAspectConfiguration {
    @Bean
    public LogAspect logAspect() {
        return new LogAspect();
    }
}
