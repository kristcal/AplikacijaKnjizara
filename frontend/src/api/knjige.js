// src/api/knjige.js
import http from "./http";

export const norm = (b) => ({
  id: b.id,
  naziv: b.naziv,
  opis: b.opis,
  cena: Number(b.cena ?? 0),
  isbn: b.isbn,
  godinaIzdavanja: b.godinaIzdanja || b.godina_izdanja,
  slika: b.imageUrl || b.image_url || "",

  // 🔽 OVO JE NOVO:
  zanrId: b.zanrId || b.zanr_id || b.zanr?.id || null,

  zanr: b.zanrNaziv || b.zanr_naziv || b.zanr?.naziv || "",

  zanrNaziv: b.zanrNaziv || b.zanr_naziv || b.zanr?.naziv || "",

  zanr_naziv: b.zanrNaziv || b.zanr_naziv || b.zanr?.naziv || "",

  autori: Array.isArray(b.autori)
    ? b.autori.map(a => ({
      id: a.id,
      ime: a.ime,
      prezime: a.prezime,
      uloga: a.uloga
    }))
    : [],

  kolicina: typeof b.kolicina === "number" ? b.kolicina : null,

  dostupnost: Array.isArray(b.dostupnost)
    ? b.dostupnost.map(x => ({
      knjizaraId: x.knjizaraId ?? x.knjizara_id,
      naziv: x.naziv ?? x.knjizara_naziv,
      lokacija: x.lokacija ?? x.knjizara_lokacija,
      kolicina: Number(x.kolicina ?? 0),
    }))
    : [],
});

const listFrom = (data) => (data?.content ?? data ?? []).map(norm);

export async function fetchKnjige() {
  const { data } = await http.get("/knjige");
  return listFrom(data);
}
export async function fetchKnjiga(id) {
  const { data } = await http.get(`/knjige/${id}`);
  return norm(data);
}

// ⬇⬇⬇ OVO PROMENI: gađaj admin rute za kreiranje/izmenu/brisanje
export async function createKnjiga(payload) {
  const body = {
    naziv: payload.naziv,
    opis: payload.opis,
    cena: payload.cena,
    isbn: payload.isbn,
    godina_izdanja: payload.godina_izdanja, // već formirano u AdminKnjige.jsx
    image_url: payload.image_url,
    // Pošalji oba ključa da backend sigurno primi
    zanrId: payload.zanrId ?? payload.zanr_id ?? null,
    zanr_id: payload.zanr_id ?? payload.zanrId ?? null,

    autori: payload.autori,           // [{id, uloga}]
    dostupnost: payload.dostupnost,  // [{knjizaraId, kolicina}]
  };
  const { data } = await http.post("/admin/knjige", body);
  return norm(data);
}

export async function updateKnjiga(id, payload) {
  const body = {
    naziv: payload.naziv,
    opis: payload.opis,
    cena: payload.cena,
    isbn: payload.isbn,
    godina_izdanja: payload.godina_izdanja,
    image_url: payload.image_url,
    zanrNaziv: payload.zanrNaziv ?? payload.zanr_naziv,
    zanr_naziv: payload.zanrNaziv ?? payload.zanr_naziv,
    zanrId: payload.zanrId ?? payload.zanr_id ?? null,
    zanr_id: payload.zanr_id ?? payload.zanrId ?? null,
    autori: payload.autori,
    dostupnost: payload.dostupnost,
  };
  const { data } = await http.put(`/admin/knjige/${id}`, body);
  return norm(data);
}

export async function deleteKnjiga(id) {
  await http.delete(`/admin/knjige/${id}`);
  return true;
}
