import { useState, useEffect } from 'react';
import { getHoldings } from '../api/holdings';
import { GLOBAL_USER_ID } from '../config';

export function useHoldings() {
  const [holdings, setHoldings] = useState([]);
  const [error, setError] = useState(null);

  const clearHoldingsError = () => setError(null);


  const reloadHoldings = async () => {
    try {
      const data = await getHoldings(GLOBAL_USER_ID);
      setHoldings(data);
      setError(null);
    } catch (err) {
      console.error('Failed to fetch holdings:', err);
      setError(err.message || 'Failed to fetch holdings');
    }
  };

  useEffect(() => { reloadHoldings(); }, []);

  return { holdings, reloadHoldings, holdingsError: error, clearHoldingsError };
}
