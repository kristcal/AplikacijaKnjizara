/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package njt.mavenproject2.mapper.impl;

import njt.mavenproject2.dto.impl.AutorDto;
import njt.mavenproject2.entity.impl.Autor;
import njt.mavenproject2.mapper.DtoEntityMapper;
import org.springframework.stereotype.Component;

/**
 *
 * @author Korisnik
 */
@Component
public class AutorMapper implements DtoEntityMapper<AutorDto, Autor> {

    @Override
    public AutorDto toDo(Autor e) {
        if (e == null) return null;
        return new AutorDto(e.getId(), e.getIme(), e.getPrezime());
    }

    @Override
    public Autor toEntity(AutorDto t) {
        if (t == null) return null;
        Autor e = new Autor();
        e.setId(t.getId());
        e.setIme(t.getIme());
        e.setPrezime(t.getPrezime());
        return e;
    }
}
