import React, { useState, useMemo } from 'react';
import { useCryptoPrices } from './hooks/useCryptoPrices';
import PriceTable from './components/PriceTable';
import Holdings from './components/Holdings';
import Balance from './components/Balance';
import { usePortfolioManager } from './hooks/usePortfolioManager';
import TransactionHistory from './components/TransactionHistory';
import './styles/App.css';
import ErrorToast from './components/ErrorToast';

function App() {
  const topCurrencies = useMemo(() => [
    "BTC/USD", "ETH/USD", "USDT/USD", "BNB/USD", "SOL/USD",
    "XRP/USD", "DOGE/USD", "ADA/USD", "AVAX/USD", "DOT/USD",
    "LINK/USD", "MATIC/USD", "TON/USD", "BCH/USD", "TRX/USD",
    "XLM/USD", "UNI/USD", "ETC/USD", "ATOM/USD", "FIL/USD"
  ], []);

  const prices = useCryptoPrices(topCurrencies);
  const { 
    holdings,
    balance,
    transactions,
    updatePortfolio,
    resetBalance,
    errorMessages,
    clearErrors
  } = usePortfolioManager();
  
  const [showHistory, setShowHistory] = useState(false);

  return (
    <>
      <header className="app-header">
        <h1 className="platform-name">Crypto Simulator</h1>
        <Balance balance={balance} onReset={resetBalance} />
      </header>

      <main className="app-main">
        <div className="contents-table">
          <div className="history-toggle-container">
            <button
              className="history-toggle-btn"
              onClick={() => setShowHistory(!showHistory)}
            >
              {showHistory ? 'Back to Prices' : 'View History'}
            </button>
          </div>

          <div className="prices-and-holdings">
            <div className="prices-container">
              {showHistory ? (
                <TransactionHistory transactions={transactions} />
              ) : (
                <PriceTable prices={prices} onTrade={updatePortfolio} holdings={holdings} />
              )}
            </div>
          </div>
        </div>

        <div className="holdings-container">
          <Holdings holdings={holdings} />
        </div>

        <div>
          <ErrorToast messages={errorMessages} onClear={clearErrors} />
        </div>
      </main>
    </>
  );
}

export default App;
