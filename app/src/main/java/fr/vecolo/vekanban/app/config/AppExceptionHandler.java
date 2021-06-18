package fr.vecolo.vekanban.app.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Arrays;
import java.util.stream.Collectors;

@ControllerAdvice
public class AppExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(AppExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public void GlobalExceptionHandler(Exception ex) {
        logger.error(ex.getMessage());
        String stackTrace = Arrays.stream(ex.getStackTrace()).limit(5).map(StackTraceElement::toString).collect(Collectors.joining("\n"));
        logger.error(stackTrace);
    }
}
