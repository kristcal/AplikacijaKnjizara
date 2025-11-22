// src/routes/CustomerRoute.jsx
import React from "react";
import { Navigate, useLocation } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

export default function CustomerRoute({ children }) {
  const { user } = useAuth();
  const loc = useLocation();

  if (!user) return <Navigate to="/login" state={{ from: loc.pathname }} replace />;
  if (user.uloga === "ADMIN") return <Navigate to="/admin/porudzbine" replace />;

  return children;
}
