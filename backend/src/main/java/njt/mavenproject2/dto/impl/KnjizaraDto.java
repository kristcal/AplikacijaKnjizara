/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package njt.mavenproject2.dto.impl;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import njt.mavenproject2.dto.Dto;
import njt.mavenproject2.entity.impl.KnjigaKnjizara;

/**
 *
 * @author Korisnik
 */
public class KnjizaraDto implements Dto{
    
    private Long id;
    @NotEmpty(message = "potreban je naziv")
    private String naziv;
    @NotBlank(message = "potrebna je lokacija")
    @Size(max=200, message = "Lokacija ne moze biti duza od 200 karaktera")
    private String lokacija;
    @NotEmpty(message = "potreban je kontakt")
    private String kontakt;

    private List<KnjigaKnjizaraDto> ponuda = new ArrayList<>();

    public KnjizaraDto(Long id, String naziv, String lokacija, String kontakt) {
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

    public List<KnjigaKnjizaraDto> getPonuda() {
        return ponuda;
    }

    public void setPonuda(List<KnjigaKnjizaraDto> ponuda) {
        this.ponuda = ponuda;
    }
    
    
    
}
