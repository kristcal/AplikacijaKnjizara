import React, { useState } from "react";
import { useNavigate, useLocation, Link } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

export default function Login() {
  const [email, setEmail] = useState("");
  const [password, setPass] = useState("");
  const [msg, setMsg] = useState("");
  const [busy, setBusy] = useState(false);

  const { login } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();

  async function handleSubmit(e) {
    e.preventDefault();
    setMsg("");
    setBusy(true);
    try {
      await login(email, password); // poziva loginApi i puni localStorage
      const to = location.state?.from || "/";
      navigate(to, { replace: true });
    } catch (e) {
      const payload = e?.response?.data;
      const text =
        typeof payload === "string"
          ? payload
          : payload?.message || e.message || "Neuspešna prijava.";
      setMsg(text);
    } finally {
      setBusy(false);
    }
  }

  return (
    <div className="container" style={{ padding: "24px 0", maxWidth: 520 }}>
      <h1>Prijava</h1>

      <form className="panel" onSubmit={handleSubmit} style={{ display: "grid", gap: 12 }}>
        <input
          className="input"
          placeholder="Email"
          type="email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          required
          autoFocus
        />
        <input
          className="input"
          placeholder="Lozinka"
          type="password"
          value={password}
          onChange={(e) => setPass(e.target.value)}
          required
        />

        {msg && <div className="panel error">{msg}</div>}

        <button className="btn primary" type="submit" disabled={busy}>
          {busy ? "Prijava..." : "Prijavi se"}
        </button>
      </form>

      <p className="muted" style={{ marginTop: 10 }}>
        Nemaš nalog? <Link to="/register">Registruj se</Link>
      </p>
    </div>
  );
}
