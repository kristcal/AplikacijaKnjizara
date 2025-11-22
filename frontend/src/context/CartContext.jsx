import React, { createContext, useContext, useEffect, useMemo, useState } from "react";
import { useAuth } from "../context/AuthContext";

const CartCtx = createContext(null);
export const useCart = () => useContext(CartCtx);

// helper za ključ po korisniku
const keyFor = (user) => `cart_${user?.id ?? "guest"}`;

export function CartProvider({ children }) {
  const { user } = useAuth() || {};
  const [items, setItems] = useState([]);

  // UČITAJ iz localStorage kada se promeni user
  useEffect(() => {
    try {
      const raw = localStorage.getItem(keyFor(user));
      const parsed = raw ? JSON.parse(raw) : [];
      setItems(Array.isArray(parsed) ? parsed : []);
    } catch {
      setItems([]);
    }
  }, [user]);

  // SAČUVAJ u localStorage kada se promeni korpa ili user
  useEffect(() => {
    try {
      localStorage.setItem(keyFor(user), JSON.stringify(items));
    } catch {}
  }, [items, user]);

  // Dodaj knjigu (spaja stavke po ID-u) – dozvoljava višestruke iste knjige
  function add(book, qty = 1) {
    const id = book.id ?? book.knjigaId;
    if (!id) return;

    setItems((prev) => {
      const i = prev.findIndex((x) => x.id === id);
      const max = Number(book.kolicina ?? Infinity); // ako imaš ukupnu dostupnost
      if (i === -1) {
        const startQty = Math.max(1, Number(qty || 1));
        const clamped = Number.isFinite(max) ? Math.min(startQty, max) : startQty;
        return [
          ...prev,
          {
            id,
            naziv: book.naziv,
            cena: Number(book.cena || 0),
            slika: book.slika || book.imageUrl || "",
            kolicina: clamped,
            maxKolicina: Number.isFinite(max) ? max : null,
          },
        ];
      } else {
        const copy = [...prev];
        const current = copy[i].kolicina || 0;
        let next = current + Number(qty || 1);
        if (Number.isFinite(max)) next = Math.min(next, max);
        copy[i] = { ...copy[i], kolicina: next };
        return copy;
      }
    });
  }

  // Postavi tačnu količinu (broj input u korpi)
  function setQty(id, qty) {
    setItems((prev) => {
      const copy = prev.map((x) => ({ ...x }));
      const i = copy.findIndex((x) => x.id === id);
      if (i === -1) return prev;
      const max = copy[i].maxKolicina ?? Infinity;
      let n = Number(qty || 0);
      if (n < 1) n = 1;
      if (Number.isFinite(max)) n = Math.min(n, max);
      copy[i].kolicina = n;
      return copy;
    });
  }

  // Ukloni stavku
  function remove(id) {
    setItems((prev) => prev.filter((x) => x.id !== id));
  }

  // Očisti korpu
  function clear() {
    setItems([]);
  }

  const total = useMemo(
    () => items.reduce((s, x) => s + Number(x.cena || 0) * Number(x.kolicina || 0), 0),
    [items]
  );

  const value = useMemo(
    () => ({ items, add, setQty, remove, clear, total }),
    [items, total]
  );

  return <CartCtx.Provider value={value}>{children}</CartCtx.Provider>;
}
