/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package njt.mavenproject2.mapper.impl;

import njt.mavenproject2.dto.impl.KnjigaKnjizaraDto;
import njt.mavenproject2.entity.impl.KnjigaKnjizara;
import njt.mavenproject2.mapper.DtoEntityMapper;
import org.springframework.stereotype.Component;

/**
 *
 * @author Korisnik
 */
@Component
public class KnjigaKnjizaraMapper implements DtoEntityMapper<KnjigaKnjizaraDto, KnjigaKnjizara> {

    @Override
    public KnjigaKnjizaraDto toDo(KnjigaKnjizara e) {
        if (e == null) return null;
        KnjigaKnjizaraDto dto = new KnjigaKnjizaraDto();
        dto.setId(e.getId());
        dto.setKnjizaraId(e.getKnjizara() != null ? e.getKnjizara().getId() : null);
        dto.setKolicina(e.getKolicina());
        return dto;
    }

    @Override
    public KnjigaKnjizara toEntity(KnjigaKnjizaraDto t) {
        if (t == null) return null;
        KnjigaKnjizara e = new KnjigaKnjizara();
        e.setId(t.getId());
        e.setKolicina(t.getKolicina());
        // Knjiga i Knjizara postavlja servis (knjizaraId iz t)
        return e;
    }
}
