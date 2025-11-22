/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package njt.mavenproject2.controller;

import jakarta.validation.Valid;
import java.util.List;
import njt.mavenproject2.dto.impl.KnjigaAutorDto;
import njt.mavenproject2.servis.KnjigaAutorServis;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/knjiga/{knjigaId}/autori")
public class KnjigaAutorController {

    private final KnjigaAutorServis servis;

    public KnjigaAutorController(KnjigaAutorServis servis) {
        this.servis = servis;
    }

    @GetMapping
    public ResponseEntity<List<KnjigaAutorDto>> list(@PathVariable Long knjigaId) {
        return ResponseEntity.ok(servis.listForKnjiga(knjigaId));
    }

    @PostMapping
    public ResponseEntity<KnjigaAutorDto> add(@PathVariable Long knjigaId,
            @Valid @RequestBody KnjigaAutorDto dto) {
        try {
            return new ResponseEntity<>(servis.addAutorToKnjiga(knjigaId, dto), HttpStatus.CREATED);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    @DeleteMapping("/{kaId}")
    public ResponseEntity<Void> remove(@PathVariable Long knjigaId, @PathVariable Long kaId) {
        servis.remove(kaId);
        return ResponseEntity.ok().build();
    }
}
