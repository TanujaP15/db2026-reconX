// TICKET-ADV106 / ADV107 — EventSource live feed with prepend + slide-in animation.
(function () {

    const feed = document.getElementById("trade-feed");
    if (!feed) return;

    const statusBadge = document.getElementById("sse-status");

    function updateConnectionBadge(text, cssClass) {
        if (!statusBadge) return;

        statusBadge.textContent = text;
        statusBadge.className = "status-badge " + cssClass;
    }

    const STREAM_URL = "/api/v1/trades/stream";
    let sse = null;

    function connect() {

        sse = new EventSource(STREAM_URL);

        sse.onopen = () => {
            updateConnectionBadge("Live", "live");
        };

        sse.onmessage = (event) => {

            try {

                const trade = JSON.parse(event.data);

                prependTradeRow(trade);

            } catch (e) {

                console.error(e);

            }

        };

        sse.onerror = () => {
            updateConnectionBadge("Reconnecting...", "reconnecting");
        };

    }

    // Hardcoded demo events for the static dashboard (no backend required).
    const demoEvents = [
        {
            tradeRef: "EQU-20260603-0001",
            symbol: "SAP.DE",
            qty: 1000,
            price: 125.50,
            status: "MATCHED"
        },
        {
            tradeRef: "FX-20260603-0001",
            symbol: "EUR/USD",
            qty: 1000000,
            price: 1.0852,
            status: "PENDING"
        },
        {
            tradeRef: "EQU-20260603-0002",
            symbol: "AAPL",
            qty: 500,
            price: 178.20,
            status: "BREAK"
        }
    ];

    function escapeHtml(str) {
        return String(str)
            .replace(/&/g, "&amp;")
            .replace(/</g, "&lt;")
            .replace(/>/g, "&gt;")
            .replace(/"/g, "&quot;")
            .replace(/'/g, "&#39;");
    }

    function formatQty(qty) {
        return new Intl.NumberFormat("en-US").format(qty);
    }

    function formatPrice(price) {
        return new Intl.NumberFormat("en-US", {
            minimumFractionDigits: 2,
            maximumFractionDigits: 4
        }).format(price);
    }

    function prependTradeRow(trade) {

        let statusModifier = "";

        if (trade.status === "MATCHED") {
            statusModifier = "trade-card--matched";
        } else if (
            trade.status === "BREAK" ||
            trade.status === "UNMATCHED"
        ) {
            statusModifier = "trade-card--break";
        }

        const row = document.createElement("article");

        row.className =
            "trade-card " +
            statusModifier +
            " trade-card--new";

        row.innerHTML = `
            <header class="trade-card__header">
                <strong>${escapeHtml(trade.tradeRef)}</strong>
                <span>${escapeHtml(trade.status)}</span>
            </header>

            <div class="trade-card__body">
                <div><strong>Symbol:</strong> ${escapeHtml(trade.symbol)}</div>
                <div><strong>Qty:</strong> ${formatQty(trade.qty)}</div>
                <div><strong>Price:</strong> ${formatPrice(trade.price)}</div>
            </div>
        `;

        feed.prepend(row);

        setTimeout(() => {
            row.classList.remove("trade-card--new");
        }, 500);

        while (feed.children.length > 50) {
            feed.lastElementChild.remove();
        }

    }

    // Demo cards (works even without backend)
    demoEvents.forEach((e, i) => {
        setTimeout(() => {
            prependTradeRow(e);
        }, 500 * i);
    });

    // Connect to backend SSE
    connect();

    // Close connection when leaving page
    window.addEventListener("beforeunload", () => {
        if (sse) {
            sse.close();
        }
    });

})();