// src/pages/AdminPorudzbine.jsx
import React, { useEffect, useMemo, useState } from "react";
import { adminDeletePorudzbina, adminListPorudzbine, adminSetStatus } from "../api/adminPorudzbine";

const statOptions = ["KREIRANA", "OBRADJENA", "POSLATA", "OTKAZANA"];

export default function AdminPorudzbine() {
  const [list, setList] = useState([]);
  const [q, setQ] = useState("");
  const [loading, setLoading] = useState(true);
  const [err, setErr] = useState("");

  async function load() {
    try {
      setErr("");
      setLoading(true);
      const rows = await adminListPorudzbine();
      setList(rows);
    } catch (e) {
      const p = e?.response?.data;
      setErr(typeof p === "string" ? p : p?.message || e.message || "Greška pri učitavanju.");
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => { load(); }, []);

  const prikaz = useMemo(() => {
    const s = q.trim().toLowerCase();
    if (!s) return list;
    return list.filter(r =>
      String(r.id).includes(s) ||
      String(r.korisnikEmail || "").toLowerCase().includes(s) ||
      String(r.korisnikId || "").includes(s)
    );
  }, [list, q]);

  async function handleStatusChange(id, status) {
    try {
      const updated = await adminSetStatus(id, status);
      // optimistički update UI-ja
      setList(prev => prev.map(p => (p.id === id ? updated : p)));
    } catch (e) {
      alert(e?.response?.data?.message || e.message);
    }
  }

  async function handleDelete(id) {
    if (!window.confirm("Obrisati porudžbinu?")) return;
    try {
      await adminDeletePorudzbina(id);
      setList(prev => prev.filter(p => p.id !== id));
    } catch (e) {
      alert(e?.response?.data?.message || e.message);
    }
  }

  return (
    <div className="container" style={{ padding: "28px 0", maxWidth: 980 }}>
      <h1>Porudžbine (Admin)</h1>
      <p className="muted">Upravljanje porudžbinama (promena statusa, brisanje).</p>

      <div style={{ display:"flex", gap:8, margin:"10px 0 16px" }}>
        <input className="input" placeholder="Pretraga (ID, korisnik...)" value={q} onChange={(e)=>setQ(e.target.value)} />
        <button className="btn" onClick={load}>Osveži</button>
      </div>

      {loading && <div className="note">Učitavam…</div>}
      {err && <div className="panel error">{err}</div>}

      {!loading && !err && (
        <div className="table-wrap">
          <table className="table">
            <thead>
              <tr>
                <th>ID</th>
                <th>Korisnik</th>
                <th>Datum</th>
                <th>Iznos</th>
                <th>Status</th>
                <th>Akcije</th>
              </tr>
            </thead>
            <tbody>
              {prikaz.map((r) => (
                <tr key={r.id}>
                  <td>{r.id}</td>
                  <td>
                    {r.korisnikEmail || (r.korisnikId ? `#${r.korisnikId}` : "—")}
                  </td>
                  <td>
                    {r.datum ? new Date(r.datum).toLocaleDateString("sr-RS") : "—"}
                  </td>
                  <td><strong>{(r.ukupanIznos || 0).toLocaleString("sr-RS")} RSD</strong></td>
                  <td>
                    <select
                      className="input"
                      value={r.status || "KREIRANA"}
                      onChange={(e) => handleStatusChange(r.id, e.target.value)}
                    >
                      {statOptions.map(s => <option key={s} value={s}>{s}</option>)}
                    </select>
                  </td>
                  <td>
                    <button className="btn danger" onClick={() => handleDelete(r.id)}>
                      Obriši
                    </button>
                  </td>
                </tr>
              ))}
              {!prikaz.length && (
                <tr><td colSpan={6} className="muted">Nema rezultata.</td></tr>
              )}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}
