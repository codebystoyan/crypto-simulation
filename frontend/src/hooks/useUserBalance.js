import { useState, useEffect } from 'react';
import { getUserBalance, resetUserBalance } from '../api/users';

export function useUserBalance(userId) {
  const [balance, setBalance] = useState(0);
  const [error, setError] = useState(null);

  const clearBalanceError = () => setError(null);

  const reloadBalance = async () => {
    try {
      const data = await getUserBalance(userId);
      setBalance(data);
      setError(null);
    } catch (err) {
      console.error('Failed to fetch balance:', err);
      setError(err.message || 'Failed to fetch balance');
    }
  };

  const resetBalance = async () => {
    try {
      await resetUserBalance(userId);
      await reloadBalance();
    } catch (err) {
      console.error('Failed to reset balance:', err);
      setError(err.message || 'Failed to reset balance');
    }
  };

  useEffect(() => { reloadBalance(); }, []);

  return { balance, reloadBalance, resetBalance, balanceError: error, clearBalanceError };
}
