package njt.mavenproject2.servis;

import njt.mavenproject2.entity.impl.Korisnik;
import njt.mavenproject2.repository.impl.KorisnikRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServis {

    private final KorisnikRepository repo;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public AuthServis(KorisnikRepository repo) {
        this.repo = repo;
    }

    /**
     * Proverava da li korisnik postoji i da li je lozinka tačna.
     */
    public Korisnik login(String email, String password) throws Exception {
        Korisnik k = repo.findByEmail(email);
        if (k == null) {
            throw new Exception("Nalog sa ovim emailom ne postoji.");
        }

        // Ako su lozinke već hashovane, koristi encoder; ako nisu, poredi plain tekst.
        if (isHashed(k.getLozinka())) {
            if (!encoder.matches(password, k.getLozinka())) {
                throw new Exception("Pogrešna lozinka.");
            }
        } else {
            if (!password.equals(k.getLozinka())) {
                throw new Exception("Pogrešna lozinka.");
            }
        }

        return k;
    }

    private boolean isHashed(String pw) {
        return pw != null && pw.startsWith("$2a$");
    }
}
