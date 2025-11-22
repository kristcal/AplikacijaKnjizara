import http from "./http";

const norm = (a) => ({
  id: a.id,
  ime: a.ime,
  prezime: a.prezime,
});

export async function fetchAutori() {
  const { data } = await http.get("/autori");
  return (Array.isArray(data) ? data : []).map(norm);
}
