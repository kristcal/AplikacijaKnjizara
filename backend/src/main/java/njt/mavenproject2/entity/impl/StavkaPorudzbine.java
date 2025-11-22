/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package njt.mavenproject2.entity.impl;

import jakarta.persistence.*;
import njt.mavenproject2.entity.MyEntity;

/**
 *
 * @author Korisnik
 */
@Entity
@Table(name = "stavka_porudzbine",
        uniqueConstraints = @UniqueConstraint(columnNames = {"porudzbina_id", "rb"}))
public class StavkaPorudzbine implements MyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer rb;
    private Integer kolicina;
    private Double cenaK;
    private Double ukupanIznosStavke;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "porudzbina_id", nullable = false)
    private Porudzbina porudzbina;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "knjiga_id", nullable = false)
    private Knjiga knjiga;

    public StavkaPorudzbine() {
    }

    public StavkaPorudzbine(Integer rb, Integer kolicina, Double cenaK,
            Porudzbina porudzbina, Knjiga knjiga) {
        this.rb = rb;
        this.kolicina = kolicina;
        this.cenaK = cenaK;
        this.ukupanIznosStavke = (cenaK != null && kolicina != null) ? cenaK * kolicina : 0d;
        this.porudzbina = porudzbina;
        this.knjiga = knjiga;
    }

    @PrePersist
    @PreUpdate
    private void izracunajUkupanIznos() {
        this.ukupanIznosStavke = (cenaK != null && kolicina != null) ? cenaK * kolicina : 0d;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRb() {
        return rb;
    }

    public void setRb(Integer rb) {
        this.rb = rb;
    }

    public Integer getKolicina() {
        return kolicina;
    }

    public void setKolicina(Integer kolicina) {
        this.kolicina = kolicina;
    }

    public Double getCenaK() {
        return cenaK;
    }

    public void setCenaK(Double cenaK) {
        this.cenaK = cenaK;
    }

    public Double getUkupanIznosStavke() {
        return ukupanIznosStavke;
    }

    public void setUkupanIznosStavke(Double ukupanIznosStavke) {
        this.ukupanIznosStavke = ukupanIznosStavke;
    }

    public Porudzbina getPorudzbina() {
        return porudzbina;
    }

    public void setPorudzbina(Porudzbina porudzbina) {
        this.porudzbina = porudzbina;
    }

    public Knjiga getKnjiga() {
        return knjiga;
    }

    public void setKnjiga(Knjiga knjiga) {
        this.knjiga = knjiga;
    }
}
