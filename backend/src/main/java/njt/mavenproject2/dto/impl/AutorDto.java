/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package njt.mavenproject2.dto.impl;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import njt.mavenproject2.dto.Dto;

/**
 *
 * @author Korisnik
 */
public class AutorDto implements Dto {

    private Long id;

    @NotBlank(message = "Ime je obavezno")
    @Size(max = 60, message = "Ime ne može biti duže od 60 karaktera")
    private String ime;

    @NotBlank(message = "Prezime je obavezno")
    @Size(max = 60, message = "Prezime ne može biti duže od 60 karaktera")
    private String prezime;

    public AutorDto() {
    }

    public AutorDto(Long id, String ime, String prezime) {
        this.id = id;
        this.ime = ime;
        this.prezime = prezime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }
}
