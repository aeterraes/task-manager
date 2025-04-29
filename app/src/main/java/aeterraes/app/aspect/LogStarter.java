package aeterraes.app.aspect;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;

@AutoConfiguration
@Import(LogAspectConfiguration.class)
public class LogStarter {
}
