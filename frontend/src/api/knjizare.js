import http from "./http";

const norm = (k) => ({
  id: k.id,
  naziv: k.naziv,
  lokacija: k.lokacija,
});

export async function fetchKnjizare() {
  const { data } = await http.get("/knjizare");
  return (Array.isArray(data) ? data : []).map(norm);
}
