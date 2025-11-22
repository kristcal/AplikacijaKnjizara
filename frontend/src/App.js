// src/App.js
import React from "react";
import { BrowserRouter, Routes, Route } from "react-router-dom";

import { AuthProvider } from "./context/AuthContext";
import { CartProvider } from "./context/CartContext";

import Navbar from "./components/Navbar";
import Footer from "./components/Footer";

import Pocetna from "./pages/Pocetna";
import Knjige from "./pages/Knjige";
import BookDetails from "./pages/BookDetails";

import Korpa from "./pages/Korpa";
import MojePorudzbine from "./pages/MojePorudzbine";
import PorudzbinaDetalj from "./pages/PorudzbinaDetalj";

import AdminKnjige from "./pages/AdminKnjige";
import AdminPorudzbine from "./pages/AdminPorudzbine";

import AdminRoute from "./routes/AdminRoute";
import CustomerRoute from "./routes/CustomerRoute";
import Login from "./pages/Login";
import Register from "./pages/Register";
// Globalni stilovi
import "./App.css";
import "./css/Pocetna.css";

function NotFound() {
  return (
    <div className="container" style={{ padding: "28px 0" }}>
      <h2>Stranica nije pronađena</h2>
      <p>Vrati se na <a href="/">početnu</a>.</p>
    </div>
  );
}

export default function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <CartProvider>
          <div className="app-shell">
            <Navbar />
            <main className="app-main">
              <Routes>
                {/* Javno */}
                <Route path="/" element={<Pocetna />} />
                <Route path="/knjige" element={<Knjige />} />
                <Route path="/knjige/:id" element={<BookDetails />} />
                <Route path="/login" element={<Login />} />
                <Route path="/register" element={<Register />} />

                {/* Korisničke rute (admin ne vidi/koristi) */}
                <Route
                  path="/korpa"
                  element={
                    <CustomerRoute>
                      <Korpa />
                    </CustomerRoute>
                  }
                />
                <Route
                  path="/moje-porudzbine"
                  element={
                    <CustomerRoute>
                      <MojePorudzbine />
                    </CustomerRoute>
                  }
                />
                <Route
                  path="/porudzbine/:id"
                  element={
                    <CustomerRoute>
                      <PorudzbinaDetalj />
                    </CustomerRoute>
                  }
                />

                {/* Admin rute */}
                <Route
                  path="/admin/knjige"
                  element={
                    <AdminRoute>
                      <AdminKnjige />
                    </AdminRoute>
                  }
                />
                <Route
                  path="/admin/porudzbine"
                  element={
                    <AdminRoute>
                      <AdminPorudzbine />
                    </AdminRoute>
                  }
                />

                {/* 404 */}
                <Route path="*" element={<NotFound />} />
              </Routes>
            </main>
            <Footer />
          </div>
        </CartProvider>
      </AuthProvider>
    </BrowserRouter>
  );
}

// Napomena: Uveri se da postoje fajlovi:
// - ./pages/PorudzbinaDetalj.jsx (export default)
// - ./routes/CustomerRoute.jsx i ./routes/AdminRoute.jsx (sa Navigate guard logikom)
// - ./pages/Login.jsx i ./pages/Register.jsx (export default)
