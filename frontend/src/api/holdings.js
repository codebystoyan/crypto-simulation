export async function getHoldings(userId) {
    const res = await fetch(`http://localhost:8080/holdings/user/${userId}`, {
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
      });
      if (!res.ok) {
        const err = await res.text();
        throw new Error(`Failed to fetch holdings: ${err}`);
      }
      return res.json();
}