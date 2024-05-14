package net.savantly.nexus.products.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.BeansException;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * Configuration for the ProductsModule
 */
@Slf4j
@Configuration("productsConfig")
@ConfigurationProperties(prefix = "nexus.modules.products")
@Data
public class ProductsConfig implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @PostConstruct
    public void init() {
        log.info("ProductsConfig initialized");

    }

}