package com.example.health;

import com.codahale.metrics.health.HealthCheck;
import org.springframework.stereotype.Component;

@Component
public class StatusHealthCheck extends HealthCheck {
    @Override
    protected Result check() throws Exception {
        return Result.healthy();
    }
}
