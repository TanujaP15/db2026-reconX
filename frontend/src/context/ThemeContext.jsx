import React, {
  createContext,
  useContext,
  useEffect,
  useState,
  useCallback,
} from "react";

const ThemeContext = createContext(null);

const STORAGE_KEY = "reconx-theme";

function initialTheme() {
  if (typeof window === "undefined") {
    return "light";
  }

  const savedTheme = localStorage.getItem(STORAGE_KEY);

  if (savedTheme) {
    return savedTheme;
  }

  const prefersDark = window.matchMedia(
    "(prefers-color-scheme: dark)"
  ).matches;

  return prefersDark ? "dark" : "light";
}

export function ThemeProvider({ children }) {
  const [theme, setTheme] = useState(initialTheme);

  useEffect(() => {
    document.documentElement.dataset.theme = theme;
    localStorage.setItem(STORAGE_KEY, theme);
  }, [theme]);

  const toggle = useCallback(() => {
    setTheme((current) =>
      current === "light" ? "dark" : "light"
    );
  }, []);

  return (
    <ThemeContext.Provider
      value={{
        theme,
        setTheme,
        toggle,
      }}
    >
      {children}
    </ThemeContext.Provider>
  );
}

export function useTheme() {
  const context = useContext(ThemeContext);

  if (!context) {
    throw new Error("useTheme must be used inside ThemeProvider");
  }

  return context;
}