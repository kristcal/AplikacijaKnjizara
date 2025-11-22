/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package njt.mavenproject2.dto.impl;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import njt.mavenproject2.dto.Dto;

/**
 *
 * @author Korisnik
 */
public class StavkaPorudzbineDto implements Dto {

    private Long id; // null za create
    private Integer rb;

    @NotNull(message = "knjigaId je obavezan")
    private Long knjigaId;

    @NotNull(message = "kolicina je obavezna")
    @Positive(message = "kolicina mora biti > 0")
    private Integer kolicina;

    @NotNull(message = "cenaK je obavezna")
    @Positive(message = "cenaK mora biti > 0")
    private Double cenaK;

    // ukupanIznosStavke računa servis (kolicina * cenaK)
    public StavkaPorudzbineDto() {
    }

    public StavkaPorudzbineDto(Long id, Integer rb, Long knjigaId, Integer kolicina, Double cenaK) {
        this.id = id;
        this.rb = rb;
        this.knjigaId = knjigaId;
        this.kolicina = kolicina;
        this.cenaK = cenaK;
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

    public Long getKnjigaId() {
        return knjigaId;
    }

    public void setKnjigaId(Long knjigaId) {
        this.knjigaId = knjigaId;
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

}
