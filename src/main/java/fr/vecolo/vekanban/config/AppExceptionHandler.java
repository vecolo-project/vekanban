package fr.vecolo.vekanban.config;

import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.stream.Collectors;

@Configuration
@Aspect()
public class AppExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(AppExceptionHandler.class);

    @AfterThrowing(pointcut = "execution(* fr.vecolo.vekanban.*.*.*(..))",throwing = "ex")
    public void GlobalExceptionHandler(Exception ex) {
        Thread.setDefaultUncaughtExceptionHandler((t,e)->{
            logger.error(e.getMessage());
            String stackTrace = Arrays.stream(e.getStackTrace()).limit(5).map(StackTraceElement::toString).collect(Collectors.joining("\n"));
            logger.error(stackTrace);
        });
    }
}
