/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/*package njt.mavenproject2.entity.impl;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import njt.mavenproject2.entity.MyEntity;
*/
/**
 *
 * @author Korisnik
 */
/*@Entity
@Table(name = "porudzbina")
public class Porudzbina implements MyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime datum;
    private Double ukupanIznos;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "korisnik_id", nullable = false)
    private Korisnik korisnik;

    @OneToMany(mappedBy = "porudzbina", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StavkaPorudzbine> stavke = new ArrayList<>();

    public Porudzbina() {
    }

    public Porudzbina(Long id, LocalDateTime datum, Double ukupanIznos, Korisnik korisnik) {
        this.id = id;
        this.datum = datum;
        this.ukupanIznos = ukupanIznos;
        this.korisnik = korisnik;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDatum() {
        return datum;
    }

    public void setDatum(LocalDateTime datum) {
        this.datum = datum;
    }

    public Double getUkupanIznos() {
        return ukupanIznos;
    }

    public void setUkupanIznos(Double ukupanIznos) {
        this.ukupanIznos = ukupanIznos;
    }

    public Korisnik getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(Korisnik korisnik) {
        this.korisnik = korisnik;
    }

    public List<StavkaPorudzbine> getStavke() {
        return stavke;
    }

    public void setStavke(List<StavkaPorudzbine> stavke) {
        this.stavke = stavke;
    }
}*/


/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package njt.mavenproject2.entity.impl;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "porudzbina")
public class Porudzbina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime datum;

    @Column(nullable = false)
    private Double ukupanIznos;

    // 🔹 NOVO POLJE STATUS
    @Column(nullable = false, length = 50)
    private String status = "KREIRANA";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "korisnik_id", nullable = false)
    private Korisnik korisnik;

    @OneToMany(mappedBy = "porudzbina", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StavkaPorudzbine> stavke = new ArrayList<>();

    // ===== KONSTRUKTORI =====
    public Porudzbina() {}

    public Porudzbina(Long id, LocalDateTime datum, Double ukupanIznos, Korisnik korisnik) {
        this.id = id;
        this.datum = datum;
        this.ukupanIznos = ukupanIznos;
        this.korisnik = korisnik;
        this.status = "KREIRANA";
    }

    // ===== GETTERI & SETTERI =====
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDatum() {
        return datum;
    }

    public void setDatum(LocalDateTime datum) {
        this.datum = datum;
    }

    public Double getUkupanIznos() {
        return ukupanIznos;
    }

    public void setUkupanIznos(Double ukupanIznos) {
        this.ukupanIznos = ukupanIznos;
    }

    public Korisnik getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(Korisnik korisnik) {
        this.korisnik = korisnik;
    }

    public List<StavkaPorudzbine> getStavke() {
        return stavke;
    }

    public void setStavke(List<StavkaPorudzbine> stavke) {
        this.stavke = stavke;
    }

    // 🔹 GETTER & SETTER ZA STATUS
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}


