package axal25.oles.jacek.config;

import axal25.oles.jacek.BugTrackerApp;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(
        basePackageClasses = BugTrackerApp.class,
        repositoryImplementationPostfix = "Impl",
        entityManagerFactoryRef = "entityManagerFactory",
        transactionManagerRef = "transactionManager"
)
public class JpaConfig {

//    @Primary
//    @Bean(name = "entityManagerFactory")
//    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
//            EntityManagerFactoryBuilder emfBuilder,
//            DataSource dataSource,
//            JpaProperties jpaProperties) {
//        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
//        return emfBuilder
//                .dataSource(dataSource)
//                .packages("axal25")
//                .persistenceUnit("")
//                .build();
//    }

//    @Primary
//    @Bean
//    public PlatformTransactionManager transactionManager(ObjectProvider<TransactionManagerCustomizers> transactionManagerCustomizers) {
//        JpaTransactionManager transactionManager = new JpaTransactionManager();
//        transactionManagerCustomizers.ifAvailable((customizers) -> customizers.customize(transactionManager));
//        return transactionManager;
//    }
}
