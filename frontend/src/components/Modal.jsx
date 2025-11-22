// src/components/Modal.jsx
import React, { useEffect } from "react";

export default function Modal({
  open,
  onClose,
  title = "Poruka",
  children,
  autoCloseMs,          // npr. 1500 (opciono)
}) {
  useEffect(() => {
    if (!open || !autoCloseMs) return;
    const t = setTimeout(() => onClose?.(), autoCloseMs);
    return () => clearTimeout(t);
  }, [open, autoCloseMs, onClose]);

  if (!open) return null;

  function handleBackdrop(e) {
    if (e.target === e.currentTarget) onClose?.();
  }

  return (
    <div className="modal-backdrop" onClick={handleBackdrop}>
      <div className="modal-card">
        <div className="modal-head">
          <h3>{title}</h3>
          <button className="btn-ghost small" onClick={onClose} aria-label="Zatvori">✕</button>
        </div>
        <div className="modal-body">{children}</div>
      </div>
    </div>
  );
}
