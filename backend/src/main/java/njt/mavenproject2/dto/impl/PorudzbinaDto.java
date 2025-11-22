package njt.mavenproject2.dto.impl;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PorudzbinaDto {

    /** READ_ONLY: vraća se klijentu */
    @JsonProperty(access = Access.READ_ONLY)
    private Long id;

    /** READ_ONLY: postavlja server */
    @JsonProperty(access = Access.READ_ONLY)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime datum;

    /** WRITE_ONLY: klijent šalje prilikom kreiranja */
    @JsonProperty(value = "korisnik_id", access = Access.WRITE_ONLY)
    @NotNull(message = "korisnik_id je obavezan")
    private Long korisnikId;

    /** READ_ONLY: računa server iz stavki */
    @JsonProperty(value = "ukupan_iznos", access = Access.READ_ONLY)
    private Double ukupanIznos;

    private String status;
    /** Stavke – klijent šalje pri kreiranju; u odgovoru možeš vratiti obogaćene nazive */
    @NotEmpty(message = "Stavke su obavezne")
    private List<Stavka> stavke = new ArrayList<>();

    // --- GET/SET ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getDatum() { return datum; }
    public void setDatum(LocalDateTime datum) { this.datum = datum; }

    public Long getKorisnikId() { return korisnikId; }
    public void setKorisnikId(Long korisnikId) { this.korisnikId = korisnikId; }

    public Double getUkupanIznos() { return ukupanIznos; }
    public void setUkupanIznos(Double ukupanIznos) { this.ukupanIznos = ukupanIznos; }

    public List<Stavka> getStavke() { return stavke; }
    public void setStavke(List<Stavka> stavke) { this.stavke = stavke; }

    public String getStatus() { return status; }
public void setStatus(String status) { this.status = status; }
    // --- Ugnježdena stavka ---
    public static class Stavka {
        /** WRITE_ONLY u requestu */
        @JsonProperty(value = "knjiga_id", access = Access.WRITE_ONLY)
        @NotNull(message = "knjiga_id je obavezno")
        private Long knjigaId;

        @NotNull(message = "kolicina je obavezna")
        private Integer kolicina;

        @NotNull(message = "cena je obavezna")
        private Double cena;

        /** READ_ONLY u odgovoru – da klijent vidi naziv knjige na računu */
        @JsonProperty(access = Access.READ_ONLY)
        private String naziv;

        
        // GET/SET
        public Long getKnjigaId() { return knjigaId; }
        public void setKnjigaId(Long knjigaId) { this.knjigaId = knjigaId; }

        public Integer getKolicina() { return kolicina; }
        public void setKolicina(Integer kolicina) { this.kolicina = kolicina; }

        public Double getCena() { return cena; }
        public void setCena(Double cena) { this.cena = cena; }

        public String getNaziv() { return naziv; }
        public void setNaziv(String naziv) { this.naziv = naziv; }
        
        
    }
}
