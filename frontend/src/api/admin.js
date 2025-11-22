// src/api/admin.js
import http from "./http";

/** Sve porudžbine (admin) */
export async function fetchSvePorudzbine() {
  const { data } = await http.get("/admin/porudzbine");
  return Array.isArray(data) ? data : [];
}

/** Jedna porudžbina (admin) */
export async function fetchPorudzbinaAdmin(id) {
  const { data } = await http.get(`/admin/porudzbine/${id}`);
  return data;
}

/** Promena statusa (admin)
 *  Backend prima čist JSON string (npr. "ISPORUCENA"),
 *  zato ovde šaljemo JSON.stringify(status).
 */
export async function promeniStatus(id, status) {
  const body = JSON.stringify(status); // npr. "\"KREIRANA\""
  const { data } = await http.put(`/admin/porudzbine/${id}/status`, body, {
    headers: { "Content-Type": "application/json" },
  });
  return data;
}

/** Brisanje porudžbine (admin) */
export async function obrisiPorudzbinu(id) {
  await http.delete(`/admin/porudzbine/${id}`);
}
