import http from "./http";

// normalizacija žanra
export const norm = (z) => ({
  id: z.id,
  naziv: z.naziv,
});

// listu pretvaramo u jednostavan niz
const listFrom = (data) => (data?.content ?? data ?? []).map(norm);

// vrati sve žanrove
export async function fetchZanrovi() {
  const { data } = await http.get("/zanrovi");
  return listFrom(data);
}

// pronađi jedan žanr
export async function fetchZanr(id) {
  const { data } = await http.get(`/zanrovi/${id}`);
  return norm(data);
}

// (opciono) za admin deo
export async function createZanr(payload) {
  const { data } = await http.post("/zanrovi", payload);
  return norm(data);
}

export async function updateZanr(id, payload) {
  const { data } = await http.put(`/zanrovi/${id}`, payload);
  return norm(data);
}

export async function deleteZanr(id) {
  await http.delete(`/zanrovi/${id}`);
}
