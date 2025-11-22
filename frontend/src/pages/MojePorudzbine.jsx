// src/pages/MojePorudzbine.jsx
import React, { useEffect, useState } from "react";
import { useAuth } from "../context/AuthContext";
import { getMojePorudzbine } from "../api/porudzbine";
import { Link, useNavigate } from "react-router-dom";

export default function MojePorudzbine() {
  const { user } = useAuth();
  const navigate = useNavigate();
  const [list, setList] = useState([]);
  const [err, setErr] = useState("");
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (!user) {
      navigate("/login", { state: { from: "/moje-porudzbine" } });
      return;
    }
    let live = true;
    (async () => {
      try {
        setErr("");
        setLoading(true);
        const data = await getMojePorudzbine(user.id);
        if (live) setList(data);
      } catch (e) {
        const p = e?.response?.data;
        const t =
          typeof p === "string" ? p : p?.message || e.message || "Greška pri učitavanju.";
        setErr(t);
      } finally {
        if (live) setLoading(false);
      }
    })();
    return () => {
      live = false;
    };
  }, [user, navigate]);

  const prazno = !loading && !err && (!Array.isArray(list) || list.length === 0);

  return (
    <div className="container" style={{ padding: "28px 0", maxWidth: 900 }}>
      <h1>Moje porudžbine</h1>
      <p className="muted" style={{ marginTop: 4 }}>
        Pregled svih porudžbina povezanih sa tvojim nalogom.
      </p>

      {loading && <div className="note" style={{ marginTop: 12 }}>Učitavam…</div>}
      {err && <div className="panel error" style={{ marginTop: 12 }}>{err}</div>}
      {prazno && <div className="panel" style={{ marginTop: 12 }}>Još uvek nema porudžbina.</div>}

      {!loading && !err && !prazno && (
        <div style={{ display: "grid", gap: 12, marginTop: 12 }}>
          {list.map((p) => {
            const id = p.id ?? p.porudzbinaId ?? p.orderId;
            const datum = p.datum ?? p.createdAt ?? p.vreme ?? null;
            const iznos = Number(p.ukupanIznos ?? p.total ?? 0);
            const stavke = Array.isArray(p.stavke) ? p.stavke : [];

            return (
              <div key={id} className="panel" style={{ display: "grid", gap: 8 }}>
                {/* 🔹 Naslov sa dugmetom „Detalji“ */}
                <div
                  style={{
                    display: "flex",
                    justifyContent: "space-between",
                    alignItems: "center",
                    gap: 12,
                    flexWrap: "wrap",
                  }}
                >
                  <div>
                    <div style={{ fontWeight: 700 }}>Porudžbina #{id}</div>
                    <div className="muted" style={{ fontSize: 14 }}>
                      {datum ? new Date(datum).toLocaleString("sr-RS") : "—"}
                    </div>
                  </div>

                  <div style={{ display: "flex", alignItems: "center", gap: 10 }}>
                    {/* 👉 dugme ka detaljima */}
                    <Link to={`/porudzbine/${id}`} className="btn-ghost small">
                      Detalji
                    </Link>

                    <div style={{ fontSize: 18 }}>
                      Ukupno:{" "}
                      <strong>{iznos.toLocaleString("sr-RS")} RSD</strong>
                    </div>
                  </div>
                </div>

                {/* 🔹 Stavke ispod */}
                {!!stavke.length && (
                  <div style={{ display: "grid", gap: 8 }}>
                    {stavke.map((s, i) => {
                      const naziv =
                        s.naziv ??
                        s.knjiga?.naziv ??
                        s.knjiga_naziv ??
                        "Nepoznata knjiga";
                      const kolicina = s.kolicina ?? 1;
                      const cena = Number(s.cena ?? 0);
                      return (
                        <div
                          key={i}
                          style={{
                            display: "grid",
                            gridTemplateColumns: "1fr auto auto",
                            gap: 10,
                            alignItems: "center",
                          }}
                        >
                          <div style={{ fontWeight: 600 }}>{naziv}</div>
                          <div className="muted">x{kolicina}</div>
                          <div style={{ fontWeight: 700 }}>
                            {(cena * kolicina).toLocaleString("sr-RS")} RSD
                          </div>
                        </div>
                      );
                    })}
                  </div>
                )}
              </div>
            );
          })}
        </div>
      )}
    </div>
  );
}
