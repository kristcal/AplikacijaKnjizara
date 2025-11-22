/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package njt.mavenproject2.mapper.impl;

import njt.mavenproject2.dto.impl.KnjigaAutorDto;
import njt.mavenproject2.entity.impl.KnjigaAutor;
import njt.mavenproject2.mapper.DtoEntityMapper;
import org.springframework.stereotype.Component;

/**
 *
 * @author Korisnik
 */
@Component
public class KnjigaAutorMapper implements DtoEntityMapper<KnjigaAutorDto, KnjigaAutor> {

    @Override
    public KnjigaAutorDto toDo(KnjigaAutor e) {
        if (e == null) return null;
        KnjigaAutorDto dto = new KnjigaAutorDto();
        dto.setId(e.getId());
        dto.setAutorId(e.getAutor() != null ? e.getAutor().getId() : null);
        dto.setUloga(e.getUloga());
        return dto;
    }

    @Override
    public KnjigaAutor toEntity(KnjigaAutorDto t) {
        if (t == null) return null;
        KnjigaAutor e = new KnjigaAutor();
        // Autor i Knjiga postavlja servis:
        // e.setAutor(autorRepo.findById(t.getAutorId()).orElseThrow());
        e.setUloga(t.getUloga());
        return e;
    }
}
