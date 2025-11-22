/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package njt.mavenproject2.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import njt.mavenproject2.dto.impl.KnjigaAutorDto;
import njt.mavenproject2.dto.impl.KnjigaDto;
import njt.mavenproject2.dto.impl.KnjigaKnjizaraDto;
import njt.mavenproject2.servis.KnjigaAutorServis;
import njt.mavenproject2.servis.KnjigaKnjizaraServis;
import njt.mavenproject2.servis.KnjigaServis;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/knjige")
@CrossOrigin(origins = "http://localhost:3000")
public class KnjigaController {

    private final KnjigaServis service;

    public KnjigaController(KnjigaServis service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<KnjigaDto>> getAll() {
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<KnjigaDto> getById(@PathVariable @NotNull Long id) {
        try {
            return new ResponseEntity<>(service.findById(id), HttpStatus.OK);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<KnjigaDto> create(@RequestBody KnjigaDto dto) throws Exception {
        return ResponseEntity.ok(service.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<KnjigaDto> update(@PathVariable Long id, @RequestBody KnjigaDto dto) throws Exception {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/genre/{zanrId}")
    public ResponseEntity<List<KnjigaDto>> byGenre(@PathVariable Long zanrId) {
        return ResponseEntity.ok(service.findByGenre(zanrId));
    }

    @GetMapping("/cheap")
    public ResponseEntity<List<KnjigaDto>> cheap(@RequestParam(defaultValue = "1000") Double max) {
        return ResponseEntity.ok(service.findCheaperThan(max));
    }

    @GetMapping("/search")
    public ResponseEntity<List<KnjigaDto>> search(
            @RequestParam String q,
            @RequestParam(required = false) Long zanrId,
            @RequestParam(required = false) Double max) {
        return ResponseEntity.ok(service.search(q, zanrId, max));
    }

}
