import React, { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import { registerApi } from "../api/auth";

export default function Register() {
  const [ime, setIme] = useState("");
  const [prezime, setPrezime] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPass] = useState("");
  const [confirm, setConfirm] = useState("");

  const [msg, setMsg] = useState("");
  const [busy, setBusy] = useState(false);
  const navigate = useNavigate();

  async function handleSubmit(e) {
    e.preventDefault();
    setMsg("");

    if (password !== confirm) {
      setMsg("Lozinke se ne poklapaju.");
      return;
    }

    try {
      setBusy(true);
      const { status, data } = await registerApi({ ime, prezime, email, password });
      if (status === 201) {
        setMsg("Uspešna registracija. Preusmeravam na prijavu…");
        setTimeout(() => navigate("/login"), 900);
      } else {
        const text = typeof data === "string" ? data : (data?.message || "Greška pri registraciji.");
        setMsg(text);
      }
    } catch (e) {
      const payload = e?.response?.data;
      const text =
        typeof payload === "string"
          ? payload
          : payload?.message || e.message || "Greška servera.";
      setMsg(text);
    } finally {
      setBusy(false);
    }
  }

  return (
    <div className="container" style={{ padding: "24px 0", maxWidth: 520 }}>
      <h1>Kreiraj nalog</h1>

      <form className="panel" onSubmit={handleSubmit} style={{ display: "grid", gap: 12 }}>
        <div className="grid" style={{ gridTemplateColumns: "1fr 1fr", gap: 10 }}>
          <input
            className="input"
            placeholder="Ime"
            value={ime}
            onChange={(e) => setIme(e.target.value)}
            required
          />
          <input
            className="input"
            placeholder="Prezime"
            value={prezime}
            onChange={(e) => setPrezime(e.target.value)}
            required
          />
        </div>

        <input
          className="input"
          placeholder="Email"
          type="email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          required
        />
        <input
          className="input"
          placeholder="Lozinka"
          type="password"
          value={password}
          onChange={(e) => setPass(e.target.value)}
          required
          minLength={4}
        />
        <input
          className="input"
          placeholder="Potvrdi lozinku"
          type="password"
          value={confirm}
          onChange={(e) => setConfirm(e.target.value)}
          required
          minLength={4}
        />

        {msg && <div className="panel">{msg}</div>}

        <button className="btn primary" type="submit" disabled={busy}>
          {busy ? "Slanje..." : "Registruj se"}
        </button>
      </form>

      <p className="muted" style={{ marginTop: 10 }}>
        Već imaš nalog? <Link to="/login">Prijavi se</Link>
      </p>
    </div>
  );
}
