package axal25.oles.jacek.bean.lifecycle;

import axal25.oles.jacek.util.CollectionUtils;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;

@SpringBootTest(classes = {
        // BeanLifeCycleTest.TestBean.class,
        BeanLifeCycleTest.TestBeanProvider.class,
        BeanLifeCycleTest.TestBeanPostProcessor.class,
        BeanLifeCycleTest.TestDependencyBean1.class,
        BeanLifeCycleTest.TestDependencyBean2.class})
public class BeanLifeCycleTest {
    private static final List<String> logContainer = Collections.synchronizedList(new ArrayList<>());
    @Autowired
    private TestBean testBean;
    @Autowired
    private TestDependencyBean1 testDependencyBean1;
    @Autowired
    private TestDependencyBean2 testDependencyBean2;
    @Autowired
    private BeanFactory beanFactory;
    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void assertBeanLifecycle() {
        String expectedDefaultBeanName = "beanLifeCycleTest.TestBean";
        assertThat(testBean.getTestDependencyBean1()).isNotNull();
        assertThat(testBean.getTestDependencyBean1()).isEqualTo(testDependencyBean1);
        assertThat(testBean.getTestDependencyBean2()).isNotNull();
        assertThat(testBean.getTestDependencyBean2()).isEqualTo(testDependencyBean2);
        assertThat(testBean.getBeanName()).isNotNull();
        assertThat(testBean.getBeanName()).isEqualTo(expectedDefaultBeanName);
        assertThat(testBean.getBeanFactory()).isNotNull();
        assertThat(testBean.getBeanFactory()).isEqualTo(beanFactory);
        assertThat(testBean.getApplicationContext()).isNotNull();
        assertThat(testBean.getApplicationContext()).isEqualTo(applicationContext);
        assertThat(applicationContext.getBean(expectedDefaultBeanName, TestBean.class)).isEqualTo(testBean);
        assertThat(beanFactory.getBean(expectedDefaultBeanName, TestBean.class)).isEqualTo(testBean);

        ((ConfigurableBeanFactory) beanFactory).destroyBean(expectedDefaultBeanName, testBean);

        List<String> expectedLogs = List.of(
                "new " + TestDependencyBean1.class.getSimpleName() + " () - constructor call",
                // TestDependencyBean2 initialization with @Bean provider:
                "new " + TestDependencyBean2.class.getSimpleName() + " () - constructor call",
                "@Autowired TestBean(" +
                        TestDependencyBean1.class.getSimpleName() +
                        " testDependencyBean1) - constructor call",

                // TestDependencyBean2 initialization with @Component
                // "new " + TestDependencyBean2.class.getSimpleName() + " () - constructor call",
                "@Autowired set" +
                        TestDependencyBean2.class.getSimpleName() +
                        "(" +
                        TestDependencyBean2.class.getSimpleName() +
                        " testDependencyBean2) {...}",

                BeanNameAware.class.getSimpleName() +
                        " # setBeanName - " +
                        TestBean.class.getSimpleName(),
                BeanFactoryAware.class.getSimpleName() +
                        " # setBeanFactory - " +
                        TestBean.class.getSimpleName(),
                ApplicationContextAware.class.getSimpleName() +
                        " # setApplicationContext - " +
                        TestBean.class.getSimpleName(),

                BeanPostProcessor.class.getSimpleName() +
                        " # postProcessBeforeInitialization - " +
                        TestBeanPostProcessor.class.getSimpleName(),

                "@" + PostConstruct.class.getSimpleName() +
                        " postConstruct() - " +
                        TestBean.class.getSimpleName(),

                InitializingBean.class.getSimpleName() +
                        " # afterPropertiesSet - " +
                        TestBean.class.getSimpleName(),

                "@Bean(initMethod=\"initMethod\") - " +
                        TestBean.class.getSimpleName() + " # initMethod()",

                BeanPostProcessor.class.getSimpleName() +
                        " # postProcessAfterInitialization - " +
                        TestBeanPostProcessor.class.getSimpleName(),

                ApplicationListener.class.getSimpleName() +
                        "<" +
                        ApplicationReadyEvent.class.getSimpleName() +
                        "> # onApplicationEvent(" +
                        ApplicationReadyEvent.class.getSimpleName() +
                        " event) - " +
                        TestBean.class.getSimpleName() +
                        " - TestBean.class.getSimpleName()",

                "@" + PreDestroy.class.getSimpleName() +
                        " preDestroy() - " +
                        TestBean.class.getSimpleName(),

                DisposableBean.class.getSimpleName() +
                        " # destroy - " +
                        TestBean.class.getSimpleName(),

                "@Bean(destroyMethod=\"destroyMethod\") - " +
                        TestBean.class.getSimpleName() + " # destroyMethod()"
        );
        assertThat(CollectionUtils.lengthyStringsToString(logContainer))
                .isEqualTo(CollectionUtils.lengthyStringsToString(expectedLogs));
        assertThat(logContainer).containsExactlyElementsIn(expectedLogs);
    }


    public static class TestBeanProvider {
        private TestBean testBean;

        @Bean(name = "beanLifeCycleTest.TestBean", initMethod = "initMethod", destroyMethod = "destroyMethod")
        public TestBean provideTestBean(@Autowired TestDependencyBean1 testDependencyBean1) {
            return new TestBean(testDependencyBean1);
        }
    }

    // @Component
    @Getter
    public static class TestBean implements
            BeanNameAware,
            BeanFactoryAware,
            ApplicationContextAware,
            InitializingBean,
            DisposableBean,
            ApplicationListener<ApplicationReadyEvent> {
        private final TestDependencyBean1 testDependencyBean1;
        private TestDependencyBean2 testDependencyBean2;
        private String beanName;
        private BeanFactory beanFactory;
        private ApplicationContext applicationContext;

        @Autowired
        TestBean(TestDependencyBean1 testDependencyBean1) {
            logContainer.add("@Autowired TestBean(" +
                    TestDependencyBean1.class.getSimpleName() +
                    " testDependencyBean1) - constructor call");
            this.testDependencyBean1 = testDependencyBean1;
        }

        @Autowired
        public void setTestDependencyBean2(TestDependencyBean2 testDependencyBean2) {
            logContainer.add("@Autowired set" +
                    TestDependencyBean2.class.getSimpleName() +
                    "(" +
                    TestDependencyBean2.class.getSimpleName() +
                    " testDependencyBean2) {...}");
            this.testDependencyBean2 = testDependencyBean2;
        }

        @Override
        public void setBeanName(String beanName) {
            logContainer.add(BeanNameAware.class.getSimpleName() +
                    " # setBeanName - " +
                    TestBean.class.getSimpleName());
            this.beanName = beanName;
        }

        @Override
        public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
            logContainer.add(BeanFactoryAware.class.getSimpleName() +
                    " # setBeanFactory - " +
                    TestBean.class.getSimpleName());
            this.beanFactory = beanFactory;
        }

        @Override
        public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
            logContainer.add(ApplicationContextAware.class.getSimpleName() +
                    " # setApplicationContext - " +
                    TestBean.class.getSimpleName());
            this.applicationContext = applicationContext;
        }

        @PostConstruct
        public void postConstruct() {
            logContainer.add("@" + PostConstruct.class.getSimpleName() +
                    " postConstruct() - " +
                    TestBean.class.getSimpleName());
        }

        @Override
        public void afterPropertiesSet() throws Exception {
            logContainer.add(InitializingBean.class.getSimpleName() +
                    " # afterPropertiesSet - " +
                    TestBean.class.getSimpleName());
        }

        public void initMethod() {
            logContainer.add("@Bean(initMethod=\"initMethod\") - " +
                    TestBean.class.getSimpleName() + " # initMethod()");
        }

        @Override
        public void onApplicationEvent(ApplicationReadyEvent event) {
            logContainer.add(ApplicationListener.class.getSimpleName() +
                    "<" +
                    ApplicationReadyEvent.class.getSimpleName() +
                    "> # onApplicationEvent(" +
                    ApplicationReadyEvent.class.getSimpleName() +
                    " event) - " +
                    TestBean.class.getSimpleName() +
                    " - TestBean.class.getSimpleName()");
        }

        @PreDestroy
        public void preDestroy() {
            logContainer.add("@" + PreDestroy.class.getSimpleName() +
                    " preDestroy() - " +
                    TestBean.class.getSimpleName());
        }

        @Override
        public void destroy() throws Exception {
            logContainer.add(DisposableBean.class.getSimpleName() +
                    " # destroy - " +
                    TestBean.class.getSimpleName());
        }

        public void destroyMethod() {
            logContainer.add("@Bean(destroyMethod=\"destroyMethod\") - " +
                    TestBean.class.getSimpleName() + " # destroyMethod()");
        }
    }

    @Component
    public static class TestDependencyBean1 {
        public TestDependencyBean1() {
            logContainer.add("new " + TestDependencyBean1.class.getSimpleName() + " () - constructor call");
        }
    }

    @Component
    public static class TestDependencyBean2 {
        public TestDependencyBean2() {
            logContainer.add("new " + TestDependencyBean2.class.getSimpleName() + " () - constructor call");
        }
    }

    @Component
    public static class TestBeanPostProcessor implements BeanPostProcessor {

        @Override
        public Object postProcessBeforeInitialization(@NonNull Object bean, @NonNull String beanName)
                throws BeansException {
            if (bean instanceof TestBean) {
                logContainer.add(BeanPostProcessor.class.getSimpleName() +
                        " # postProcessBeforeInitialization - " +
                        TestBeanPostProcessor.class.getSimpleName());
            }
            return bean;
        }

        @Override
        public Object postProcessAfterInitialization(@NonNull Object bean, @NonNull String beanName)
                throws BeansException {
            if (bean instanceof TestBean) {
                logContainer.add(BeanPostProcessor.class.getSimpleName() +
                        " # postProcessAfterInitialization - " +
                        TestBeanPostProcessor.class.getSimpleName());
            }
            return bean;
        }
    }
}
