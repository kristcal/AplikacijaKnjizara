/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/*
package njt.mavenproject2.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import njt.mavenproject2.dto.impl.KnjizaraDto;
import njt.mavenproject2.servis.KnjizaraServis;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
*/
/**
 *
 * @author Korisnik
 */
/*
@RestController
@RequestMapping("/api/knjizara")
public class KnjizaraController {

    private final KnjizaraServis knjizaraService;

    public KnjizaraController(KnjizaraServis knjizaraService) {
        this.knjizaraService = knjizaraService;
    }

    @GetMapping
    @Operation(summary = "Retrieve all Knjizara entities.")
    @ApiResponse(responseCode = "200", content = {
        @Content(schema = @Schema(implementation = KnjizaraDto.class), mediaType = "application/json")})
    public ResponseEntity<List<KnjizaraDto>> getAll() {
        return new ResponseEntity<>(knjizaraService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<KnjizaraDto> getById(
            @NotNull(message = "Should not be null or empty.")
            @PathVariable(value = "id") Long id) {
        try {
            return new ResponseEntity<>(knjizaraService.findById(id), HttpStatus.OK);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "KnjizaraController exception " + ex.getMessage());
        }
    }

    @PostMapping
    @Operation(summary = "Create a new Knjizara entity.")
    @ApiResponse(responseCode = "201", content = {
        @Content(schema = @Schema(implementation = KnjizaraDto.class), mediaType = "application/json")})
    public ResponseEntity<KnjizaraDto> addKnjizara(@Valid @RequestBody @NotNull KnjizaraDto knjizaraDto) {
        try {
            //System.out.println(knjizaraDto);
            KnjizaraDto saved = knjizaraService.create(knjizaraDto);
            return new ResponseEntity<>(saved, HttpStatus.CREATED);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error while saving knjizara " + ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable(value = "id") Long id) {
        try {
            knjizaraService.deleteById(id);
            return new ResponseEntity<>("Knjizara successfully deleted.", HttpStatus.OK);
        }catch (Exception ex){
            return new ResponseEntity<>("Knjizara does not exist: "+id, HttpStatus.NOT_FOUND);
        }
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update an exiscting Knjizara entity.")
    @ApiResponse(responseCode = "200", content = {
        @Content(schema = @Schema(implementation = KnjizaraDto.class), mediaType = "application/json")
    })
    public ResponseEntity<KnjizaraDto> updateKnjizara(
        @PathVariable Long id,
            @Valid @RequestBody KnjizaraDto knjizaraDto){
        
        try{
            knjizaraDto.setId(id);
            KnjizaraDto updated = knjizaraService.update(knjizaraDto);
            return new ResponseEntity<>(updated, HttpStatus.OK);
        }catch (Exception ex){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error while updating knjizara "+ ex.getMessage());
        }
    }

}
*/

package njt.mavenproject2.controller;

import java.util.List;
import java.util.stream.Collectors;
import njt.mavenproject2.entity.impl.Knjizara;
import njt.mavenproject2.repository.impl.KnjizaraRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/knjizare")
@CrossOrigin(origins = "http://localhost:3000")
public class KnjizaraController {

    private final KnjizaraRepository repo;

    public KnjizaraController(KnjizaraRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public ResponseEntity<List<KnjizaraDto>> getAll() {
        List<KnjizaraDto> list = repo.findAll().stream()
            .map(k -> new KnjizaraDto(k.getId(), k.getNaziv(), k.getLokacija()))
            .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    public static record KnjizaraDto(Long id, String naziv, String lokacija) {}
}
