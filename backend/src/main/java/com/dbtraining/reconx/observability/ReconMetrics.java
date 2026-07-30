package com.dbtraining.reconx.observability;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

@Component
public class ReconMetrics {

    private final Timer reconciliationTimer;

    public ReconMetrics(MeterRegistry meterRegistry) {
        this.reconciliationTimer = Timer.builder("reconciliation_duration_seconds")
                .description("Time taken by reconciliation engine")
                .publishPercentileHistogram(true)
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);
    }

    public Timer reconciliationTimer() {
        return reconciliationTimer;
    }
}
