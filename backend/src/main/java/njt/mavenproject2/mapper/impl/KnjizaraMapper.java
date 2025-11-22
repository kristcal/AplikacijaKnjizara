/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package njt.mavenproject2.mapper.impl;

import njt.mavenproject2.dto.impl.KnjizaraDto;
import njt.mavenproject2.entity.impl.Knjizara;
import njt.mavenproject2.mapper.DtoEntityMapper;
import org.springframework.stereotype.Component;

/**
 *
 * @author Korisnik
 */
@Component
public class KnjizaraMapper implements DtoEntityMapper<KnjizaraDto, Knjizara> {

    @Override
    public KnjizaraDto toDo(Knjizara e) {
        if (e == null) return null;
        KnjizaraDto dto = new KnjizaraDto(e.getId(), e.getNaziv(), e.getLokacija(), e.getKontakt());
        return dto;
    }

    @Override
    public Knjizara toEntity(KnjizaraDto t) {
        if (t == null) return null;
        return new Knjizara(t.getId(), t.getNaziv(), t.getLokacija(), t.getKontakt());
    }
    
}
