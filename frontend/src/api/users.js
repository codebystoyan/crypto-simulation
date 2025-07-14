import { BACKEND_SERVER_URL } from "../config";

export async function getUserBalance(userId) {
    const res = await fetch(`${BACKEND_SERVER_URL}/users/balance/${userId}`, {
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
      });
      if (!res.ok) {
        const err = await res.text();
        throw new Error(`Failed to fetch user balance: ${err}`);
      }
      return res.json();
}

export async function resetUserBalance(userId) {
    const res = await fetch(`${BACKEND_SERVER_URL}/users/balance/reset/${userId}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
      });
      if (!res.ok) {
        const err = await res.text();
        throw new Error(`Failed to reset user balance: ${err}`);
      }
      return res.json();
}