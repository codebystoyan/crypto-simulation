import React, { useState } from "react";
import { GLOBAL_USER_ID } from "../config";
import "../styles/TradeForm.css";

const TradeForm = ({ symbol, type, price, onClose, onConfirm, holdings }) => {
  const [mode, setMode] = useState("USD"); // "USD" or "CRYPTO"
  const [amount, setAmount] = useState("");

  const baseSymbol = symbol.split("/")[0];
  const userHolding = holdings?.find(h => h.symbol === baseSymbol)?.amount || 0;

  const handleSubmit = () => {
    const amountNum = parseFloat(amount);
    if (!amount || isNaN(amountNum) || amountNum <= 0) {
      alert("Please enter a valid amount");
      return;
    }

    const cryptoAmount = mode === "USD" ? amountNum / price : amountNum;
    const fiatAmount = cryptoAmount * price;

    const transaction = {
      userId: GLOBAL_USER_ID,
      symbol: baseSymbol,
      amount: cryptoAmount,
      fiatAmount,
      type,
      timestamp: new Date().toISOString(),
    };

    onConfirm(transaction);
    onClose();
  };

  const handleSellAll = () => {
    if (type === "SELL" && mode === "CRYPTO") {
      setAmount(userHolding.toString());
    } else if (type === "SELL" && mode === "USD") {
      setAmount((userHolding * price).toString());
    }
  };

  const handleModeChange = (newMode) => {
    const amountNum = parseFloat(amount);
    if (!amount || isNaN(amountNum)) {
      setMode(newMode);
      return;
    }
  
    if (newMode === "USD" && mode === "CRYPTO") {
      setAmount((amountNum * price).toFixed(2));
    } else if (newMode === "CRYPTO" && mode === "USD") {
      setAmount((amountNum / price).toFixed(8));
    }
  
    setMode(newMode);
  };

  return (
    <>
      <div className="trade-form-modal">
        <h3 className="trade-form-header">{type} {symbol}</h3>
        <h4 className="trade-form-subheader">Price: ${price.toFixed(2)}</h4>

        {type === "SELL" && (
          <p className="trade-form-holdings">Current Holdings: {userHolding.toFixed(8)} {baseSymbol}</p>
        )}

        <div className="trade-form-radio-group">
          <label>
            <input
              type="radio"
              name="mode"
              value="USD"
              checked={mode === "USD"}
              onChange={() => handleModeChange("USD")}
            /> USD
          </label>
          {" "}
          <label>
            <input
              type="radio"
              name="mode"
              value="CRYPTO"
              checked={mode === "CRYPTO"}
              onChange={() => handleModeChange("CRYPTO")}
            /> {baseSymbol}
          </label>
        </div>

        <input
          className="trade-form-input"
          type="number"
          placeholder={mode === "USD" ? "Amount in USD" : `Amount in ${baseSymbol}`}
          value={amount}
          onChange={(e) => setAmount(e.target.value)}
          min="0"
          step="any"
        />

        {type === "SELL" && (
          <button className="trade-form-sell-all" onClick={handleSellAll}>
            Sell All
          </button>
        )}

        {amount && !isNaN(parseFloat(amount)) && (
          <p className="trade-form-message">
            {type === "BUY" && mode === "USD" && (
              <>You will receive {(parseFloat(amount) / price).toFixed(8)} {baseSymbol} for ${parseFloat(amount).toFixed(2)}</>
            )}
            {type === "BUY" && mode === "CRYPTO" && (
              <>You will buy {parseFloat(amount).toFixed(8)} {baseSymbol} for ${(parseFloat(amount) * price).toFixed(2)}</>
            )}
            {type === "SELL" && mode === "USD" && (
              <>You will sell {(parseFloat(amount) / price).toFixed(8)} {baseSymbol} for ${parseFloat(amount).toFixed(2)}</>
            )}
            {type === "SELL" && mode === "CRYPTO" && (
              <>You will sell {parseFloat(amount).toFixed(8)} {baseSymbol} for ${(parseFloat(amount) * price).toFixed(2)}</>
            )}
          </p>
        )}

        <div style={{ marginTop: "10px" }}>
          <button className="trade-form-button" onClick={handleSubmit}>Confirm</button>
          <button className="trade-form-button trade-form-button-secondary" onClick={onClose}>
            Cancel
          </button>
        </div>
      </div>

      {/* Modal Backdrop */}
      <div onClick={onClose} className="trade-form-backdrop" />
    </>
  );
};

export default TradeForm;
