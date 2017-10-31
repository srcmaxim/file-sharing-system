package github.srcmaxim.filesharingsystem.postprocessor;

import github.srcmaxim.filesharingsystem.annotation.Loggable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Component
public class LoggableBeanPostProcessor implements BeanPostProcessor {

    public final Logger logger = LoggerFactory.getLogger(LoggableBeanPostProcessor.class.getName());

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields) {
            Loggable loggable = field.getAnnotation(Loggable.class);
            if (loggable != null) {
                setLoggerIntoField(bean, field);
                break;
            }
        }
        return bean;
    }

    private void setLoggerIntoField(Object bean, Field field) {
        field.setAccessible(true);
        String className = bean.getClass().getName();
        Logger beanLogger = LoggerFactory.getLogger(className);
        try {
            field.set(bean, beanLogger);
        } catch (IllegalAccessException e) {
            logger.error("Exception while instantiating logger in " + className, e);
        }
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

}
