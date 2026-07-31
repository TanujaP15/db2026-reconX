import { useEffect, useRef, useState } from 'react';

export function useWebSocket(url, { reconnect = true, maxRetries = 5 } = {}) {
  // TODO(TICKET-ADV115): open a WebSocket in a useEffect.
  //   - track readyState in `status` ('connecting' | 'open' | 'closed' | 'error').
  //   - parse incoming messages as JSON (fall back to raw string).
  //   - on close, if `reconnect` and retries < maxRetries, schedule another
  //     connect() with exponential backoff (500 * 2^attempt, capped at 30s).
  //   - cleanup must close the socket AND cancel any pending reconnect.
  const [data, setData] = useState(null);
  const [status, setStatus] = useState('connecting');
  const socketRef = useRef(null);
  const retryRef = useRef(0);
  const reconnectTimerRef = useRef(null);

  useEffect(() => {
    function connect() {
      setStatus('connecting');

      const socket = new WebSocket(url);
      socketRef.current = socket;

      socket.onopen = () => {
        retryRef.current = 0;
        setStatus('open');
      };

      socket.onmessage = (event) => {
        try {
          setData(JSON.parse(event.data));
        } catch {
          setData(event.data);
        }
      };

      socket.onerror = () => {
        setStatus('error');
      };

      socket.onclose = () => {
        setStatus('closed');

        if (
          reconnect &&
          retryRef.current < maxRetries
        ) {
          const delay = Math.min(
            30000,
            500 * 2 ** retryRef.current
          );

          retryRef.current += 1;

          reconnectTimerRef.current = setTimeout(() => {
            connect();
          }, delay);
        }
      };
    }

    connect();

    return () => {
      if (reconnectTimerRef.current) {
        clearTimeout(reconnectTimerRef.current);
      }

      if (socketRef.current) {
        socketRef.current.close();
      }
    };
  }, [url, reconnect, maxRetries]);

  const send = (message) => {
    if (
      socketRef.current &&
      socketRef.current.readyState === WebSocket.OPEN
    ) {
      socketRef.current.send(
        typeof message === 'string'
          ? message
          : JSON.stringify(message)
      );
    }
  };

  return { data, status, send };
}
