// src/api/auth.js
import http from "./http";

/** PRIJAVA */
export async function loginApi(email, password) {
  const { data } = await http.post("/auth/login", { email, password });
  // očekujemo { token, id, email, ime, prezime, uloga }
  return data;
}

/** REGISTRACIJA */
export async function registerApi({ ime, prezime, email, password }) {
  return http.post("/auth/register", { ime, prezime, email, password });
}

/** ODJAVA – trenutno samo “no-op” na klijentu */
export function logoutApi() {
  // ako kasnije uvedeš backend /auth/logout, ovde pozovi http.post("/auth/logout")
  return Promise.resolve();
}
