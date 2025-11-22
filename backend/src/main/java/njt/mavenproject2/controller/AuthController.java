package njt.mavenproject2.controller;

import njt.mavenproject2.dto.auth.LoginReq;
import njt.mavenproject2.dto.auth.LoginRes;
import njt.mavenproject2.dto.auth.RegisterReq;
import njt.mavenproject2.entity.impl.Korisnik;
import njt.mavenproject2.repository.impl.KorisnikRepository;
import njt.mavenproject2.servis.AuthServis;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthServis authServis;
    private final KorisnikRepository korisnikRepo;

    public AuthController(AuthServis authServis, KorisnikRepository korisnikRepo) {
        this.authServis = authServis;
        this.korisnikRepo = korisnikRepo;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginReq req) {
        try {
            Korisnik k = authServis.login(req.email(), req.password());
            String token = "demo-token"; // TODO: zameniti JWT-om
            return ResponseEntity.ok(new LoginRes(token, k.getId(), k.getEmail(), k.getIme(), k.getPrezime(), k.getUloga()));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterReq req) {
        // 1) provera email-a
        if (korisnikRepo.findByEmail(req.email()) != null) {
            return ResponseEntity.status(409).body("Email je zauzet.");
        }
        // 2) kreiraj korisnika
        Korisnik k = new Korisnik();
        k.setIme(req.ime());
        k.setPrezime(req.prezime());
        k.setEmail(req.email());
        k.setLozinka(req.password()); // za početak plain; kasnije hash
        k.setUloga("USER");
        korisnikRepo.save(k);

        return ResponseEntity.status(201).build();
    }
}
