package njt.mavenproject2.dto.impl;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import njt.mavenproject2.dto.Dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
// Dozvoljava snake_case iz fronta (image_url, zanr_id, godina_izdanja, ...):
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KnjigaDto implements Dto {

    private Long id;
    private String naziv;
    private String opis;
    private Double cena;
    private String isbn;

    @JsonAlias({"godina_izdanja"})
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate godinaIzdanja;

    @JsonAlias({"image_url"})
    private String imageUrl;

    // KLJUČNO: prihvata i zanrId i zanr_id
    @JsonAlias({"zanr_id"})
    private Long zanrId;

    private String zanrNaziv;

    // AUTORI
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AutorView {
        public Long id;
        public String ime;
        public String prezime;
        public String uloga;
    }
    private List<AutorView> autori = new ArrayList<>();

    private Integer kolicina;

    // DOSTUPNOST PO KNJIŽARAMA
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DostupnostView {
        @JsonAlias({"knjizara_id"})
        public Long knjizaraId;

        @JsonAlias({"knjizara_naziv"})
        public String knjizaraNaziv;

        @JsonAlias({"knjizara_lokacija"})
        public String lokacija;

        public Integer kolicina;
    }
    private List<DostupnostView> dostupnost = new ArrayList<>();

    // GET / SET
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNaziv() { return naziv; }
    public void setNaziv(String naziv) { this.naziv = naziv; }

    public String getOpis() { return opis; }
    public void setOpis(String opis) { this.opis = opis; }

    public Double getCena() { return cena; }
    public void setCena(Double cena) { this.cena = cena; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public LocalDate getGodinaIzdanja() { return godinaIzdanja; }
    public void setGodinaIzdanja(LocalDate godinaIzdanja) { this.godinaIzdanja = godinaIzdanja; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public Long getZanrId() { return zanrId; }
    public void setZanrId(Long zanrId) { this.zanrId = zanrId; }

    public String getZanrNaziv() { return zanrNaziv; }
    public void setZanrNaziv(String zanrNaziv) { this.zanrNaziv = zanrNaziv; }

    public List<AutorView> getAutori() { return autori; }
    public void setAutori(List<AutorView> autori) { this.autori = autori; }

    public Integer getKolicina() { return kolicina; }
    public void setKolicina(Integer kolicina) { this.kolicina = kolicina; }

    public List<DostupnostView> getDostupnost() { return dostupnost; }
    public void setDostupnost(List<DostupnostView> dostupnost) { this.dostupnost = dostupnost; }
}
