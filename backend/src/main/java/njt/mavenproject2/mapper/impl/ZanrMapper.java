/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package njt.mavenproject2.mapper.impl;

import njt.mavenproject2.dto.impl.ZanrDto;
import njt.mavenproject2.entity.impl.Zanr;
import njt.mavenproject2.mapper.DtoEntityMapper;
import org.springframework.stereotype.Component;

/**
 *
 * @author Korisnik
 */
@Component
public class ZanrMapper implements DtoEntityMapper<ZanrDto, Zanr> {

    @Override
    public ZanrDto toDo(Zanr e) {
        if (e == null) return null;
        return new ZanrDto(e.getId(), e.getNaziv());
    }

    @Override
    public Zanr toEntity(ZanrDto t) {
        if (t == null) return null;
        Zanr e = new Zanr();
        e.setId(t.getId());
        e.setNaziv(t.getNaziv());
        return e;
    }
}
