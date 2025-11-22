/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package njt.mavenproject2.dto.impl;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import njt.mavenproject2.dto.Dto;

/**
 *
 * @author Korisnik
 */
public class KnjigaKnjizaraDto implements Dto {

    private Long id; // null kod kreiranja

    @NotNull(message = "knjizaraId je obavezan")
    private Long knjizaraId;

    @NotNull(message = "kolicina je obavezna")
    @PositiveOrZero(message = "kolicina mora biti >= 0")
    private Integer kolicina;

    private String lokacija;
    public KnjigaKnjizaraDto() {
    }

    public KnjigaKnjizaraDto(Long id, Long knjizaraId, Integer kolicina) {
        this.id = id;
        this.knjizaraId = knjizaraId;
        this.kolicina = kolicina;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getKnjizaraId() {
        return knjizaraId;
    }

    public void setKnjizaraId(Long knjizaraId) {
        this.knjizaraId = knjizaraId;
    }

    public Integer getKolicina() {
        return kolicina;
    }

    public void setKolicina(Integer kolicina) {
        this.kolicina = kolicina;
    }
    public String getLokacija() { return lokacija; }
    public void setLokacija(String lokacija) { this.lokacija = lokacija; }
}
