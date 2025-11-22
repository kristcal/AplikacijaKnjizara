// src/pages/Knjige.jsx
import React, { useEffect, useMemo, useState } from "react";
import { useLocation } from "react-router-dom";
import { fetchKnjige } from "../api/knjige";
import BookCard from "../components/BookCard";

export default function Knjige() {
  const location = useLocation();

  const [loading, setLoading] = useState(true);
  const [err, setErr] = useState("");
  const [knjige, setKnjige] = useState([]);

  // UI stanje
  const [q, setQ] = useState("");           // pretraga (naziv/ISBN)
  const [autorQ, setAutorQ] = useState(""); // pretraga po autoru/ulozi
  const [zanr, setZanr] = useState("SVE");  // filter žanra (po nazivu)
  const [sort, setSort] = useState("novo"); // sortiranje

  useEffect(() => {
    let mounted = true;
    (async () => {
      try {
        setErr("");
        setLoading(true);
        const list = await fetchKnjige();
        if (mounted) setKnjige(Array.isArray(list) ? list : []);
      } catch (e) {
        setErr(e.message || "Greška pri učitavanju knjiga.");
      } finally {
        if (mounted) setLoading(false);
      }
    })();
    return () => { mounted = false; };
  }, []);

  // naziv žanra (backend može vratiti različita imena polja)
  const getGenreName = (k) =>
    k.zanrNaziv || k.zanr_naziv || k.zanr || "";

  // dinamički žanrovi (po nazivu žanra)
  const sviZanrovi = useMemo(() => {
    const s = new Set();
    knjige.forEach(k => {
      const z = getGenreName(k);
      if (z) s.add(z);
    });
    return ["SVE", ...Array.from(s)];
  }, [knjige]);

  // ako je stigao ?zanr=... preselektuj ga kada su žanrovi spremni
  useEffect(() => {
    const params = new URLSearchParams(location.search);
    const z = params.get("zanr");
    if (!z) return;

    // tačan naziv žanra, case-sensitive
    if (sviZanrovi.includes(z)) {
      setZanr(z);
      return;
    }

    // fallback: probaj case-insensitive match
    const found = sviZanrovi.find(x => x.toLowerCase() === z.toLowerCase());
    if (found) setZanr(found);
  }, [location.search, sviZanrovi]);

  // filtriranje + pretraga (naziv/ISBN + autor/uloga + žanr)
  const filtrirane = useMemo(() => {
    const sQ = q.trim().toLowerCase();
    const sA = autorQ.trim().toLowerCase();

    const matchesAuthor = (book) => {
      if (!sA) return true;
      if (!Array.isArray(book.autori) || book.autori.length === 0) return false;
      return book.autori.some(a => {
        const full = `${a?.ime || ""} ${a?.prezime || ""}`.trim().toLowerCase();
        const role = (a?.uloga || "").toLowerCase();
        return full.includes(sA) || role.includes(sA);
      });
    };

    return knjige.filter(k => {
      const matchFree =
        !sQ ||
        String(k.naziv || "").toLowerCase().includes(sQ) ||
        String(k.isbn || "").toLowerCase().includes(sQ);

      const matchZanr = zanr === "SVE" || getGenreName(k) === zanr;
      const matchAutor = matchesAuthor(k);

      return matchFree && matchZanr && matchAutor;
    });
  }, [knjige, q, autorQ, zanr]);

  // sortiranje
  const prikaz = useMemo(() => {
    const arr = [...filtrirane];
    switch (sort) {
      case "cena_asc":
        arr.sort((a,b) => Number(a.cena||0) - Number(b.cena||0));
        break;
      case "cena_desc":
        arr.sort((a,b) => Number(b.cena||0) - Number(a.cena||0));
        break;
      case "naziv_asc":
        arr.sort((a,b) => String(a.naziv||"").localeCompare(String(b.naziv||"")));
        break;
      case "naziv_desc":
        arr.sort((a,b) => String(b.naziv||"").localeCompare(String(a.naziv||"")));
        break;
      default: // "novo"
        arr.sort((a,b)=> Number(b.id||0) - Number(a.id||0));
    }
    return arr;
  }, [filtrirane, sort]);

  return (
    <div style={{ padding: "28px 0" }}>
      <div className="container">
        {/* Header */}
        <div style={{ display:"flex", alignItems:"center", justifyContent:"space-between", gap:12, flexWrap:"wrap", marginBottom:16 }}>
          <div>
            <h1 style={{ margin:0 }}>Knjige</h1>
            <p className="muted" style={{ margin:"6px 0 0" }}>
              Pretraži katalog, filtriraj po žanru i autoru i dodaj u korpu.
            </p>
          </div>
          <div style={{ display:"flex", gap:8, flexWrap:"wrap" }}>
            <input
              className="input"
              placeholder="Pretraga (naziv, ISBN)"
              value={q}
              onChange={e=>setQ(e.target.value)}
              style={{ minWidth: 220 }}
            />
            <input
              className="input"
              placeholder="Autor ili uloga (npr. prevodilac)"
              value={autorQ}
              onChange={(e)=>setAutorQ(e.target.value)}
              style={{ minWidth: 220 }}
            />
            <select className="input" value={zanr} onChange={e=>setZanr(e.target.value)}>
              {sviZanrovi.map(z => <option key={z} value={z}>{z}</option>)}
            </select>
            <select className="input" value={sort} onChange={e=>setSort(e.target.value)}>
              <option value="novo">Najnovije</option>
              <option value="cena_asc">Cena: rastuće</option>
              <option value="cena_desc">Cena: opadajuće</option>
              <option value="naziv_asc">Naziv: A–Z</option>
              <option value="naziv_desc">Naziv: Z–A</option>
            </select>
          </div>
        </div>

        {/* Statusi */}
        {loading && <div className="note">Učitavam knjige…</div>}
        {err && <div className="note">{err}</div>}

        {/* Grid knjiga */}
        {!loading && !err && (
          !prikaz.length ? (
            <div className="note">Nema rezultata za zadate kriterijume.</div>
          ) : (
            <div
              style={{
                display: "grid",
                gridTemplateColumns: "repeat(auto-fill, minmax(220px, 1fr))",
                gap: 24,
              }}
            >
              {prikaz.map(b => (
                <BookCard key={b.id} book={b} />
              ))}
            </div>
          )
        )}
      </div>
    </div>
  );
}
