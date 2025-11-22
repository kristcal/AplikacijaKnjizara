/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package njt.mavenproject2.controller;

import jakarta.validation.Valid;
import java.util.List;
import njt.mavenproject2.dto.impl.StavkaPorudzbineDto;
import njt.mavenproject2.servis.StavkaPorudzbineServis;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/porudzbina/{porudzbinaId}/stavke")
public class PorudzbinaStavkeController {

    private final StavkaPorudzbineServis servis;

    public PorudzbinaStavkeController(StavkaPorudzbineServis servis) {
        this.servis = servis;
    }

    @GetMapping
    public ResponseEntity<List<StavkaPorudzbineDto>> list(@PathVariable Long porudzbinaId) {
        return ResponseEntity.ok(servis.listForPorudzbina(porudzbinaId));
    }

    @PostMapping
    public ResponseEntity<StavkaPorudzbineDto> add(@PathVariable Long porudzbinaId,
                                                   @Valid @RequestBody StavkaPorudzbineDto dto) {
        try {
            return new ResponseEntity<>(servis.add(porudzbinaId, dto), HttpStatus.CREATED);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    @PutMapping("/{stavkaId}")
    public ResponseEntity<StavkaPorudzbineDto> update(@PathVariable Long porudzbinaId,
                                                      @PathVariable Long stavkaId,
                                                      @Valid @RequestBody StavkaPorudzbineDto dto) {
        try {
            return ResponseEntity.ok(servis.update(stavkaId, dto));
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    @DeleteMapping("/{stavkaId}")
    public ResponseEntity<Void> delete(@PathVariable Long porudzbinaId, @PathVariable Long stavkaId) {
        servis.remove(stavkaId);
        return ResponseEntity.noContent().build();
    }
}
