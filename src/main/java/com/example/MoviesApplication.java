package com.example;

import com.codahale.metrics.health.HealthCheck;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

public class MoviesApplication extends Application<MoviesConfiguration> {
    private static final Logger log = getLogger(MoviesApplication.class);


    public static void main(final String[] args) throws Exception {
        new MoviesApplication().run(args);
    }

    static String beanNameToHealthCheckName(String name) {
        return StringUtils.removeEnd(name, "HealthCheck");
    }


    @Override
    public String getName() {
        return "Movies";
    }

    @Override
    public void initialize(final Bootstrap<MoviesConfiguration> bootstrap) {
    }

    @Override
    public void run(
            final MoviesConfiguration configuration,
            final Environment environment) {
        SpringApplication springApplication = new SpringApplication(SpringConfiguration.class);
        springApplication.setWebEnvironment(false);
        springApplication.setBannerMode(Banner.Mode.OFF);
        ConfigurableApplicationContext context = springApplication.run();
        registerHealthChecks(environment, context);
    }

    private void registerHealthChecks(Environment environment, ApplicationContext context) {
        Map<String, HealthCheck> healthCheckBeans = context.getBeansOfType(HealthCheck.class);
        for (Map.Entry<String, HealthCheck> bean : healthCheckBeans.entrySet()) {
            String name = beanNameToHealthCheckName(bean.getKey());
            HealthCheck instance = bean.getValue();
            String className = instance.getClass().getName();
            environment.healthChecks().register(name, instance);
            log.info("Registered health check bean '{}' of class {}", name, className);
        }
    }
}
