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
public class ZanrDto implements Dto {

    private Long id;

    @NotBlank(message = "Naziv žanra je obavezan")
    @Size(max = 80, message = "Naziv žanra ne može biti duži od 80 karaktera")
    private String naziv;

    public ZanrDto() {
    }

    public ZanrDto(Long id, String naziv) {
        this.id = id;
        this.naziv = naziv;
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
}
