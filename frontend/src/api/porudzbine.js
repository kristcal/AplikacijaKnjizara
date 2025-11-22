// src/api/porudzbine.js
import http from "./http";

/* ---------- Helpers (normalizacija) ---------- */

const mapAutori = (knjiga) =>
  Array.isArray(knjiga?.autori)
    ? knjiga.autori
        .map(a =>
          `${a.autor?.ime ?? a.ime ?? ""} ${a.autor?.prezime ?? a.prezime ?? ""}`.trim()
        )
        .filter(Boolean)
    : [];

const normStavka = (s) => {
  const knjiga = s.knjiga ?? null;
  return {
    knjigaId: s.knjiga_id ?? s.knjigaId ?? knjiga?.id ?? null,
    naziv: s.naziv ?? s.knjiga_naziv ?? knjiga?.naziv ?? "Knjiga",
    cena: Number(s.cena ?? 0),
    kolicina: Number(s.kolicina ?? 1),
    slika: knjiga?.imageUrl ?? knjiga?.image_url ?? null,
    autori: mapAutori(knjiga),
  };
};

export const normPorudzbina = (o) => {
  const stavkeRaw = Array.isArray(o.stavke) ? o.stavke : [];
  const stavke = stavkeRaw.map(normStavka);

  const ukupan =
    o.ukupanIznos ??
    o.ukupan_iznos ??
    o.total ??
    stavke.reduce((sum, x) => sum + Number(x.cena || 0) * Number(x.kolicina || 0), 0);

  return {
    id: o.id ?? o.porudzbinaId ?? o.orderId,
    datum: o.datum ?? o.createdAt ?? o.vreme ?? null,
    status: o.status ?? o.stanje ?? "KREIRANA",
    stavke,
    ukupanIznos: Number(ukupan || 0),
  };
};

/* ---------- API pozivi ---------- */

/**
 * POST /api/porudzbine
 * payload primer:
 * {
 *   korisnikId: 3,
 *   stavke: [{ knjiga_id: 10, kolicina: 2, cena: 999 }]
 * }
 */
export async function kreirajPorudzbinu({ korisnikId, stavke }) {
  const payload = {
    korisnik_id: korisnikId,   // backend očekuje snake_case
    stavke,                    // [{knjiga_id, kolicina, cena}]
  };
  const { data } = await http.post("/porudzbine", payload);
  return normPorudzbina(data);
}

/**
 * GET /api/porudzbine/korisnik/{korisnikId}
 * Vraća listu porudžbina za korisnika
 */
export async function getMojePorudzbine(korisnikId) {
  const { data } = await http.get(`/porudzbine/korisnik/${korisnikId}`);
  const list = Array.isArray(data) ? data : data?.content || [];
  return list.map(normPorudzbina);
}

// Ostavio sam i ovaj alias ako ga negde koristiš
export async function fetchMojePorudzbine(korisnikId) {
  return getMojePorudzbine(korisnikId);
}

/**
 * GET /api/porudzbine/{id}
 * Detalj jedne porudžbine (za stranicu /porudzbine/:id)
 */
export async function getPorudzbina(id) {
  const { data } = await http.get(`/porudzbine/${id}`);
  return normPorudzbina(data);
}
