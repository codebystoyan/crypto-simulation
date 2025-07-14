import { useEffect, useState } from 'react';
import { useTransactions } from './useTransactions';
import { useHoldings } from './useHoldings';
import { useUserBalance } from './useUserBalance';
import { GLOBAL_USER_ID } from '../config';
import { createTransaction } from '../api/transactions';

export function usePortfolioManager() {
  const { holdings, reloadHoldings, holdingsError, clearHoldingsError } = useHoldings();
  const { balance, reloadBalance, resetBalance, balanceError, clearBalanceError } = useUserBalance(GLOBAL_USER_ID);
  const { transactions, loadTransactions, transactionsError, clearTransactionsError } = useTransactions();
  
  const [errorMessages, setErrorMessages] = useState([]);

  useEffect(() => {
    const allErrors = [
      holdingsError,
      balanceError,
      transactionsError,
    ].filter(Boolean);

    setErrorMessages(allErrors);
  }, [holdingsError, balanceError, transactionsError]);

  const clearErrors = () => {
    clearHoldingsError();
    clearBalanceError();
    clearTransactionsError();
    setErrorMessages([]);
  }

  const updatePortfolio = async (transaction) => {
    try {
      await createTransaction(transaction);
      await Promise.all([
        reloadHoldings(),
        reloadBalance(),
        loadTransactions(),
      ]);
    } catch (err) {
      setErrorMessages(prev => [...prev, err.message || 'Unknown error']);
    }
  };

  return {
    holdings,
    balance,
    transactions,
    updatePortfolio,
    resetBalance,
    errorMessages,
    clearErrors
  };
}
