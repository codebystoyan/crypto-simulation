import React, { useState } from "react";
import TradeForm from "./TradeForm";
import "../styles/PriceTable.css";

const PriceTable = ({ prices, onTrade, holdings }) => {
    const [modalData, setModalData] = useState(null);

    const openModal = (symbol, type, price) => {
        setModalData({ symbol, type, price });
    };

    const closeModal = () => setModalData(null);

    if (!prices || Object.keys(prices).length === 0) {
        return <div>Loading prices...</div>;
    }

    return (
        <>
            <div className="grid-container-wrapper">
                <div className="grid-container">
                    {Object.entries(prices).map(([symbol, price]) => (
                        <div key={symbol} className="card">
                            <h3>{symbol}</h3>
                            <p className="price-text">${price.toFixed(2)}</p>
                            <div className="button-group">
                                <button
                                    className="buy-button"
                                    onClick={() => openModal(symbol, "BUY", price)}
                                >
                                    Buy
                                </button>
                                <button
                                    className="sell-button"
                                    onClick={() => openModal(symbol, "SELL", price)}
                                >
                                    Sell
                                </button>
                            </div>
                        </div>
                    ))}
                </div>
            </div>
            {modalData && (
                <TradeForm
                    symbol={modalData.symbol}
                    type={modalData.type}
                    price={modalData.price}
                    onClose={closeModal}
                    onConfirm={onTrade}
                    holdings={holdings}
                />
            )}
        </>
    );
};

export default PriceTable;
