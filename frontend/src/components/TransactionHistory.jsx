import React from 'react';
import '../styles/TransactionHistory.css'; 

function TransactionHistory({ transactions }) {
  if (!transactions.length) return <div className="empty-message">No transactions yet</div>;

  return (
    <div className="transaction-history-container">
      <table className="transaction-table">
        <thead>
          <tr>
            <th>Type</th>
            <th>Symbol</th>
            <th>Amount</th>
            <th>Price</th>
            <th>Date</th>
          </tr>
        </thead>
        <tbody>
          {transactions.map(tx => (
            <tr key={tx.id}>
              <td className={tx.type.toLowerCase()}>{tx.type}</td>
              <td>{tx.symbol}</td>
              <td>{tx.amount}</td>
              <td>${tx.fiatAmount.toFixed(2)}</td>
              <td>{new Date(tx.timestamp).toLocaleString()}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default TransactionHistory;
