import React, { createContext, useContext, useMemo, useState } from "react";
import { loginApi, logoutApi } from "../api/auth";

const AuthCtx = createContext(null);

export function AuthProvider({ children }) {
  const [user, setUser] = useState(() => {
    try { return JSON.parse(localStorage.getItem("user") || "null"); }
    catch { return null; }
  });
  const [token, setToken] = useState(() => localStorage.getItem("token") || null);

  async function login(email, password) {
    const res = await loginApi(email, password);
    // očekujemo { token, id, email, ime, prezime, uloga }
    localStorage.setItem("token", res.token);
    localStorage.setItem("user", JSON.stringify({
      id: res.id, email: res.email, ime: res.ime, prezime: res.prezime, uloga: res.uloga
    }));
    setToken(res.token);
    setUser({ id: res.id, email: res.email, ime: res.ime, prezime: res.prezime, uloga: res.uloga });
  }

  function logout() {
    try { logoutApi(); } catch {}
    localStorage.removeItem("token");
    localStorage.removeItem("user");
    setToken(null);
    setUser(null);
  }

  const value = useMemo(() => ({ user, token, login, logout }), [user, token]);
  return <AuthCtx.Provider value={value}>{children}</AuthCtx.Provider>;
}

export function useAuth() {
  return useContext(AuthCtx);
}
