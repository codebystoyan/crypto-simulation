import { BACKEND_SERVER_URL } from "../config";

export async function getHoldings(userId) {
    const res = await fetch(`${BACKEND_SERVER_URL}/holdings/user/${userId}`, {
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
      });
      if (!res.ok) {
        const err = await res.text();
        throw new Error(`Failed to fetch holdings: ${err}`);
      }
      return res.json();
}