import React, { useState } from 'react';
import '../styles/Holdings.css';

function Holdings({ holdings }) {
  const [expanded, setExpanded] = useState(false);

  const displayedHoldings = expanded ? holdings : holdings.slice(0, 3);
  const shouldScroll = expanded && holdings.length > 7;

  return (
    <div className="holdings-container">
      <div
        className="holdings-card"
        style={{
          height: shouldScroll ? '400px' : 'auto',
          overflowY: shouldScroll ? 'auto' : 'visible',
        }}
      >
        <h2 className="holdings-header">Your Portfolio</h2>

        {displayedHoldings.length === 0 ? (
          <p className="holdings-empty">No holdings yet</p>
        ) : (
          <table className="holdings-table">
            <thead>
              <tr>
                <th className="holdings-th">Symbol</th>
                <th className="holdings-th">Amount</th>
                <th className="holdings-th">Updated</th>
              </tr>
            </thead>
            <tbody>
              {displayedHoldings.map((h) => (
                <tr key={h.symbol}>
                  <td className="holdings-td">{h.symbol}</td>
                  <td className="holdings-td">{h.amount.toFixed(4)}</td>
                  <td className="holdings-td">
                    {new Date(h.lastUpdated).toLocaleString()}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}

        {holdings.length > 3 && (
          <button
            onClick={() => setExpanded(!expanded)}
            className="holdings-button"
          >
            {expanded ? 'Show Less' : 'Show All'}
          </button>
        )}
      </div>
    </div>
  );
}

export default Holdings;
