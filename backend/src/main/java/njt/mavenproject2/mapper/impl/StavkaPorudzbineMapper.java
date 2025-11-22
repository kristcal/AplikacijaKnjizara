/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package njt.mavenproject2.mapper.impl;

import njt.mavenproject2.dto.impl.StavkaPorudzbineDto;
import njt.mavenproject2.entity.impl.StavkaPorudzbine;
import njt.mavenproject2.mapper.DtoEntityMapper;
import org.springframework.stereotype.Component;

/**
 *
 * @author Korisnik
 */
@Component
public class StavkaPorudzbineMapper implements DtoEntityMapper<StavkaPorudzbineDto, StavkaPorudzbine> {

    @Override
    public StavkaPorudzbineDto toDo(StavkaPorudzbine e) {
        if (e == null) return null;
        Long knjigaId = e.getKnjiga() != null ? e.getKnjiga().getId() : null;
        return new StavkaPorudzbineDto(e.getId(), e.getRb(), knjigaId, e.getKolicina(), e.getCenaK());
    }

    @Override
    public StavkaPorudzbine toEntity(StavkaPorudzbineDto t) {
        if (t == null) return null;
        var s = new StavkaPorudzbine();
        s.setId(t.getId());
        s.setRb(t.getRb()); // obično null; dodeljujemo u servisu ako je null
        s.setKolicina(t.getKolicina());
        s.setCenaK(t.getCenaK());
        return s;
    }
}
