// src/pages/AdminKnjige.jsx
import React, { useEffect, useMemo, useState } from "react";
import http from "../api/http";
import {
  fetchKnjige,
  fetchKnjiga,      // ← koristimo za pun detalj pre EDIT
  createKnjiga,
  updateKnjiga,
  deleteKnjiga,
  norm,
} from "../api/knjige";

const rsd = (n) => Number(n || 0).toLocaleString("sr-RS") + " RSD";

/** Red za AUTORA (autor + uloga) */
function AutorRow({ row, idx, authors, onChange, onRemove }) {
  return (
    <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr auto", gap: 8 }}>
      <select
        className="input"
        value={row.autorId ?? ""}
        onChange={(e) =>
          onChange(idx, {
            ...row,
            autorId: e.target.value ? Number(e.target.value) : null,
          })
        }
      >
        <option value="">— izaberi autora —</option>
        {authors.map((a) => (
          <option key={a.id} value={a.id}>
            {a.ime} {a.prezime}
          </option>
        ))}
      </select>

      <input
        className="input"
        placeholder="Uloga (autor, priređivač, prevodilac...)"
        value={row.uloga || ""}
        onChange={(e) => onChange(idx, { ...row, uloga: e.target.value })}
      />

      <button className="btn" type="button" onClick={() => onRemove(idx)}>
        Ukloni
      </button>
    </div>
  );
}

/** Red za DOSTUPNOST (knjižara + količina) */
function DostupnostRow({ row, idx, shops, onChange, onRemove }) {
  return (
    <div style={{ display: "grid", gridTemplateColumns: "1fr 160px auto", gap: 8 }}>
      <select
        className="input"
        value={row.knjizaraId ?? ""}
        onChange={(e) =>
          onChange(idx, {
            ...row,
            knjizaraId: e.target.value ? Number(e.target.value) : null,
          })
        }
      >
        <option value="">— izaberi knjižaru —</option>
        {shops.map((s) => (
          <option key={s.id} value={s.id}>
            {s.naziv} {s.lokacija ? `– ${s.lokacija}` : ""}
          </option>
        ))}
      </select>

      <input
        className="input"
        type="number"
        min={0}
        placeholder="Količina"
        value={row.kolicina ?? ""}
        onChange={(e) =>
          onChange(idx, {
            ...row,
            kolicina: e.target.value === "" ? null : Number(e.target.value),
          })
        }
      />

      <button className="btn" type="button" onClick={() => onRemove(idx)}>
        Ukloni
      </button>
    </div>
  );
}

export default function AdminKnjige() {
  /** forma */
  const [f, setF] = useState({
    naziv: "",
    isbn: "",
    cena: "",
    godina: "",
    imageUrl: "",
    opis: "",
    zanrId: "",
    autori: [],       // [{autorId, uloga}]
    dostupnost: [],  // [{knjizaraId, kolicina}]
  });

  /** šifarnici */
  const [zanrovi, setZanrovi] = useState([]);
  const [autori, setAutori] = useState([]);
  const [knjizare, setKnjizare] = useState([]);

  /** lista knjiga */
  const [list, setList] = useState([]);
  const [q, setQ] = useState("");
  const [editingId, setEditingId] = useState(null);
  const [busy, setBusy] = useState(false);
  const [msg, setMsg] = useState("");

  /** učitaj šifarnike + knjige */
  useEffect(() => {
    let live = true;
    (async () => {
      try {
        const [z, a, s, knj] = await Promise.all([
          http.get("/zanrovi").catch(() => ({ data: [] })),
          http.get("/autori").catch(() => ({ data: [] })),
          http.get("/knjizare").catch(() => ({ data: [] })),
          fetchKnjige(),
        ]);

        if (!live) return;
        setZanrovi(Array.isArray(z?.data) ? z.data : []);
        setAutori(Array.isArray(a?.data) ? a.data : []);
        setKnjizare(Array.isArray(s?.data) ? s.data : []);
        setList(Array.isArray(knj) ? knj : []);
      } catch (e) {
        console.error(e);
        setMsg("Greška pri učitavanju šifarnika/knjiga.");
      }
    })();
    return () => {
      live = false;
    };
  }, []);

  /** pretraga */
  const filtered = useMemo(() => {
    const s = q.trim().toLowerCase();
    if (!s) return list;
    return list.filter(
      (k) =>
        String(k.naziv || "").toLowerCase().includes(s) ||
        String(k.isbn || "").toLowerCase().includes(s)
    );
  }, [list, q]);

  /** reset forme */
  function resetForm() {
    setF({
      naziv: "",
      isbn: "",
      cena: "",
      godina: "",
      imageUrl: "",
      opis: "",
      zanrId: "",
      autori: [],
      dostupnost: [],
    });
    setEditingId(null);
    setMsg("");
  }

  /** puni formu za izmenu — koristi FULL GET jedne knjige */
  async function handleEditClick(id) {
    try {
      setBusy(true);
      setMsg("");
      const bk = await fetchKnjiga(id); // puni detalj (autori+dostupnost)
      setEditingId(bk.id);
      setF({
        naziv: bk.naziv || "",
        isbn: bk.isbn || "",
        cena: bk.cena ?? "",
        godina: bk.godinaIzdavanja
          ? String(new Date(bk.godinaIzdavanja).getFullYear())
          : "",
        imageUrl: bk.slika || bk.imageUrl || "",
        opis: bk.opis || "",
        zanrId: bk.zanrId || "",

        autori: (bk.autori || []).map((a) => ({
          autorId: a.id,
          uloga: a.uloga || "",
        })),

        dostupnost: (bk.dostupnost || []).map((d) => ({
          knjizaraId: d.knjizaraId,
          kolicina: d.kolicina,
        })),
      });
    } catch (e) {
      const p = e?.response?.data;
      setMsg(
        typeof p === "string" ? p : p?.message || e.message || "Greška pri učitavanju knjige za izmenu."
      );
    } finally {
      setBusy(false);
    }
  }

  /** payload prema backendu */
  function payloadFromForm() {
    const godina_izdanja = f.godina
      ? `${String(f.godina).padStart(4, "0")}-01-01`
      : null;

    return {
      naziv: f.naziv,
      isbn: f.isbn,
      cena: f.cena === "" ? null : Number(f.cena),
      opis: f.opis,
      image_url: f.imageUrl,
      godina_izdanja,
      zanr_id: f.zanrId ? Number(f.zanrId) : null,

      autori: (f.autori || [])
        .filter((r) => r.autorId)
        .map((r) => ({ id: Number(r.autorId), uloga: r.uloga || null })),

      dostupnost: (f.dostupnost || [])
        .filter((r) => r.knjizaraId && r.kolicina != null)
        .map((r) => ({
           knjizara_id: Number(r.knjizaraId),
          kolicina: Number(r.kolicina),
        })),
    };
  }

  /** validacija (osnovna) */
  const formInvalid =
    !String(f.naziv).trim() ||
    !String(f.isbn).trim() ||
    f.cena === "" ||
    f.zanrId === "";

  /** snimi (create ili update) */
  async function handleSubmit(e) {
    const payload = payloadFromForm();
console.log("📦 PAYLOAD KOJI ŠALJEM:", payload);

    e.preventDefault();
    setMsg("");

    if (formInvalid) {
      setMsg("Popuni obavezna polja: Naziv, ISBN, Cena, Žanr.");
      return;
    }

    try {
      setBusy(true);
      const payload = payloadFromForm();
      // console.log("payload:", payload);

      if (editingId) {
        const res = await updateKnjiga(editingId, payload);
        // osveži listu na osnovu odgovora
        setList((prev) => prev.map((x) => (x.id === editingId ? norm(res) : x)));
      } else {
        const res = await createKnjiga(payload);
        setList((prev) => [norm(res), ...prev]);
      }

      resetForm();
    } catch (e) {
      const p = e?.response?.data;
      setMsg(
        typeof p === "string" ? p : p?.message || e.message || "Greška pri snimanju."
      );
    } finally {
      setBusy(false);
    }
  }

  /** brisanje */
  async function handleDelete(id) {
    if (!window.confirm("Obriši ovu knjigu?")) return;
    try {
      await deleteKnjiga(id);
      setList((prev) => prev.filter((x) => x.id !== id));
      if (editingId === id) resetForm();
    } catch (e) {
      alert(e?.response?.data?.message || e.message || "Brisanje neuspešno.");
    }
  }

  /** izmene u redovima autora/dostupnosti */
  const changeAutor = (i, row) =>
    setF((s) => ({
      ...s,
      autori: s.autori.map((r, idx) => (idx === i ? row : r)),
    }));
  const removeAutor = (i) =>
    setF((s) => ({ ...s, autori: s.autori.filter((_, idx) => idx !== i) }));
  const addAutor = () =>
    setF((s) => ({ ...s, autori: [...s.autori, { autorId: null, uloga: "" }] }));

  const changeDost = (i, row) =>
    setF((s) => ({
      ...s,
      dostupnost: s.dostupnost.map((r, idx) => (idx === i ? row : r)),
    }));
  const removeDost = (i) =>
    setF((s) => ({
      ...s,
      dostupnost: s.dostupnost.filter((_, idx) => idx !== i),
    }));
  const addDost = () =>
    setF((s) => ({
      ...s,
      dostupnost: [...s.dostupnost, { knjizaraId: null, kolicina: null }],
    }));

  return (
    <div className="container" style={{ padding: "28px 0" }}>
      <h1>Upravljanje knjigama</h1>

      {editingId ? (
        <div className="note" style={{ marginBottom: 8 }}>
          Režim izmene za knjigu #{editingId}
        </div>
      ) : null}

      <form className="panel" onSubmit={handleSubmit} style={{ display: "grid", gap: 10 }}>
        <div className="grid" style={{ gridTemplateColumns: "1fr 1fr", gap: 10 }}>
          <input
            className="input"
            placeholder="Naziv"
            value={f.naziv}
            onChange={(e) => setF({ ...f, naziv: e.target.value })}
          />
          <input
            className="input"
            placeholder="Cena"
            type="number"
            min={0}
            value={f.cena}
            onChange={(e) => setF({ ...f, cena: e.target.value })}
          />

          <input
            className="input"
            placeholder="ISBN"
            value={f.isbn}
            onChange={(e) => setF({ ...f, isbn: e.target.value })}
          />
          <input
            className="input"
            placeholder="Godina izdanja (YYYY)"
            value={f.godina}
            onChange={(e) => setF({ ...f, godina: e.target.value })}
          />

          <input
            className="input"
            placeholder="URL slike"
            value={f.imageUrl}
            onChange={(e) => setF({ ...f, imageUrl: e.target.value })}
          />
          <select
            className="input"
            value={f.zanrId}
            onChange={(e) => setF({ ...f, zanrId: e.target.value })}
          >
            <option value="">— izaberi žanr —</option>
            {zanrovi.map((z) => (
              <option key={z.id} value={z.id}>
                {z.naziv}
              </option>
            ))}
          </select>
        </div>

        <textarea
          className="input"
          rows={3}
          placeholder="Opis"
          value={f.opis}
          onChange={(e) => setF({ ...f, opis: e.target.value })}
        />

        {/* AUTORI */}
        <div className="panel" style={{ display: "grid", gap: 8 }}>
          <div style={{ fontWeight: 700 }}>Autori i uloge</div>
          {(f.autori || []).map((row, idx) => (
            <AutorRow
              key={idx}
              row={row}
              idx={idx}
              authors={autori}
              onChange={changeAutor}
              onRemove={removeAutor}
            />
          ))}
          <button className="btn" type="button" onClick={addAutor}>
            + Dodaj autora
          </button>
        </div>

        {/* DOSTUPNOST */}
        <div className="panel" style={{ display: "grid", gap: 8 }}>
          <div style={{ fontWeight: 700 }}>Dostupnost po knjižarama</div>
          {(f.dostupnost || []).map((row, idx) => (
            <DostupnostRow
              key={idx}
              row={row}
              idx={idx}
              shops={knjizare}
              onChange={changeDost}
              onRemove={removeDost}
            />
          ))}
          <button className="btn" type="button" onClick={addDost}>
            + Dodaj knjižaru
          </button>
        </div>

        {msg && <div className="panel error">{msg}</div>}

        <div style={{ display: "flex", gap: 8 }}>
          <button className="btn primary" type="submit" disabled={busy}>
            {editingId ? "Sačuvaj izmene" : "Dodaj"}
          </button>
          {editingId && (
            <button type="button" className="btn" onClick={resetForm}>
              Otkaži izmene
            </button>
          )}
        </div>
      </form>

      {/* Lista knjiga */}
      <div style={{ marginTop: 20 }}>
        <div
          style={{
            display: "flex",
            justifyContent: "space-between",
            alignItems: "center",
            marginBottom: 8,
          }}
        >
          <h3 style={{ margin: 0 }}>Lista knjiga</h3>
          <input
            className="input"
            placeholder="Pretraga (naziv, ISBN)"
            value={q}
            onChange={(e) => setQ(e.target.value)}
          />
        </div>

        <div className="panel" style={{ padding: 0 }}>
          <table className="table">
            <thead>
              <tr>
                <th>Naziv</th>
                <th>Cena</th>
                <th>ISBN</th>
                <th>Žanr</th>
                <th style={{ width: 220 }}>Akcije</th>
              </tr>
            </thead>
            <tbody>
              {filtered.map((b) => (
                <tr key={b.id}>
                  <td>{b.naziv}</td>
                  <td>{rsd(b.cena)}</td>
                  <td>{b.isbn}</td>
                  <td>{b.zanr || "-"}</td>
                  <td>
                    <div style={{ display: "flex", gap: 8, flexWrap: "wrap" }}>
                      <button className="btn" onClick={() => handleEditClick(b.id)}>
                        Izmeni
                      </button>
                      <button
                        className="btn danger"
                        onClick={() => handleDelete(b.id)}
                      >
                        Obriši
                      </button>
                    </div>
                  </td>
                </tr>
              ))}
              {!filtered.length && (
                <tr>
                  <td colSpan={5} style={{ padding: 16 }}>
                    Nema rezultata.
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}
