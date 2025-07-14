import React, { useEffect } from 'react';
import "../styles/ErrorToast.css";

const ErrorToast = ({ messages, onClear }) => {
    useEffect(() => {
        if (messages.length) {
            const timer = setTimeout(onClear, 3000);
            return () => clearTimeout(timer);
        }
    }, [messages, onClear]);

    if (!messages.length) {
        return null;
    }

    return (
        <div className="error-container" onClick={onClear}>
            {messages.map((msg, i) => (
                <div key={i}>{msg}</div>
            ))}
            <small>Click to dismiss</small>
        </div>
    );
};

export default ErrorToast;
