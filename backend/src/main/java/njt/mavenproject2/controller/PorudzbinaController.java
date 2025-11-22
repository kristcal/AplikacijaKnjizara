package njt.mavenproject2.controller;

import jakarta.validation.Valid;
import njt.mavenproject2.dto.impl.PorudzbinaDto;
import njt.mavenproject2.servis.PorudzbinaServis;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/porudzbine")
public class PorudzbinaController {

    private final PorudzbinaServis service;

    public PorudzbinaController(PorudzbinaServis service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<PorudzbinaDto>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.findById(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> create(@Valid @RequestBody PorudzbinaDto req) {
        try {
            PorudzbinaDto res = service.create(req);
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody PorudzbinaDto req) {
        try {
            PorudzbinaDto res = service.update(id, req);
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.ok("Porudžbina obrisana.");
    }
    
     @GetMapping("/korisnik/{korisnikId}")
    public ResponseEntity<List<PorudzbinaDto>> moje(@PathVariable Long korisnikId) {
        return ResponseEntity.ok(service.findByKorisnik(korisnikId));
    }
    /*
    @GetMapping("/{id}")
    public PorudzbinaDto getOne(@PathVariable Long id) throws Exception {
        return service.findById(id);
    }*/
}