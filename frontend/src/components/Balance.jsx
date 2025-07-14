import React from 'react';
import "../styles/Balance.css"

const Balance = ({ balance, onReset }) => {
  return (
    <div className="user-balance-container">
      Balance: ${balance.toFixed(2)}
      <button className="reset-btn" onClick={onReset} style={{ marginTop: '4px' }}>
        Reset Balance
      </button>
    </div>
  );
};

export default Balance;
