package com.example;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.slf4j.Logger;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;

import static org.slf4j.LoggerFactory.getLogger;

public class MoviesApplication extends Application<MoviesConfiguration> {
    private static final Logger log = getLogger(MoviesApplication.class);


    public static void main(final String[] args) throws Exception {
        new MoviesApplication().run(args);
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
        springApplication.run();
    }
}
