// src/pages/PorudzbinaDetalj.jsx
import React, { useEffect, useMemo, useState } from "react";
import { useParams, Link, useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import { getPorudzbina } from "../api/porudzbine";
import { fetchKnjiga } from "../api/knjige";

function rsd(n) {
  return Number(n || 0).toLocaleString("sr-RS") + " RSD";
}

function StatusBadge({ value }) {
  const map = {
    KREIRANA: { bg: "#eef2ff", fg: "#3730a3", label: "Kreirana" },
    OBRADA:   { bg: "#fff7ed", fg: "#9a3412", label: "U obradi" },
    OBRADJENA:{ bg: "#ecfdf5", fg: "#065f46", label: "Obrađena" },
    OTKAZANA: { bg: "#fef2f2", fg: "#991b1b", label: "Otkazana" },
  };
  const s = map[value] || { bg: "#eef2ff", fg: "#3730a3", label: value || "—" };
  return (
    <span style={{ padding:"4px 10px", borderRadius:999, background:s.bg, color:s.fg, fontWeight:700, fontSize:12 }}>
      {s.label}
    </span>
  );
}

/** Pokuša više mogućih imena polja i mapira jednu stavku na {knjigaId, naziv, slika, kolicina, cena} */
function coerceItem(x) {
  if (!x) return null;

  const knjigaId =
    x.knjigaId ??
    x.bookId ??
    x.knjiga?.id ??
    x.book?.id ??
    null;

  const naziv =
    x.naziv ??
    x.title ??
    x.knjiga?.naziv ??
    x.book?.naziv ??
    x.knjiga?.title ??
    x.book?.title ??
    (knjigaId ? `Knjiga #${knjigaId}` : "Knjiga");

  const slika =
    x.slika ??
    x.image ??
    x.knjiga?.slika ??
    x.book?.slika ??
    x.knjiga?.imageUrl ??
    x.book?.imageUrl ??
    null;

  const kolicina =
    x.kolicina ??
    x.qty ??
    x.quantity ??
    0;

  const cena =
    x.cena ??
    x.jedinicnaCena ??
    x.unitPrice ??
    x.price ??
    0;

  return { knjigaId, naziv, slika, kolicina: Number(kolicina || 0), cena: Number(cena || 0) };
}

/** Izvadi listu stavki bez obzira kako se zovu na backendu */
function normalizeStavke(raw) {
  const list =
    raw?.stavke ??
    raw?.stavkePorudzbine ??
    raw?.stavkeDto ??
    raw?.items ??
    raw?.orderItems ??
    [];

  // ako backend vraća objekat {stavke:[...]} u drugom ključu, dodaj gore
  return Array.isArray(list) ? list.map(coerceItem).filter(Boolean) : [];
}

export default function PorudzbinaDetalj() {
  const { id } = useParams();
  const { user } = useAuth();
  const navigate = useNavigate();

  const [data, setData] = useState(null);
  const [err, setErr] = useState("");
  const [loading, setLoading] = useState(true);

  async function enrichStavke(stavke) {
    const out = [];
    for (const s of stavke) {
      if (s.slika || !s.knjigaId) { out.push(s); continue; }
      try {
        const bk = await fetchKnjiga(s.knjigaId);
        out.push({ ...s, slika: bk?.slika || bk?.imageUrl || null });
      } catch {
        out.push(s);
      }
    }
    return out;
  }

  useEffect(() => {
    let live = true;
    (async () => {
      try {
        setErr("");
        setLoading(true);

        if (!user) {
          navigate("/login", { state: { from: `/porudzbine/${id}` } });
          return;
        }

        const raw = await getPorudzbina(id);
        const stavke0 = normalizeStavke(raw);
        const stavke = await enrichStavke(stavke0);

        const norm = {
          id: raw.id,
          datum: raw.datum,
          status: raw.status || "KREIRANA",
          ukupanIznos: Number(raw.ukupanIznos ?? raw.total ?? 0),
          kupac: raw.kupac || raw.buyer || null,
          adresa: raw.adresa || raw.address || null,
          placanje: raw.placanje || raw.payment || null,
          isporuka: raw.isporuka || raw.shipping || null,
          stavke,
        };

        if (live) setData(norm);
      } catch (e) {
        const p = e?.response?.data;
        const t = typeof p === "string" ? p : p?.message || e.message || "Greška pri učitavanju porudžbine.";
        if (live) setErr(t);
      } finally {
        if (live) setLoading(false);
      }
    })();
    return () => { live = false; };
  }, [id, user, navigate]);

  const total = useMemo(() => {
    if (!data?.stavke) return 0;
    return data.stavke.reduce((s, x) => s + Number(x.cena || 0) * Number(x.kolicina || 0), 0);
  }, [data]);

  const prettyDate = useMemo(() => {
    if (!data?.datum) return "—";
    try {
      const d = new Date(data.datum);
      return isNaN(d.getTime()) ? String(data.datum) : d.toLocaleString("sr-RS");
    } catch {
      return String(data.datum);
    }
  }, [data]);

  return (
    <div className="container" style={{ padding: "28px 0", maxWidth: 980 }}>
      <div style={{ display:"flex", justifyContent:"space-between", alignItems:"center", gap:12, marginBottom:16 }}>
        <Link className="btn" to="/knjige">← Nazad</Link>
        <div className="muted" style={{ fontSize: 13 }}>
          Porudžbina <strong>#{id}</strong>
        </div>
      </div>

      <h1 style={{ margin: "0 0 10px" }}>Detalji porudžbine</h1>

      {loading && <div className="note">Učitavam…</div>}
      {err && <div className="panel error">{err}</div>}

      {!loading && !err && data && (
        <div className="panel" style={{ display: "grid", gap: 16 }}>
          <div style={{ display:"flex", alignItems:"center", justifyContent:"space-between", gap:12, flexWrap:"wrap" }}>
            <div className="muted">Datum: {prettyDate}</div>
            <StatusBadge value={data.status} />
          </div>

          <div style={{ fontWeight: 700 }}></div>  

          {!data.stavke?.length ? (
            <div className="note"></div> //Nema stavki o ovoj porudzbini
          ) : (
            <div style={{ display:"grid", gap:10 }}>
              {data.stavke.map((s, i) => {
                const subtotal = Number(s.cena || 0) * Number(s.kolicina || 0);
                return (
                  <div
                    key={i}
                    className="subtle panel"
                    style={{
                      padding: 10,
                      display: "grid",
                      gridTemplateColumns: "64px 1fr auto auto",
                      gap: 12,
                      alignItems: "center",
                    }}
                  >
                    <div style={{ width:64, height:64, borderRadius:12, overflow:"hidden", background:"#f3f4f6", display:"flex", alignItems:"center", justifyContent:"center" }}>
                      {s.knjigaId ? (
                        <Link to={`/knjige/${s.knjigaId}`}>
                          <img
                            src={s.slika || "https://placehold.co/64x64?text=?"}
                            alt=""
                            style={{ width:64, height:64, objectFit:"cover", display:"block" }}
                            onError={(e)=>{ e.currentTarget.src="https://placehold.co/64x64?text=?"; }}
                          />
                        </Link>
                      ) : (
                        <img
                          src={s.slika || "https://placehold.co/64x64?text=?"}
                          alt=""
                          style={{ width:64, height:64, objectFit:"cover", display:"block" }}
                          onError={(e)=>{ e.currentTarget.src="https://placehold.co/64x64?text=?"; }}
                        />
                      )}
                    </div>

                    <div>
                      {s.knjigaId ? (
                        <Link to={`/knjige/${s.knjigaId}`} style={{ fontWeight: 700 }}>
                          {s.naziv || "Knjiga"}
                        </Link>
                      ) : (
                        <div style={{ fontWeight: 700 }}>{s.naziv || "Knjiga"}</div>
                      )}
                      <div className="muted" style={{ fontSize: 13 }}>Količina: x{s.kolicina || 0}</div>
                    </div>

                    <div className="muted">{rsd(s.cena)}</div>
                    <div style={{ fontWeight: 700 }}>{rsd(subtotal)}</div>
                  </div>
                );
              })}
            </div>
          )}

          <div className="subtle panel" style={{ padding:12, display:"flex", justifyContent:"flex-end", gap:16 }}>
            <div style={{ fontSize: 18 }}>
              Ukupno: <strong>{rsd((total || data.ukupanIznos) ?? 0)}</strong>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
