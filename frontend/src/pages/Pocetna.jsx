// src/pages/Pocetna.jsx
import React, { useEffect, useMemo, useState } from "react";
import { useNavigate } from "react-router-dom";
import "../css/Pocetna.css";
import { fetchKnjige } from "../api/knjige";
import BookCard from "../components/BookCard";

// mali helper – backend nam ponekad šalje različita imena polja za žanr
const getGenreName = (k) => k.zanrNaziv || k.zanr_naziv || k.zanr || "";

// fallback ikone za žanrove (možeš dopuniti)
const GENRE_ICON = {
  Klasici: "📚",
  Krimi: "🕵️‍♂️",
  "Istorijski roman": "🏛️",
  Romantika: "💘",
  Fantastika: "🧙",
  "Dečje": "🧸",
  Publicistika: "📰",
};

export default function Pocetna() {
  const navigate = useNavigate();

  const [loading, setLoading] = useState(true);
  const [err, setErr] = useState("");
  const [knjige, setKnjige] = useState([]);

  useEffect(() => {
    let live = true;
    (async () => {
      try {
        setErr("");
        setLoading(true);
        const list = await fetchKnjige();
        if (live) setKnjige(Array.isArray(list) ? list : []);
      } catch (e) {
        if (live) setErr(e.message || "Greška pri učitavanju knjiga.");
      } finally {
        if (live) setLoading(false);
      }
    })();
    return () => { live = false; };
  }, []);

  // dostupni žanrovi iz baze (unique, zadržavamo do 6 da početna ne bude preduga)
  const genres = useMemo(() => {
    const s = new Set();
    knjige.forEach(k => { const z = getGenreName(k); if (z) s.add(z); });
    return Array.from(s).slice(0, 6);
  }, [knjige]);

  // po žanru uzimamo do 4 knjige
  const byGenre = useMemo(() => {
    const map = {};
    genres.forEach(g => {
      map[g] = knjige.filter(k => getGenreName(k) === g).slice(0, 4);
    });
    return map;
  }, [genres, knjige]);

  // “Najnovije” – jednostavno po ID-u opadajuće
  const najnovije = useMemo(() => {
    const arr = [...knjige];
    arr.sort((a, b) => Number(b.id || 0) - Number(a.id || 0));
    return arr.slice(0, 8);
  }, [knjige]);

  const gotoGenre = (g) => navigate(`/knjige?zanr=${encodeURIComponent(g)}`);
  

  return (
    <div className="home">

      {/* HERO */}
      <section
        className="hero hero--rich"
        style={{
          background:
            "radial-gradient(1200px 400px at -10% 0%, #fff 0%, #fafafa 40%, #f0f2f5 100%)",
        }}
      >
        <div className="container hero-grid">
          <div className="hero-left">
            <h1 className="hero-title">
              Miris papira, topla kafa i dobra knjiga.
              <br />Dobrodošao nazad!
            </h1>
            <p className="hero-sub">
              Pregledaj žanrove iz kataloga i pronađi sledeće omiljeno štivo.
            </p>
            <div className="hero-actions">
              <button className="btn-primary" onClick={() => navigate("/knjige")}>
                Pretraži sve knjige
              </button>
              <a className="btn-ghost" href="/" onClick={(e)=>{e.preventDefault(); window.scrollTo({top: window.innerHeight, behavior:"smooth"});}}>
                Pogledaj žanrove
              </a>
            </div>
            <p className="quote">“Čitanje je za um ono što je vežba za telo.” — Džozef Adison</p>
          </div>

          
        </div>
      </section>

      {/* NAJNOVIJE */}
      <section className="section">
        <div className="container">
          <div className="head">
            <h2>Najnovije</h2>
            <a href="/" onClick={(e)=>{e.preventDefault(); navigate("/knjige");}}>
              Prikaži sve
            </a>
          </div>

          {loading && <div className="note">Učitavam…</div>}
          {err && <div className="panel error">{err}</div>}

          {!loading && !err && (
            najnovije.length ? (
              <div className="grid cards">
                {najnovije.map((b) => <BookCard key={b.id} book={b} />)}
              </div>
            ) : (
              <div className="note">Još uvek nema knjiga.</div>
            )
          )}
        </div>
      </section>

      {/* PO ŽANROVIMA – svaka sekcija ima svoj “Prikaži sve” */}
      {genres.map((g) => (
        <section className="section" key={g}>
          <div className="container">
            <div className="head">
              <h2>
                {GENRE_ICON[g] || "📘"} {g}
              </h2>
              <a href="/" onClick={(e)=>{e.preventDefault(); gotoGenre(g);}}>
                Prikaži sve
              </a>
            </div>

            {!byGenre[g]?.length ? (
              <div className="note">Nema naslova u ovom žanru.</div>
            ) : (
              <div className="grid cards">
                {byGenre[g].map((b) => <BookCard key={b.id} book={b} />)}
              </div>
            )}
          </div>
        </section>
      ))}

      {/* UTISCI – zadržavamo, samo malo “dišemo” */}
      <section className="section">
        <div className="container">
          <div className="head">
            <h2>Šta kažu čitaoci</h2>
          </div>
          <div className="grid testimonials">
            <article className="t-card">
              <p>“Brza dostava i odličan izbor klasika. Vratila mi se navika čitanja!”</p>
              <div className="t-user"><span className="avatar">AM</span> Ana M.</div>
            </article>
            <article className="t-card">
              <p>“Sekcija ‘ispod 1000’ je pun pogodak. Super pokloni za društvo.”</p>
              <div className="t-user"><span className="avatar">NP</span> Nikola P.</div>
            </article>
            <article className="t-card">
              <p>“Preporuke po žanru su mi skroz legle. Odmah sam našla ‘1984’.”</p>
              <div className="t-user"><span className="avatar">JV</span> Jelena V.</div>
            </article>
          </div>
        </div>
      </section>
    </div>
  );
}
