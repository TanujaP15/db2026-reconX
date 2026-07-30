package com.dbtraining.reconx.repository.entity;

import com.dbtraining.reconx.dto.ReconResult;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "recon_results")
public class ReconResultEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "trade_ref", nullable = false, length = 60)
    private String tradeRef;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ReconResult.Status status;

    @Column(name = "discrepancy_type", length = 100)
    private String discrepancyType;

    @Column(name = "details", length = 1000)
    private String details;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    public ReconResultEntity() {}

    public ReconResultEntity(String tradeRef, ReconResult.Status status, String discrepancyType, String details) {
        this.tradeRef = tradeRef;
        this.status = status;
        this.discrepancyType = discrepancyType;
        this.details = details;
    }

    @PrePersist
    void onPersist() {
        this.createdAt = Instant.now();
    }

    public Long getId() { return id; }
    public String getTradeRef() { return tradeRef; }
    public ReconResult.Status getStatus() { return status; }
    public String getDiscrepancyType() { return discrepancyType; }
    public String getDetails() { return details; }
    public Instant getCreatedAt() { return createdAt; }

    public void setTradeRef(String tradeRef) { this.tradeRef = tradeRef; }
    public void setStatus(ReconResult.Status status) { this.status = status; }
    public void setDiscrepancyType(String discrepancyType) { this.discrepancyType = discrepancyType; }
    public void setDetails(String details) { this.details = details; }
}
