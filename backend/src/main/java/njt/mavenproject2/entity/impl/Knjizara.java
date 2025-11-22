/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package njt.mavenproject2.entity.impl;

import java.util.List;
import jakarta.persistence.*;
import java.util.ArrayList;
import njt.mavenproject2.entity.MyEntity;

/**
 *
 * @author Korisnik
 */
@Entity
@Table(name = "knjizara")
public class Knjizara implements MyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String naziv;
    private String lokacija;
    private String kontakt;

    @OneToMany(mappedBy = "knjizara", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<KnjigaKnjizara> ponuda = new ArrayList<>();

    public Knjizara() {
    }

    public Knjizara(Long id, String naziv, String lokacija, String kontakt) {
        this.id = id;
        this.naziv = naziv;
        this.lokacija = lokacija;
        this.kontakt = kontakt;
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

    public String getLokacija() {
        return lokacija;
    }

    public void setLokacija(String lokacija) {
        this.lokacija = lokacija;
    }

    public String getKontakt() {
        return kontakt;
    }

    public void setKontakt(String kontakt) {
        this.kontakt = kontakt;
    }

    public List<KnjigaKnjizara> getPonuda() {
        return ponuda;
    }

    public void setPonuda(List<KnjigaKnjizara> ponuda) {
        this.ponuda = ponuda;
    }
}
