package njt.mavenproject2.controller;

import java.util.List;
import njt.mavenproject2.dto.impl.PorudzbinaDto;
import njt.mavenproject2.servis.PorudzbinaServis;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/porudzbine")
@CrossOrigin(origins = "http://localhost:3000")
public class PorudzbinaAdminController {

    private final PorudzbinaServis servis;

    public PorudzbinaAdminController(PorudzbinaServis servis) {
        this.servis = servis;
    }

    @GetMapping
    public ResponseEntity<List<PorudzbinaDto>> sve() {
        return ResponseEntity.ok(servis.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PorudzbinaDto> jedna(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(servis.findById(id));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<PorudzbinaDto> promeniStatus(
            @PathVariable Long id,
            @RequestBody String noviStatus
    ) throws Exception {
        PorudzbinaDto dto = servis.promeniStatus(id, noviStatus);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> obrisi(@PathVariable Long id) {
        servis.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
