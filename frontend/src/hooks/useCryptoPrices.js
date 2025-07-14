import { useState, useEffect } from 'react';

export function useCryptoPrices(pairs) {
  const [prices, setPrices] = useState({});

  useEffect(() => {
    const ws = new WebSocket('wss://ws.kraken.com/v2');

    ws.onopen = () => {
      ws.send(JSON.stringify({
        method: 'subscribe',
        params: {
          channel: "ticker",
          symbol: pairs
        }
      }));
    };

    ws.onmessage = ({ data }) => {
      const msg = JSON.parse(data);
      if (msg?.channel === 'ticker' && msg.type === 'update') {
        setPrices(prevPrices => {
          const ticker = msg.data[0];
          return {
            ...prevPrices,
            [ticker.symbol]: ticker.last,
          };
        });
      }
    };

    ws.onerror = console.error;
    ws.onclose = (event) => {
      console.warn('WS closed', event.code, event.reason);
      console.warn(event.code);
      console.warn(event.reason);
    };

    return () => ws.close();
  }, [pairs]);

  return prices;
}
