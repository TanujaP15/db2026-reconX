package com.dbtraining.reconx.service;

import com.dbtraining.reconx.dto.ReconResult;
import com.dbtraining.reconx.repository.entity.ReconResultEntity;
import com.dbtraining.reconx.repository.entity.Trade;
import com.dbtraining.reconx.repository.ExternalTradeRepository;
import com.dbtraining.reconx.repository.InternalTradeRepository;
import com.dbtraining.reconx.repository.ReconResultRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
@Transactional
class ReconciliationIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("reconx")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void props(DynamicPropertyRegistry r) {
        r.add("spring.datasource.url", postgres::getJdbcUrl);
        r.add("spring.datasource.username", postgres::getUsername);
        r.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    InternalTradeRepository internalTradeRepo;

    @Autowired
    ExternalTradeRepository externalTradeRepo;

    @Autowired
    ReconResultRepository reconResultRepo;

    @Autowired
    ReconciliationService reconciliationService;

    @Test
    void containerIsRunning() {
        assertThat(postgres.isRunning()).isTrue();
    }

    @Test
    void insertedTradesAreReconciledAndPersisted() {
        // given
        Trade internal = new Trade(
                "TRD-INT-1",
                "CP-1",
                "SAP.DE",
                new BigDecimal("100"),
                new BigDecimal("245.50"),
                LocalDate.now());

        Trade external = new Trade(
                "TRD-INT-1",
                "CP-1",
                "SAP.DE",
                new BigDecimal("100"),
                new BigDecimal("245.50"),
                LocalDate.now());

        internalTradeRepo.save(internal);
        externalTradeRepo.save(external);

        // when
        reconciliationService.runRecon(
                internalTradeRepo.findAll(),
                externalTradeRepo.findAll());

        // then
        List<ReconResultEntity> persisted = reconResultRepo.findAll();

        assertThat(persisted).hasSize(1);
        assertThat(persisted.get(0).getStatus())
                .isEqualTo(ReconResult.Status.MATCHED);
        assertThat(persisted.get(0).getTradeRef())
                .isEqualTo("TRD-INT-1");
    }
}
