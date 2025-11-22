import React from "react";
import { Navigate, useLocation } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

/**
 * Dozvoljava pristup samo ulogovanom korisniku sa ulogom ADMIN.
 * Ako nije ulogovan -> vodi na /login (čuva "from" rutu)
 * Ako je ulogovan ali nije ADMIN -> vodi na /
 */
export default function AdminRoute({ children }) {
  const { isAuth, user } = useAuth();
  const loc = useLocation();

  if (!isAuth) {
    return <Navigate to="/login" replace state={{ from: loc.pathname }} />;
  }
  if (user?.uloga !== "ADMIN") {
    return <Navigate to="/" replace />;
  }
  return children;
}
