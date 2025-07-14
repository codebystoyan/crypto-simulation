import { BACKEND_SERVER_URL } from "../config";

export async function createTransaction(transaction) {
    const res = await fetch(`${BACKEND_SERVER_URL}/transactions`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(transaction),
    });
    if (!res.ok) {
      const err = await res.text();
      throw new Error(`Failed to create transaction: ${err}`);
    }
    return res.json();
  }
  
export async function getAllTransactions(userId) {
  const res = await fetch(`${BACKEND_SERVER_URL}/transactions/user/${userId}`, {
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
  });
  if (!res.ok) {
    const err = await res.text();
    throw new Error(`Failed to fetch transactions: ${err}`);
  }
  return res.json();
}