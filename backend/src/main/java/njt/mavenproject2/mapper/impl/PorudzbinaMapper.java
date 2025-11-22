package njt.mavenproject2.mapper.impl;

import njt.mavenproject2.dto.impl.PorudzbinaDto;
import njt.mavenproject2.entity.impl.Porudzbina;
import njt.mavenproject2.mapper.DtoEntityMapper;
import org.springframework.stereotype.Component;

@Component
public class PorudzbinaMapper implements DtoEntityMapper<PorudzbinaDto, Porudzbina> {

    @Override
    public PorudzbinaDto toDo(Porudzbina e) {
        if (e == null) return null;

        PorudzbinaDto dto = new PorudzbinaDto();
        dto.setId(e.getId());
        dto.setDatum(e.getDatum());
        dto.setUkupanIznos(e.getUkupanIznos());
        dto.setStatus(e.getStatus()); // ✅ novo polje

        if (e.getKorisnik() != null) {
            dto.setKorisnikId(e.getKorisnik().getId());
        }
        

        return dto;
    }

    @Override
    public Porudzbina toEntity(PorudzbinaDto dto) {
        if (dto == null) return null;

        Porudzbina e = new Porudzbina();
        e.setId(dto.getId());
        e.setDatum(dto.getDatum());
        e.setUkupanIznos(dto.getUkupanIznos());
        e.setStatus(dto.getStatus() != null ? dto.getStatus() : "KREIRANA"); // ✅ podrazumevani status

        // Korisnika i stavke dodaje servis (kao i do sada)
        return e;
    }
}
