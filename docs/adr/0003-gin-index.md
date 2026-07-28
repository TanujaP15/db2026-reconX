# ADR-0003 — Use GIN `jsonb_path_ops` index for JSONB containment

- **Status:** Accepted
- **Date:** 2026-07-28
- **Deciders:** ReconX team

## Context

Containment queries against `instruments.metadata` (e.g., `metadata @>
'{"sector":"Technology"}'`) must be performant at scale. A regular
B-tree index is ineffective for JSONB containment operations.

## Decision

Create a GIN index on `instruments.metadata` using the `jsonb_path_ops`
operator class: `CREATE INDEX idx_instruments_metadata_gin ON
instruments USING GIN (metadata jsonb_path_ops);` This targets the
containment operator (`@>`) with a compact index structure.

## Consequences

Positive:
- Containment queries use the GIN index, reducing sequential scans for
  attribute lookups.
- `jsonb_path_ops` is smaller and faster for `@>` than the default class.

Negative:
- GIN indexes are slower to update than B-tree for heavy write workloads;
  monitor index maintenance costs.
- Some JSON lookup patterns (existence of keys, full-text search) may need
  different operator classes or additional indexes.

## Prompt

```
System: ReconX, PostgreSQL 16
Decision to record: Use GIN + jsonb_path_ops on instruments.metadata
Alternatives: (1) No index (seq scan); (2) GIN jsonb_ops; (3) Functional
indexes on extracted keys
Constraints: fast containment queries for dashboards, balanced write
costs
Format: Michael Nygard ADR, <300 words, Status + Date line
```
