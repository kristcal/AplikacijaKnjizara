/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package njt.mavenproject2.controller;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import njt.mavenproject2.dto.impl.KnjigaKnjizaraDto;
import njt.mavenproject2.servis.KnjigaKnjizaraServis;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/knjiga/{knjigaId}/ponuda")
public class KnjigaKnjizaraController {

    private final KnjigaKnjizaraServis servis;

    public KnjigaKnjizaraController(KnjigaKnjizaraServis servis) {
        this.servis = servis;
    }

    @GetMapping
    public ResponseEntity<List<KnjigaKnjizaraDto>> list(@PathVariable Long knjigaId) {
        return ResponseEntity.ok(servis.listForKnjiga(knjigaId));
    }

    @PostMapping
    public ResponseEntity<KnjigaKnjizaraDto> add(@PathVariable Long knjigaId,
            @Valid @RequestBody KnjigaKnjizaraDto dto) {
        try {
            return new ResponseEntity<>(servis.addOrSet(knjigaId, dto), HttpStatus.CREATED);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    @PutMapping("/{kkId}")
    public ResponseEntity<KnjigaKnjizaraDto> updateQty(@PathVariable Long knjigaId,
            @PathVariable Long kkId,
            @RequestBody Map<String, Integer> body) {
        try {
            Integer kolicina = body.get("kolicina");
            return ResponseEntity.ok(servis.updateKolicina(kkId, kolicina));
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    @DeleteMapping("/{kkId}")
    public ResponseEntity<Void> remove(@PathVariable Long knjigaId, @PathVariable Long kkId) {
        servis.remove(kkId);
        return ResponseEntity.ok().build();
    }
}
