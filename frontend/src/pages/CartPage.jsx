import React, { useState } from "react";
import { useCart } from "../context/CartContext";
import { useAuth } from "../context/AuthContext";
import { createPorudzbina } from "../api/porudzbine";

export default function CartPage(){
  const { items, remove, clear, total } = useCart();
  const { isAuth } = useAuth();
  const [busy, setBusy] = useState(false);
  const [msg, setMsg] = useState("");

  async function handleCheckout(){
    if (!items.length) return;
    if (!isAuth) { setMsg("Prijavite se da biste poručili."); return; }
    try{
      setBusy(true); setMsg("");
      const stavke = items.map(it => ({ knjigaId: it.id, kolicina: it.kolicina, cena: it.cena }));
      const res = await createPorudzbina({ stavke });
      setMsg(`Porudžbina #${res?.id || ""} sačuvana. Hvala!`);
      clear();
    }catch(e){
      setMsg(e?.response?.data?.message || e.message || "Greška pri poručivanju.");
    }finally{ setBusy(false); }
  }

  return (
    <div className="container" style={{ padding: "18px 0" }}>
      <h1>Korpa</h1>
      {!items.length && <div className="panel">Korpa je prazna.</div>}

      {items.length > 0 && (
        <div className="panel">
          {items.map(it => (
            <div key={it.id} style={{
              display:"grid", gridTemplateColumns:"64px 1fr auto auto", gap:12, alignItems:"center", marginBottom:10
            }}>
              <div className="b-cover" style={{height:64}}>
                {it.slika ? <img src={it.slika} alt="" style={{width:"100%",height:"100%",objectFit:"cover",borderRadius:10}}/> : <span/>}
              </div>
              <div>
                <div style={{fontWeight:600}}>{it.naziv}</div>
                <div className="b-sub">{it.autor}</div>
              </div>
              <div>x{it.kolicina}</div>
              <div style={{fontWeight:700}}>{(it.cena * it.kolicina).toLocaleString("sr-RS")} RSD</div>
              <button className="btn" onClick={() => remove(it.id)} style={{gridColumn:"1 / -1", justifySelf:"end"}}>Ukloni</button>
            </div>
          ))}
          <hr/>
          <div style={{ display:"flex", justifyContent:"space-between", alignItems:"center" }}>
            <strong>Ukupno:</strong>
            <strong style={{ fontSize: 20 }}>{total.toLocaleString("sr-RS")} RSD</strong>
          </div>
          <div style={{ marginTop:12, display:"flex", gap:8, justifyContent:"flex-end" }}>
            <button className="btn" onClick={clear} disabled={busy}>Isprazni</button>
            <button className="btn primary" onClick={handleCheckout} disabled={busy}>
              {busy ? "Slanje..." : "Poruči"}
            </button>
          </div>
          {!!msg && <div className="panel" style={{marginTop:10}}>{msg}</div>}
        </div>
      )}
    </div>
  );
}
