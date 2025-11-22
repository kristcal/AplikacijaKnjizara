/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package njt.mavenproject2.dto.impl;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import njt.mavenproject2.dto.Dto;

/**
 *
 * @author Korisnik
 */
public class KorisnikDto implements Dto {

    private Long id;

    @NotBlank(message = "Ime je obavezno")
    @Size(max = 60, message = "Ime ne može biti duže od 60 karaktera")
    private String ime;

    @NotBlank(message = "Prezime je obavezno")
    @Size(max = 60, message = "Prezime ne može biti duže od 60 karaktera")
    private String prezime;

    @NotBlank(message = "Email je obavezan")
    @Email(message = "Email nije ispravan")
    @Size(max = 120, message = "Email ne može biti duži od 120 karaktera")
    private String email;

    @NotBlank(message = "Lozinka je obavezna")
    @Size(min = 6, max = 100, message = "Lozinka mora imati 6-100 karaktera")
    private String lozinka;

    public KorisnikDto() {
    }

    public KorisnikDto(Long id, String ime, String prezime, String email, String lozinka) {
        this.id = id;
        this.ime = ime;
        this.prezime = prezime;
        this.email = email;
        this.lozinka = lozinka;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLozinka() {
        return lozinka;
    }

    public void setLozinka(String lozinka) {
        this.lozinka = lozinka;
    }

}
