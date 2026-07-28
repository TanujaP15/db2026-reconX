# ADR-0001 — Partition the `trades` table by `trade_date`

- **Status:** Accepted
- **Date:** 2026-07-28
- **Deciders:** ReconX team

## Context

`trades` is the highest-volume table in ReconX (~50k inserts/day). Most
operational queries (reconciliation runs, dashboard lookups, analyst
investigations) filter by date ranges. Without partitioning, daily queries
scan large rowsets and archival requires heavy DELETE operations.

## Decision

Partition `trades` by RANGE on `trade_date` with one partition per calendar
month. The parent table uses `PRIMARY KEY (id, trade_date)` to satisfy
Postgres partitioning constraints. A `trades_default` partition will catch
out-of-range inserts. Monthly partitions are pre-created by routine ops.

## Consequences

Positive:
- Partition pruning reduces scanned data for date-bounded queries by ~11/12
  for a monthly filter.
- Archival becomes a DDL operation (`DETACH` / `DROP`), faster and safer.

Negative:
- Composite PK `(id, trade_date)` complicates some ORMs and uniqueness
  constraints across partitions.
- Requires operational automation to pre-create partitions and monitor
  unexpected default-partition inserts.

## Prompt

```
System: ReconX, PostgreSQL 16, ~50k trades/day
Decision to record: Partition `trades` by `trade_date` (monthly range)
Alternatives: (1) No partitioning; (2) Partition by surrogate id; (3)
Partition by counterparty
Constraints: dashboard queries are date-bounded; 5-year retention; avoid
long-running deletes
Format: Michael Nygard ADR, <300 words, Status + Date line
```
