ADR templates and guidance

Use this template to generate Architecture Decision Records (ADRs) via Claude
or another LLM. Save the prompt alongside the generated ADR so reviewers can
see how the decision was drafted.

Prompt template:

```
You are an enterprise software architect. Write an Architecture Decision Record
(ADR) in the Michael Nygard format (Title, Status, Context, Decision,
Consequences) for the following decision.

System: ReconX, a near-prod trade reconciliation platform.
Stack: PostgreSQL 16, Spring Boot 3, Kafka, React.
Scale: ~50,000 trades/day, 5-year retention, 10 concurrent recon analysts.

Decision to record: <ONE LINE DESCRIBING THE DECISION>

Alternatives we considered: <LIST 2-3>

Constraints / forces: <LIST 2-3>

Format: Markdown, Nygard 5-section template, no fluff. Keep under 300 words.
Include a "Status: Accepted | Date: <YYYY-MM-DD>" line.
```

Save generated ADRs as `docs/adr/0001-*.md`, `0002-*.md`, ... and include the
prompt used in a `Prompt:` section at the end of the file.
