import React, {
  createContext,
  useContext,
  useMemo,
  useState,
} from "react";

const DataTableContext = createContext();

export default function DataTable({ children, rows = [] }) {
  const [sortKey, setSortKey] = useState(null);
  const [sortDir, setSortDir] = useState("asc");

  const sortedRows = useMemo(() => {
    if (!sortKey) return rows;

    return [...rows].sort((a, b) => {
      if (a[sortKey] < b[sortKey]) {
        return sortDir === "asc" ? -1 : 1;
      }

      if (a[sortKey] > b[sortKey]) {
        return sortDir === "asc" ? 1 : -1;
      }

      return 0;
    });
  }, [rows, sortKey, sortDir]);

  return (
    <DataTableContext.Provider
      value={{
        rows: sortedRows,
        sortKey,
        sortDir,
        setSortKey,
        setSortDir,
      }}
    >
      <div className="data-table">
        {children}
      </div>
    </DataTableContext.Provider>
  );
}

DataTable.Header = function Header({ columns }) {
  const {
    sortKey,
    sortDir,
    setSortKey,
    setSortDir,
  } = useContext(DataTableContext);

  return (
    <div className="data-table__header" role="row">
      {columns.map((c) => (
        <button
          key={c.key}
          className={`data-table__th data-table__th--${
            sortKey === c.key ? "active" : "idle"
          }`}
          onClick={() => {
            if (sortKey === c.key) {
              setSortDir(sortDir === "asc" ? "desc" : "asc");
            } else {
              setSortKey(c.key);
              setSortDir("asc");
            }
          }}
        >
          {c.label}
        </button>
      ))}
    </div>
  );
};

DataTable.Body = function Body({ render }) {
  const { rows } = useContext(DataTableContext);

  return (
    <div className="data-table__body">
      {rows.map((row, i) => (
        <div key={row.id ?? i} className="data-table__row" role="row">
          {render(row)}
        </div>
      ))}
    </div>
  );
};

DataTable.Pagination = function Pagination({
  page,
  totalPages,
  onChange,
}) {
  return (
    <nav className="data-table__pagination" aria-label="Pagination">
      <button
        disabled={page === 0}
        onClick={() => onChange(page - 1)}
      >
        ‹
      </button>

      <span>
        {page + 1} / {totalPages}
      </span>

      <button
        disabled={page >= totalPages - 1}
        onClick={() => onChange(page + 1)}
      >
        ›
      </button>
    </nav>
  );
};