package github.srcmaxim.filesharingsystem.system.log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
public class LoggableAspect {

    final Logger logger = LoggerFactory.getLogger("github.srcmaxim.filesharingsystem");

    @Around("@within(github.srcmaxim.filesharingsystem.system.log.Loggable)")
    public Object log(ProceedingJoinPoint point) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = point.proceed();
        long stop = System.currentTimeMillis();
        if (logger.isInfoEnabled()) {
            String time = getTime(stop - start);
            writeToLog(point, time, result);
        }
        return result;
    }

    private static String getTime(long currentMillis) {
        long minute = TimeUnit.MILLISECONDS.toMinutes(currentMillis);
        long second = TimeUnit.MILLISECONDS.toSeconds(currentMillis);
        long millis = TimeUnit.MILLISECONDS.toMillis(currentMillis);
        return String.format("%02d:%02d:%d", minute, second, millis);
    }

    private void writeToLog(ProceedingJoinPoint point, String time, Object result) {
        logger.info(
                "{}#{}({}): {} in {}",
                point.getTarget().getClass().getSimpleName(),
                MethodSignature.class.cast(point.getSignature()).getMethod().getName(),
                Arrays.toString(point.getArgs()),
                result,
                time
        );
    }

}
