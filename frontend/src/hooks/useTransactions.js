import { useState, useEffect } from 'react';
import { getAllTransactions } from '../api/transactions';
import { GLOBAL_USER_ID } from '../config';

export function useTransactions() {
  const [transactions, setTransactions] = useState([]);
  const [error, setError] = useState(null);

  const clearTransactionsError = () => setError(null);


  const loadTransactions = async () => {
    try {
      const data = await getAllTransactions(GLOBAL_USER_ID);
      setTransactions(data);
      setError(null);
    } catch (err) {
      console.error('Failed to fetch transactions:', err);
      setError(err.message || 'Failed to load transactions');
    }
  };

  useEffect(() => { loadTransactions(); }, []);

  return { transactions, loadTransactions, transactionsError: error, clearTransactionsError };
}
