// src/components/BookCard.jsx
import React from "react";
import { useNavigate, Link } from "react-router-dom";
import { useCart } from "../context/CartContext";
import { useAuth } from "../context/AuthContext";

const rsd = (n) => Number(n || 0).toLocaleString("sr-RS") + " RSD";

export default function BookCard({ book }) {
  const navigate = useNavigate();
  const { add } = useCart() || {};
  const { user } = useAuth() || {};

  const totalKolicina = Number(book?.kolicina ?? 0);
  const outOfStock = totalKolicina <= 0;
  const isAdmin = user?.uloga === "ADMIN";

  function handleAdd() {
    if (!user) {
      // traži login pa vrati korisnika na listu knjiga
      navigate("/login", { state: { from: "/knjige" } });
      return;
    }
    if (isAdmin || outOfStock) return;
    add(book);
  }

  return (
    <div
      style={{
        background: "#fff",
        borderRadius: 16,
        boxShadow: "0 2px 10px rgba(0,0,0,.08)",
        overflow: "hidden",
        display: "flex",
        flexDirection: "column",
        transition: "transform .15s ease",
      }}
      className="book-card"
    >
      {/* Slika + badge */}
      <Link to={`/knjige/${book.id}`} style={{ textDecoration: "none" }}>
        <div
          style={{
            height: 220,
            background: "#f5f5f7",
            position: "relative",
            display: "flex",
            alignItems: "center",
            justifyContent: "center",
            overflow: "hidden",
          }}
        >
          <img
            src={book.slika || "https://placehold.co/400x560?text=Knjiga"}
            alt={book.naziv}
            style={{ width: "100%", height: "100%", objectFit: "cover" }}
            onError={(e) => { e.currentTarget.src = "https://placehold.co/400x560?text=Nema+slike"; }}
          />
          {outOfStock && (
            <span
              style={{
                position: "absolute",
                top: 10,
                left: 10,
                background: "#b00020",
                color: "#fff",
                fontSize: 12,
                padding: "4px 8px",
                borderRadius: 999,
                letterSpacing: .3,
              }}
            >
              NEMA NA STANJU
            </span>
          )}
        </div>
      </Link>

      {/* Sadržaj */}
      <div style={{ padding: "12px 14px", display: "grid", gap: 6, flex: 1 }}>
        <Link to={`/knjige/${book.id}`} style={{ color: "inherit", textDecoration: "none" }}>
          <h3 style={{ fontSize: 16, margin: 0, fontWeight: 700 }}>{book.naziv}</h3>
        </Link>

        {book.autori?.length > 0 && (
          <div className="muted" style={{ fontSize: 13 }}>
            {book.autori.map(a => `${a.ime} ${a.prezime}`).join(", ")}
          </div>
        )}

        <div className="muted" style={{ fontSize: 12 }}>
          {book.zanr || "Bez žanra"}
        </div>

        {/* Cena + Dodaj */}
        <div
          style={{
            marginTop: 8,
            display: "flex",
            justifyContent: "space-between",
            alignItems: "center",
          }}
        >
          <strong>{rsd(book.cena)}</strong>

          {!isAdmin && (
            <button
              onClick={handleAdd}
              disabled={outOfStock}
              style={{
                background: outOfStock ? "#c7c7cc" : "#2d6cdf",
                color: "#fff",
                border: "none",
                borderRadius: 10,
                padding: "6px 10px",
                cursor: outOfStock ? "not-allowed" : "pointer",
                transition: "transform .08s ease",
              }}
              title={outOfStock ? "Proizvod trenutno nije na stanju" : "Dodaj u korpu"}
            >
              {outOfStock ? "Nema" : "➕ Dodaj"}
            </button>
          )}
        </div>
      </div>
    </div>
  );
}
