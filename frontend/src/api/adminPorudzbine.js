// src/api/adminPorudzbine.js
import http from "./http";

const normStavka = (s) => ({
  kolicina: Number(s.kolicina ?? 0),
  cena: Number(s.cena ?? 0),
  naziv: s.naziv || s.knjiga?.naziv || "",
});

const normPorudzbina = (p) => {
  const stavke = Array.isArray(p.stavke) ? p.stavke.map(normStavka) : [];
  const ukupan =
    p.ukupanIznos ?? p.ukupan_iznos ??
    stavke.reduce((z, x) => z + x.cena * x.kolicina, 0);

  return {
    id: p.id,
    datum: p.datum,
    status: p.status || "KREIRANA",
    korisnikId: p.korisnikId ?? p.korisnik_id ?? p.korisnik?.id ?? null,
    korisnikEmail: p.korisnikEmail ?? p.korisnik_email ?? p.korisnik?.email ?? null,
    ukupanIznos: Number(ukupan || 0),
    stavke,
  };
};

export async function adminListPorudzbine() {
  const { data } = await http.get("/admin/porudzbine");
  const list = Array.isArray(data) ? data : data?.content || [];
  return list.map(normPorudzbina);
}

export async function adminGetPorudzbina(id) {
  const { data } = await http.get(`/admin/porudzbine/${id}`);
  return normPorudzbina(data);
}

export async function adminSetStatus(id, status) {
  const { data } = await http.put(`/admin/porudzbine/${id}/status`, status, {
    headers: { "Content-Type": "text/plain" },
  });
  return normPorudzbina(data);
}

export async function adminDeletePorudzbina(id) {
  await http.delete(`/admin/porudzbine/${id}`);
}
