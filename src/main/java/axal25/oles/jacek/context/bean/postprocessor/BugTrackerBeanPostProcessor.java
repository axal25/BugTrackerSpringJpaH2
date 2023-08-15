package axal25.oles.jacek.context.bean.postprocessor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class BugTrackerBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(@NonNull Object bean, @NonNull String beanName)
            throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(@NonNull Object bean, @NonNull String beanName)
            throws BeansException {
        return bean;
    }
}
