package com.dbtraining.reconx.service;

import com.dbtraining.reconx.dto.ReconResult;
import com.dbtraining.reconx.model.EquityTrade;
// import com.dbtraining.reconx.model.Trade;
import com.dbtraining.reconx.model.TradeRef;
import com.dbtraining.reconx.model.Side;
import com.dbtraining.reconx.repository.entity.ReconResultEntity;
import com.dbtraining.reconx.repository.ReconResultRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class ReconciliationServiceTest {

    @Test
    void testReconcile_savesResultWithMatchedStatus() {
        // given
        ReconResultRepository repo = mock(ReconResultRepository.class);
        ReconciliationEngine engine = new ReconciliationEngine();
        ReconciliationService svc = new ReconciliationService(engine, repo);

        EquityTrade internal = EquityTrade.builder()
                .tradeRef(TradeRef.of("TRD-20260730-0001"))
                .instrumentSymbol("SAP.DE")
                .price(new BigDecimal("10"))
                .quantity(new BigDecimal("100"))
                .currency("EUR")
                .side(Side.BUY)
                .tradeDate(LocalDate.now())
                .counterpartyId(1L)
                .build();

        EquityTrade external = EquityTrade.builder()
                .tradeRef(TradeRef.of("TRD-20260730-0001"))
                .instrumentSymbol("SAP.DE")
                .price(new BigDecimal("10"))
                .quantity(new BigDecimal("100"))
                .currency("EUR")
                .side(Side.BUY)
                .tradeDate(LocalDate.now())
                .counterpartyId(1L)
                .build();

        // Trade i = new Trade("TRD-1", "CP-1", "SAP.DE",
        //         new BigDecimal("10"), new BigDecimal("100"), LocalDate.now());
        // Trade e = new Trade("TRD-1", "CP-1", "SAP.DE",
        //         new BigDecimal("10"), new BigDecimal("100"), LocalDate.now());

        // when
        svc.runReconWithDomainTrades(List.of(internal), List.of(external));

        // then
        ArgumentCaptor<ReconResultEntity> captor = ArgumentCaptor.forClass(ReconResultEntity.class);
        verify(repo).save(captor.capture());
        assertThat(captor.getValue().getTradeRef()).isEqualTo("TRD-20260730-0001");
        assertThat(captor.getValue().getStatus()).isEqualTo(ReconResult.Status.MATCHED);
    }
}