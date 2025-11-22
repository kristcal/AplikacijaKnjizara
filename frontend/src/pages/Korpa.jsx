import React, { useMemo, useState } from "react";
import { useNavigate } from "react-router-dom";
import { useCart } from "../context/CartContext";
import { useAuth } from "../context/AuthContext";
import { kreirajPorudzbinu } from "../api/porudzbine";

const rsd = (n) => Number(n || 0).toLocaleString("sr-RS") + " RSD";

export default function Korpa() {
  const navigate = useNavigate();
  const { user } = useAuth() || {};
  const { items, setQty, remove, clear, total } = useCart();
  const [busy, setBusy] = useState(false);
  const [err, setErr] = useState("");

  const hasItems = items.length > 0;

  // validacija količina (min 1, ne preko maxKolicina ako postoji)
  const invalid = useMemo(() => {
    return items.some((x) => {
      const q = Number(x.kolicina || 0);
      if (q < 1) return true;
      if (Number.isFinite(x.maxKolicina ?? Infinity) && q > x.maxKolicina) return true;
      return false;
    });
  }, [items]);

  async function handleOrder() {
    setErr("");

    if (!user) {
      navigate("/login", { state: { from: "/korpa" } });
      return;
    }
    if (!hasItems) {
      setErr("Korpa je prazna.");
      return;
    }
    if (invalid) {
      setErr("Proveri količine (min 1, ne preko dostupnog).");
      return;
    }

    try {
      setBusy(true);
      // payload koji backend očekuje
      const stavke = items.map((x) => ({
        knjiga_id: x.id,
        kolicina: Number(x.kolicina || 1),
        cena: Number(x.cena || 0),
      }));

      await kreirajPorudzbinu({ korisnikId: user.id, stavke });
      clear();
      navigate("/moje-porudzbine");
    } catch (e) {
      setErr(e.message || "Greška pri kreiranju porudžbine.");
    } finally {
      setBusy(false);
    }
  }

  return (
    <div className="container" style={{ padding: "28px 0", maxWidth: 900 }}>
      <h1>Korpa</h1>

      {!hasItems && <div className="panel" style={{ marginTop: 12 }}>Korpa je prazna.</div>}

      {hasItems && (
        <div className="panel" style={{ marginTop: 12, overflowX: "auto" }}>
          <table className="table">
            <thead>
              <tr>
                <th colSpan={2}>Artikal</th>
                <th style={{ width: 120 }}>Cena</th>
                <th style={{ width: 140 }}>Količina</th>
                <th style={{ width: 120 }}>Ukupno</th>
                <th style={{ width: 100 }}>Akcija</th>
              </tr>
            </thead>
            <tbody>
              {items.map((x) => {
                const max = x.maxKolicina ?? null;
                return (
                  <tr key={x.id}>
                    <td style={{ width: 64 }}>
                      <img
                        alt={x.naziv}
                        src={x.slika || "https://placehold.co/80x100?text=Knjiga"}
                        style={{ width: 64, height: 84, objectFit: "cover", borderRadius: 6 }}
                        onError={(e) => {
                          e.currentTarget.src = "https://placehold.co/80x100?text=Knjiga";
                        }}
                      />
                    </td>
                    <td>{x.naziv}</td>
                    <td>{rsd(x.cena)}</td>
                    <td>
                      {/* Bez +/-, samo brojčani input kako si tražila */}
                      <input
                        className="input"
                        type="number"
                        min={1}
                        {...(max ? { max } : {})}
                        value={x.kolicina}
                        onChange={(e) => setQty(x.id, e.target.value)}
                        style={{ width: 100 }}
                      />
                      {max ? (
                        <div className="muted" style={{ fontSize: 12, marginTop: 4 }}>
                          Dostupno: {max}
                        </div>
                      ) : null}
                    </td>
                    <td style={{ fontWeight: 700 }}>{rsd(x.cena * x.kolicina)}</td>
                    <td>
                      <button className="btn danger" onClick={() => remove(x.id)}>
                        Ukloni
                      </button>
                    </td>
                  </tr>
                );
              })}
              <tr>
                <td colSpan={4} style={{ textAlign: "right", fontWeight: 700 }}>
                  Ukupno:
                </td>
                <td style={{ fontWeight: 700 }}>{rsd(total)}</td>
                <td />
              </tr>
            </tbody>
          </table>

          {err && (
            <div className="panel error" style={{ marginTop: 10 }}>
              {err}
            </div>
          )}

          <div style={{ display: "flex", gap: 8, justifyContent: "flex-end", marginTop: 12 }}>
            <button className="btn" onClick={() => clear()} disabled={busy}>
              Isprazni
            </button>
            <button
              className="btn primary"
              onClick={handleOrder}
              disabled={busy || !hasItems || invalid}
              title={invalid ? "Proveri količine" : ""}
            >
              {busy ? "Slanje..." : "Poruči"}
            </button>
          </div>
        </div>
      )}
    </div>
  );
}
