/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/*package njt.mavenproject2.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import njt.mavenproject2.dto.impl.AutorDto;
import njt.mavenproject2.servis.AutorServis;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;



@RestController
@RequestMapping("/api/autor")
public class AutorController {

    private final AutorServis service;

    public AutorController(AutorServis service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<AutorDto>> getAll() {
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AutorDto> getById(@PathVariable @NotNull Long id) {
        try {
            return new ResponseEntity<>(service.findById(id), HttpStatus.OK);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<AutorDto> create(@Valid @RequestBody @NotNull AutorDto dto) {
        return new ResponseEntity<>(service.create(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AutorDto> update(@PathVariable Long id, @Valid @RequestBody AutorDto dto) {
        dto.setId(id);
        return new ResponseEntity<>(service.update(dto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        service.deleteById(id);
        return new ResponseEntity<>("Autor deleted.", HttpStatus.OK);
    }
}
*/

package njt.mavenproject2.controller;

import java.util.List;
import java.util.stream.Collectors;
import njt.mavenproject2.entity.impl.Autor;
import njt.mavenproject2.repository.impl.AutorRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/autori")
@CrossOrigin(origins = "http://localhost:3000")
public class AutorController {

    private final AutorRepository repo;

    public AutorController(AutorRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public ResponseEntity<List<AutorDto>> getAll() {
        List<AutorDto> list = repo.findAll().stream()
            .map(a -> new AutorDto(a.getId(), a.getIme(), a.getPrezime()))
            .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    // minimalistički DTO samo za listanje
    public static record AutorDto(Long id, String ime, String prezime) {}
}

