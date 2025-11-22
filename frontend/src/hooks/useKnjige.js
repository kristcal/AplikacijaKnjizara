import { useCallback, useEffect, useRef, useState } from "react";
import { fetchKnjige } from "../api/knjige";

/**
 * Stabilna verzija hook-a:
 * - Ne koristi {} kao default u potpisu (to je novi objekat svako renderovanje).
 * - load() nema vanjske zavisnosti -> stabilan je.
 * - Efekat poziva load() SAMO jednom (gasi StrictMode dupli poziv).
 */
export default function useKnjige(initialParams) {
  const [knjige, setKnjige] = useState([]);
  const [kLoading, setLoading] = useState(true);
  const [kError, setError] = useState("");

  const didInit = useRef(false); // sprečava duplo učitavanje u dev/StrictMode

  const load = useCallback(async (params = {}) => {
    try {
      setLoading(true);
      setError("");
      const list = await fetchKnjige(params);
      setKnjige(Array.isArray(list) ? list : []);
    } catch (e) {
      setError(e?.response?.data?.message || e.message || "Greška pri učitavanju knjiga.");
    } finally {
      setLoading(false);
    }
  }, []); // ⬅️ nema zavisnosti, pa je stabilna

  useEffect(() => {
    if (didInit.current) return;  // ⬅️ onemogući dupli poziv u dev-u
    didInit.current = true;
    // ako nisi prosledila ništa, koristi prazan objekat JEDNOM
    load(initialParams || {});
  }, [load, initialParams]);

  return { knjige, kLoading, kError, reload: load, setKnjige };
}
