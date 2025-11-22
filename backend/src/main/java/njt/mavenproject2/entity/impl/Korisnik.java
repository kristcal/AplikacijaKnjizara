package njt.mavenproject2.entity.impl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import njt.mavenproject2.entity.MyEntity;

@Entity
@Table(
        name = "korisnik",
        uniqueConstraints = {
            @UniqueConstraint(name = "uk_korisnik_email", columnNames = "email")
        },
        indexes = {
            @Index(name = "idx_korisnik_email", columnList = "email")
        }
)
public class Korisnik implements MyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 60)
    private String ime;

    @NotBlank
    @Size(max = 60)
    private String prezime;

    @NotBlank
    @Email
    @Size(max = 120)
    private String email;

    /**
     * Čuvaj uvek HASH (npr. BCrypt). Ne izvozimo u JSON.
     */
    @Column(nullable = false)
    @Size(min = 4, max = 100, message = "Lozinka mora imati između 4 i 100 karaktera.")
    private String lozinka;

    /**
     * Ako planiraš role/uloge (npr. USER/ADMIN)
     */
    @Size(max = 40)
    private String uloga = "USER";

    /**
     * Ako izbacuješ korisnika u JSON-u, ne želimo rekurziju preko porudžbina.
     */
    @JsonIgnore
    @OneToMany(mappedBy = "korisnik", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Porudzbina> porudzbine = new ArrayList<>();

    public Korisnik() {
    }

    public Korisnik(Long id, String ime, String prezime, String email, String lozinka) {
        this.id = id;
        this.ime = ime;
        this.prezime = prezime;
        this.email = email;
        this.lozinka = lozinka;
    }

    // --- getters/setters ---
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

    public String getUloga() {
        return uloga;
    }

    public void setUloga(String uloga) {
        this.uloga = uloga;
    }

    public List<Porudzbina> getPorudzbine() {
        return porudzbine;
    }

    public void setPorudzbine(List<Porudzbina> porudzbine) {
        this.porudzbine = porudzbine;
    }

    // --- equals/hashCode samo po ID ---
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Korisnik that)) {
            return false;
        }
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Korisnik{id=%d, email=%s}".formatted(id, email);
    }
}
