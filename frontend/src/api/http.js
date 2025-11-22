// src/api/http.js
import axios from "axios";

const http = axios.create({
  baseURL: "http://localhost:8080/api",
  headers: { "Content-Type": "application/json" },
});

http.interceptors.request.use((config) => {
  const token = localStorage.getItem("token");
  if (token) config.headers.Authorization = `Bearer ${token}`;
  return config;
});

http.interceptors.response.use(
  (res) => res,
  (err) => {
    const msg =
      err?.response?.data?.message ||
      err?.response?.data ||
      err?.message ||
      "Greška na serveru";
    // po želji: ako 401 => logout
    if (err?.response?.status === 401) {
      localStorage.removeItem("token");
      localStorage.removeItem("user");
      // window.location.href = "/login";
    }
    return Promise.reject(new Error(typeof msg === "string" ? msg : "Greška"));
  }
);

export default http;
