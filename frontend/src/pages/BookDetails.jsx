// src/pages/BookDetails.jsx
import React, { useEffect, useState, useMemo } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { fetchKnjiga } from "../api/knjige";
import { useCart } from "../context/CartContext";
import { useAuth } from "../context/AuthContext";

const rsd = (n) => Number(n || 0).toLocaleString("sr-RS") + " RSD";

export default function BookDetails() {
  const { id } = useParams();
  const navigate = useNavigate();
  const { user } = useAuth() || {};
  const { add } = useCart() || {};

  const [book, setBook] = useState(null);
  const [err, setErr] = useState("");
  const [loading, setLoading] = useState(true);

  // učitaj knjigu
  useEffect(() => {
    let live = true;
    (async () => {
      try {
        setErr("");
        setLoading(true);
        const data = await fetchKnjiga(id);
        if (live) setBook(data);
      } catch (e) {
        const p = e?.response?.data;
        const text = typeof p === "string" ? p : p?.message || e.message || "Greška pri učitavanju.";
        setErr(text);
      } finally {
        if (live) setLoading(false);
      }
    })();
    return () => {
      live = false;
    };
  }, [id]);

  const autoriText = useMemo(() => {
    if (!book?.autori?.length) return "";
    return book.autori
      .map((a) =>
        `${a.ime ?? ""} ${a.prezime ?? ""}${a.uloga ? ` (${a.uloga})` : ""}`.trim()
      )
      .join(", ");
  }, [book]);

  const godina = useMemo(() => {
    const g = book?.godinaIzdavanja || book?.godina_izdanja;
    if (!g) return "";
    try {
      return new Date(g).getFullYear(); // LocalDate -> "YYYY-MM-DD"
    } catch {
      return String(g);
    }
  }, [book]);

  const totalKolicina = Number(book?.kolicina ?? 0);
  const isAdmin = user?.uloga === "ADMIN";

  function handleAdd() {
    // ako nije ulogovan → vodi na login i posle vrati na detalje
    if (!user) {
      navigate("/login", { state: { from: `/knjige/${id}` } });
      return;
    }
    // admin ne naručuje
    if (isAdmin) return;
    // nema na stanju → nemoj dodavati
    if (totalKolicina <= 0) return;

    add(book);
  }

  return (
    <div className="container" style={{ padding: "28px 0", maxWidth: 1000 }}>
      <div style={{ marginBottom: 12 }}>
        <button className="btn-ghost" onClick={() => navigate(-1)} title="Nazad">
          ← Nazad
        </button>
      </div>

      {loading && <div className="note">Učitavam…</div>}
      {err && <div className="panel error">{err}</div>}

      {!loading && !err && book && (
        <div
          className="panel"
          style={{
            display: "grid",
            gridTemplateColumns: "280px 1fr",
            gap: 24,
          }}
        >
          {/* Slika */}
          <div
            style={{
              width: "100%",
              aspectRatio: "3/4",
              background: "#f5f5f7",
              borderRadius: 12,
              overflow: "hidden",
            }}
          >
            <img
              src={
                book.slika ||
                book.imageUrl ||
                "https://placehold.co/600x800?text=Knjiga"
              }
              alt={book.naziv}
              style={{ width: "100%", height: "100%", objectFit: "cover" }}
              onError={(e) => {
                e.currentTarget.src =
                  "https://placehold.co/600x800?text=Nema+slike";
              }}
            />
          </div>

          {/* Detalji */}
          <div style={{ display: "grid", gap: 12 }}>
            <h1 style={{ margin: 0 }}>{book.naziv}</h1>

            {autoriText && (
              <div className="muted" style={{ fontSize: 16 }}>
                {autoriText}
              </div>
            )}

            <div
              className="muted"
              style={{ display: "flex", gap: 12, flexWrap: "wrap" }}
            >
              <span>
                Žanr: <strong>{book.zanr || "—"}</strong>
              </span>
              {godina && (
                <span>
                  Godina: <strong>{godina}</strong>
                </span>
              )}
              {book.isbn && (
                <span>
                  ISBN: <strong>{book.isbn}</strong>
                </span>
              )}
            </div>

            {book.opis && (
              <p style={{ marginTop: 4, lineHeight: 1.6 }}>{book.opis}</p>
            )}

            <div
              style={{
                marginTop: 6,
                display: "flex",
                alignItems: "center",
                justifyContent: "space-between",
                gap: 12,
                flexWrap: "wrap",
              }}
            >
              <div style={{ fontSize: 22, fontWeight: 700 }}>
                {rsd(book.cena)}
              </div>

              <div className="muted">
                Dostupno: <strong>{totalKolicina}</strong> kom.
              </div>

              {!isAdmin && (
                <>
                  {totalKolicina > 0 ? (
                    <button className="btn primary" onClick={handleAdd}>
                      Dodaj u korpu
                    </button>
                  ) : (
                    <button
                      className="btn-disabled"
                      disabled
                      title="Knjiga trenutno nije dostupna"
                      style={{
                        background: "#ccc",
                        cursor: "not-allowed",
                        color: "#666",
                        border: "none",
                      }}
                    >
                      Nema na stanju
                    </button>
                  )}
                </>
              )}
            </div>

            {/* DOSTUPNOST PO KNJIŽARAMA */}
            {Array.isArray(book.dostupnost) && book.dostupnost.length > 0 && (
              <div className="panel" style={{ marginTop: 18 }}>
                <div style={{ fontWeight: 700, marginBottom: 10 }}>
                  Dostupnost po knjižarama
                </div>
                <div style={{ display: "grid", gap: 8 }}>
                  {book.dostupnost.map((d, i) => {
                    const lok = d.lokacija || d.knjizara_lokacija;
                    const label =
                      lok ||
                      (d.knjizaraNaziv
                        ? d.knjizaraNaziv
                        : `Knjizara #${d.knjizaraId ?? "?"}`);
                    return (
                      <div
                        key={i}
                        style={{
                          display: "flex",
                          justifyContent: "space-between",
                        }}
                      >
                        <div className="muted">{label}</div>
                        <div style={{ fontWeight: 700 }}>
                          {d.kolicina} kom.
                        </div>
                      </div>
                    );
                  })}
                </div>
              </div>
            )}
          </div>
        </div>
      )}
    </div>
  );
}
