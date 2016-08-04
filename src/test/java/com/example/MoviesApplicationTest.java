package com.example;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MoviesApplicationTest {
    @Test
    public void beanNameToHealthCheckName() throws Exception {
        // given
        String beanName = "Status";

        // when
        String healthCheckName = MoviesApplication.beanNameToHealthCheckName(beanName);

        // then
        assertThat(healthCheckName).isEqualTo(beanName);
    }

    @Test
    public void healthCheckDerivedBeanNameToHealthCheckName() throws Exception {
        // given
        String beanName = "StatusHealthCheck";

        // when
        String healthCheckName = MoviesApplication.beanNameToHealthCheckName(beanName);

        // then
        assertThat(healthCheckName).isEqualTo("Status");
    }
}