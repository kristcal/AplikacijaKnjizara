/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package njt.mavenproject2.dto.impl;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import njt.mavenproject2.dto.Dto;

/**
 *
 * @author Korisnik
 */
public class KnjigaAutorDto implements Dto {

    private Long id; // null kod kreiranja

    @NotNull(message = "autorId je obavezan")
    private Long autorId;

    @NotBlank(message = "Uloga je obavezna")
    @Size(max = 50, message = "Uloga ne može biti duža od 50 karaktera")
    private String uloga;

    public KnjigaAutorDto() {
    }

    public KnjigaAutorDto(Long id, Long autorId, String uloga) {
        this.id = id;
        this.autorId = autorId;
        this.uloga = uloga;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAutorId() {
        return autorId;
    }

    public void setAutorId(Long autorId) {
        this.autorId = autorId;
    }

    public String getUloga() {
        return uloga;
    }

    public void setUloga(String uloga) {
        this.uloga = uloga;
    }

}
