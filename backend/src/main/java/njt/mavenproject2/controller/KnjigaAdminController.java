// src/main/java/njt/mavenproject2/controller/KnjigaAdminController.java
package njt.mavenproject2.controller;

import njt.mavenproject2.dto.impl.KnjigaDto;
import njt.mavenproject2.servis.KnjigaServis;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/knjige")
@CrossOrigin(origins = "http://localhost:3000")
public class KnjigaAdminController {

    private final KnjigaServis servis;

    public KnjigaAdminController(KnjigaServis servis) {
        this.servis = servis;
    }

    @PostMapping
    public ResponseEntity<KnjigaDto> create(@RequestBody KnjigaDto dto) throws Exception {
        return ResponseEntity.ok(servis.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<KnjigaDto> update(@PathVariable Long id, @RequestBody KnjigaDto dto) throws Exception {
        return ResponseEntity.ok(servis.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        servis.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
