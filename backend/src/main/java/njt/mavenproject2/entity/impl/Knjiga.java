/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package njt.mavenproject2.entity.impl;

import jakarta.persistence.CascadeType;
import jakarta.persistence.*;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import njt.mavenproject2.entity.MyEntity;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 *
 * @author Korisnik
 */
@Entity
@Table(name = "knjiga")
public class Knjiga implements MyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String naziv;
    private String opis;
    private Double cena;
    private String isbn;
    private LocalDate godinaIzdanja;
    private String imageUrl;

    @OneToMany(mappedBy = "knjiga",
           cascade = CascadeType.ALL,
           orphanRemoval = true,
           fetch = FetchType.LAZY)
    private List<KnjigaAutor> autori = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "zanr_id")
    private Zanr zanr;

    @OneToMany(mappedBy = "knjiga",
           cascade = CascadeType.ALL,
           orphanRemoval = true,
           fetch = FetchType.LAZY)
    private List<KnjigaKnjizara> dostupnost = new ArrayList<>();

    public Knjiga() {
    }

    public Knjiga(Long id, String naziv, String opis, Double cena, String isbn,
            LocalDate godinaIzdanja, String imageUrl, Zanr zanr) {
        this.id = id;
        this.naziv = naziv;
        this.opis = opis;
        this.cena = cena;
        this.isbn = isbn;
        this.godinaIzdanja = godinaIzdanja;
        this.imageUrl = imageUrl;
        this.zanr = zanr;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public Double getCena() {
        return cena;
    }

    public void setCena(Double cena) {
        this.cena = cena;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public LocalDate getGodinaIzdanja() {
        return godinaIzdanja;
    }

    public void setGodinaIzdanja(LocalDate godinaIzdanja) {
        this.godinaIzdanja = godinaIzdanja;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<KnjigaAutor> getAutori() {
        return autori;
    }

    public void setAutori(List<KnjigaAutor> autori) {
        this.autori = autori;
    }

    public Zanr getZanr() {
        return zanr;
    }

    public void setZanr(Zanr zanr) {
        this.zanr = zanr;
    }

    public List<KnjigaKnjizara> getDostupnost() {
        return dostupnost;
    }

    public void setDostupnost(List<KnjigaKnjizara> dostupnost) {
        this.dostupnost = dostupnost;
    }
}
