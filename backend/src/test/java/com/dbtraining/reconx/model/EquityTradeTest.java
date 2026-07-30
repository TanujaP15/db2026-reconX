package com.dbtraining.reconx.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EquityTradeTest {

    @Test
    void builder_buildsWhenAllRequiredPresent() {
        EquityTrade t = EquityTrade.builder()
                .tradeRef(TradeRef.of("EQU-20260603-0001"))
                .instrumentSymbol("SAP.DE")
                .quantity(new BigDecimal("100"))
                .price(new BigDecimal("2.50"))
                .currency("EUR").side(Side.BUY)
                .tradeDate(LocalDate.of(2026,6,3))
                .counterpartyId(1L)
                .build();

        assertThat(t.tradeRef().value()).isEqualTo("EQU-20260603-0001");
        assertThat(t.assetClass()).isEqualTo(TradeType.AssetClass.EQUITY);
        assertThat(t.notional().amount()).isEqualByComparingTo(new BigDecimal("250.00"));
    }

    @Test
    void builder_missingPrice_throws() {
        EquityTrade.Builder b = EquityTrade.builder()
            .tradeRef(TradeRef.of("EQU-20260603-0002"))
            .instrumentSymbol("SAP.DE")
            .quantity(new BigDecimal("10"))
            .currency("EUR").side(Side.SELL)
            .tradeDate(LocalDate.of(2026,6,3))
            .counterpartyId(1L);

        assertThatThrownBy(b::build)
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("price");
    }

    @Test
    void equality_byTradeRef() {
        EquityTrade a = sampleEquity("EQU-20260603-0003");
        EquityTrade b = sampleEquity("EQU-20260603-0003");
        EquityTrade c = sampleEquity("EQU-20260603-0004");

        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
        assertThat(a).isNotEqualTo(c);
    }

    private EquityTrade sampleEquity(String ref) {
        return EquityTrade.builder()
                .tradeRef(TradeRef.of(ref))
                .instrumentSymbol("SAP.DE")
                .quantity(new BigDecimal("100"))
                .price(new BigDecimal("100"))
                .currency("EUR").side(Side.BUY)
                .tradeDate(LocalDate.of(2026, 6, 3))
                .counterpartyId(1L).build();
    }
}
