package aeterraes.app.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogAspect {

    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Before("@annotation(aeterraes.app.aspect.annotation.Loggable)")
    public void logBefore(JoinPoint joinPoint) {
        logger.info("{}.{}", joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName());
    }

    @AfterThrowing(
            pointcut = "@annotation(aeterraes.app.aspect.annotation.ExceptionHandling)",
            throwing = "exception"
    )
    public void logAfterThrowing(JoinPoint joinPoint,  Exception exception) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
        logger.error("Exception in {}.{}(): {} - {}", className, methodName,
                exception.getClass().getSimpleName(), exception.getMessage());
    }

    @AfterReturning(
            pointcut = "@annotation(aeterraes.app.aspect.annotation.ResultHandling)",
            returning = "result"
    )
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        logger.info("{} returned: {}", joinPoint.getSignature().getName(),
                result != null ? result.toString() : "null");
    }

    @Around("@annotation(aeterraes.app.aspect.annotation.LogTimeTracking)")
    public Object logMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        String methodName = joinPoint.getSignature().getName();
        Object result = joinPoint.proceed();
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        logger.info("Method {} executed in {} ms", methodName, duration);
        return result;
    }
}
