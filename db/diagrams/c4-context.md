```mermaid
C4Context
    title C4 Context — ReconX Enterprise Trade Reconciliation Platform

    Person(traderUser, "Trader", "Books and amends trades; investigates breaks.")
    Person(reconAnalyst, "Recon Analyst", "Resolves daily reconciliation breaks.")
    Person(opsAdmin, "Ops Admin", "Manages users, audits activity.")
    Person(complianceUser, "Compliance Officer", "Reads audit logs and reports.")

    System(reconx, "ReconX", "Trade reconciliation platform")

    System_Ext(internalOMS, "Internal OMS", "Internal trade source")
    System_Ext(counterpartySFTP, "Counterparty Trade Files", "CSV files via SFTP")
    System_Ext(bloombergPricing, "Bloomberg Pricing", "Market reference data")
    System_Ext(emailGateway, "Corporate Email Gateway", "Notification emails")
    System_Ext(ssoIdP, "Corporate SSO", "OIDC Authentication")
    System_Ext(grafana, "Grafana", "Monitoring dashboards")

    Rel(traderUser, reconx, "Books trades and views breaks", "HTTPS")
    Rel(reconAnalyst, reconx, "Resolves reconciliation breaks", "HTTPS")
    Rel(opsAdmin, reconx, "User management and audit", "HTTPS")
    Rel(complianceUser, reconx, "Views reports", "HTTPS")

    Rel(internalOMS, reconx, "Streams trade events", "Kafka")
    Rel(counterpartySFTP, reconx, "Uploads CSV trade files", "SFTP")
    Rel(reconx, bloombergPricing, "Fetches prices", "REST")
    Rel(reconx, emailGateway, "Sends notifications", "SMTP")
    Rel(reconx, ssoIdP, "Authenticates users", "OIDC")
    Rel(grafana, reconx, "Reads metrics", "HTTPS")