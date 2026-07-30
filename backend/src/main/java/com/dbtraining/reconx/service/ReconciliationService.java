package com.dbtraining.reconx.service;

import com.dbtraining.reconx.dto.ReconResult;
import com.dbtraining.reconx.model.EquityTrade;
import com.dbtraining.reconx.model.ReconciliationRule;
import com.dbtraining.reconx.model.Side;
import com.dbtraining.reconx.model.TradeRef;
import com.dbtraining.reconx.model.TradeType;
import com.dbtraining.reconx.repository.ReconResultRepository;
import com.dbtraining.reconx.repository.entity.ReconResultEntity;
import com.dbtraining.reconx.repository.entity.Trade;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReconciliationService {

    private final ReconciliationEngine engine;
    private final ReconResultRepository repo;

    public ReconciliationService(ReconciliationEngine engine,
                                 ReconResultRepository repo) {
        this.engine = engine;
        this.repo = repo;
    }

    public void runRecon(List<Trade> internalTrades, List<Trade> externalTrades) {
        List<TradeType> internal = internalTrades == null ? List.of() : internalTrades.stream()
                .map(this::toDomain).toList();

        List<TradeType> external = externalTrades == null ? List.of() : externalTrades.stream()
                .map(this::toDomain).toList();

        List<ReconResult> results = engine.reconcile(internal, external, ReconciliationRule.EXACT);

        results.stream().map(this::toEntity).forEach(r -> repo.save(r));
    }
    public void runReconWithDomainTrades(List<TradeType> internal, List<TradeType> external) {
    List<ReconResult> results = engine.reconcile(internal, external, ReconciliationRule.EXACT);
    results.stream()
           .map(this::toEntity)
           .forEach(repo::save);
}

    private TradeType toDomain(Trade t) {
        // Example mapping for EQUITY trades only; adapt for other asset classes
        return EquityTrade.builder()
                .tradeRef(TradeRef.of(t.getTradeRef()))
                .instrumentSymbol(t.getInstrument().getSymbol())
                .price(t.getPrice())
                .quantity(t.getQuantity())
                .currency(t.getInstrument().getCurrency())
                .side(Side.valueOf(t.getSide()))
                .tradeDate(t.getTradeDate())
                .counterpartyId(t.getCounterparty().getId())
                .build();
    }
    private ReconResultEntity toEntity(ReconResult r) {
        return new ReconResultEntity(r.tradeRef(), r.status(), r.discrepancyType(), r.details());
    }
}
