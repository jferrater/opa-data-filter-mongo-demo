package com.github.jferrater.userservice.config;

import com.github.jferrater.opadatafiltermongospringbootstarter.repository.OpaMongoRepositoryFactoryBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * @author joffryferrater
 */
@Configuration
@EnableMongoRepositories(
        basePackages = "com.github.jferrater.userservice.repository",
        repositoryFactoryBeanClass = OpaMongoRepositoryFactoryBean.class
)
public class MongoDbConfig {

}
