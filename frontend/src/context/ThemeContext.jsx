import React, {
  createContext,
  useContext,
  useEffect,
  useState,
  useCallback,
} from 'react';

const ThemeContext = createContext(null);

const STORAGE_KEY = 'reconx-theme';

function initialTheme() {
  // For SSR / tests
  if (typeof window === 'undefined') {
    return 'light';
  }

  // Check if user has already selected a theme
  const savedTheme = localStorage.getItem(STORAGE_KEY);

  if (savedTheme) {
    return savedTheme;
  }

  // matchMedia is not available in some test environments (Vitest/jsdom)
  if (typeof window.matchMedia !== 'function') {
    return 'light';
  }

  // Respect system preference on first visit
  const prefersDark = window.matchMedia(
    '(prefers-color-scheme: dark)'
  ).matches;

  return prefersDark ? 'dark' : 'light';
}

export function ThemeProvider({ children }) {
  const [theme, setTheme] = useState(initialTheme);

  useEffect(() => {
    document.documentElement.dataset.theme = theme;
    localStorage.setItem(STORAGE_KEY, theme);
  }, [theme]);

  const toggle = useCallback(() => {
    setTheme((current) =>
      current === 'light' ? 'dark' : 'light'
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
    throw new Error(
      'useTheme must be used inside ThemeProvider'
    );
  }

  return context;
}