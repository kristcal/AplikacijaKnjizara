// src/components/Navbar.jsx
import React from "react";
import { Link, NavLink, useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import { useCart } from "../context/CartContext";

export default function Navbar() {
  // konteksti mogu biti undefined u ranoj fazi mount-a, zato defaulti
  const { user, logout } = useAuth() || {};
  const { items = [] } = useCart() || {};
  const navigate = useNavigate();

  const isAdmin = user?.uloga === "ADMIN";
  const isUser = user?.uloga === "USER";

  function handleLogout() {
    try {
      logout?.();
    } finally {
      navigate("/", { replace: true });
    }
  }

  return (
    <header className="topbar">
      <div className="container topbar-inner">
        {/* Levo: brend */}
        <Link to="/" className="brand" aria-label="Početna">
          <span className="logo" />
          <strong>Knjižanje</strong>
        </Link>

        {/* Sredina: meni */}
        <nav className="menu">
          <NavLink className="navlink" to="/">
            Početna
          </NavLink>

          <NavLink className="navlink" to="/knjige">
            Knjige
          </NavLink>

          {/* Linkovi za kupca */}
          {isUser && (
            <>
              <NavLink className="navlink" to="/korpa">
                Korpa ({items.length})
              </NavLink>
              <NavLink className="navlink" to="/moje-porudzbine">
                Moje porudžbine
              </NavLink>
            </>
          )}

          {/* Linkovi za admina */}
          {isAdmin && (
            <>
              <NavLink className="navlink" to="/admin/knjige">
                Admin knjige
              </NavLink>
              <NavLink className="navlink" to="/admin/porudzbine">
                Admin porudžbine
              </NavLink>
            </>
          )}
        </nav>

        {/* Desno: auth akcije */}
        <div className="nav-actions">
          {user ? (
            <>
              <span className="muted">Zdravo, {user.ime}</span>
              <button className="btn-ghost small" onClick={handleLogout}>
                Odjava
              </button>
            </>
          ) : (
            <>
              <Link className="btn-ghost small" to="/login">
                Prijava
              </Link>
              <Link className="btn-ghost small" to="/register">
                Registracija
              </Link>
            </>
          )}
        </div>
      </div>
    </header>
  );
}
