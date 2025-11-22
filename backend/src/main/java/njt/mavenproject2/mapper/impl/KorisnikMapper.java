/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package njt.mavenproject2.mapper.impl;

import njt.mavenproject2.dto.impl.KorisnikDto;
import njt.mavenproject2.entity.impl.Korisnik;
import njt.mavenproject2.mapper.DtoEntityMapper;
import org.springframework.stereotype.Component;

/**
 *
 * @author Korisnik
 */
@Component
public class KorisnikMapper implements DtoEntityMapper<KorisnikDto, Korisnik> {

    @Override
    public KorisnikDto toDo(Korisnik e) {
        if (e == null) return null;
        return new KorisnikDto(e.getId(), e.getIme(), e.getPrezime(), e.getEmail(), e.getLozinka());
    }

    @Override
    public Korisnik toEntity(KorisnikDto t) {
        if (t == null) return null;
        Korisnik e = new Korisnik();
        e.setId(t.getId());
        e.setIme(t.getIme());
        e.setPrezime(t.getPrezime());
        e.setEmail(t.getEmail());
        e.setLozinka(t.getLozinka());
        return e;
    }
}
