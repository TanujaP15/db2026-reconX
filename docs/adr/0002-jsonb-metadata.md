# ADR-0002 — Use `JSONB` for `instruments.metadata`

- **Status:** Accepted
- **Date:** 2026-07-28
- **Deciders:** ReconX team

## Context

Instrument attributes (sector, issuer metadata, tags, ratings) vary by
asset class and evolve frequently. Adding dedicated columns for every
optional attribute would bloat the schema and require frequent migrations.

## Decision

Add a `metadata JSONB NOT NULL DEFAULT '{}'::jsonb` column to
`instruments` to hold schema-flexible attributes. Access patterns use
containment queries (e.g., `metadata @> '{"sector":"Banks"}'`) and
path extraction for indexed lookups.

## Consequences

Positive:
- Avoids frequent ALTER TABLE cycles; new attributes can be added in app
  code without migrations.
- JSONB is indexable; common containment queries can be accelerated with
  a GIN index.

Negative:
- JSONB moves some validation from the DB to the application layer.
- Overuse may hide data that should be strongly typed; use for truly
  optional, schema-light fields only.

## Prompt

```
System: ReconX, PostgreSQL 16
Decision to record: Add `metadata JSONB` to `instruments`
Alternatives: (1) Add explicit columns per attribute; (2) Use key-value
table; (3) Use JSON text column
Constraints: support containment queries, minimize migrations, keep
dashboard queries fast
Format: Michael Nygard ADR, <300 words, Status + Date line
```
