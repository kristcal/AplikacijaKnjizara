package njt.mavenproject2.mapper.impl;

import java.util.ArrayList;
import java.util.List;

import njt.mavenproject2.dto.impl.KnjigaDto;
import njt.mavenproject2.entity.impl.Knjiga;
import njt.mavenproject2.entity.impl.KnjigaAutor;
import njt.mavenproject2.entity.impl.KnjigaKnjizara;
import njt.mavenproject2.mapper.DtoEntityMapper;
import org.springframework.stereotype.Component;

@Component
public class KnjigaMapper implements DtoEntityMapper<KnjigaDto, Knjiga> {

    @Override
    public KnjigaDto toDo(Knjiga e) {
        if (e == null) {
            return null;
        }

        KnjigaDto d = new KnjigaDto();
        d.setId(e.getId());
        d.setNaziv(e.getNaziv());
        d.setOpis(e.getOpis());
        d.setCena(e.getCena());
        d.setIsbn(e.getIsbn());
        d.setGodinaIzdanja(e.getGodinaIzdanja());
        d.setImageUrl(e.getImageUrl());

        if (e.getZanr() != null) {
            d.setZanrId(e.getZanr().getId());
            d.setZanrNaziv(e.getZanr().getNaziv());
        }

        // --- AUTORI ---
        List<KnjigaDto.AutorView> aut = new ArrayList<>();
        if (e.getAutori() != null) {
            for (KnjigaAutor ka : e.getAutori()) {
                if (ka.getAutor() == null) {
                    continue;
                }
                KnjigaDto.AutorView a = new KnjigaDto.AutorView();
                a.id = ka.getAutor().getId();
                a.ime = ka.getAutor().getIme();
                a.prezime = ka.getAutor().getPrezime();
                a.uloga = ka.getUloga();
                aut.add(a);
            }
        }
        d.setAutori(aut);

        // --- DOSTUPNOST / KOLIČINA ---
        int total = 0;
        List<KnjigaDto.DostupnostView> dList = new ArrayList<>();
        if (e.getDostupnost() != null) {
            for (KnjigaKnjizara kk : e.getDostupnost()) {
                int kol = kk.getKolicina() == null ? 0 : kk.getKolicina();
                total += kol;

                KnjigaDto.DostupnostView dv = new KnjigaDto.DostupnostView();
                dv.kolicina = kol;

                if (kk.getKnjizara() != null) {
                    dv.knjizaraId = kk.getKnjizara().getId();
                    dv.knjizaraNaziv = kk.getKnjizara().getNaziv();
                    // ⬇️ DODATO: lokacija knjižare
                    dv.lokacija = kk.getKnjizara().getLokacija();
                }

                dList.add(dv);
            }
        }
        d.setKolicina(total);
        d.setDostupnost(dList);

        return d;
    }

    @Override
    public Knjiga toEntity(KnjigaDto d) {
        if (d == null) {
            return null;
        }
        Knjiga e = new Knjiga();
        e.setId(d.getId());
        e.setNaziv(d.getNaziv());
        e.setOpis(d.getOpis());
        e.setCena(d.getCena());
        e.setIsbn(d.getIsbn());
        e.setGodinaIzdanja(d.getGodinaIzdanja());
        e.setImageUrl(d.getImageUrl());

        // ključni deo
        if (e.getAutori() == null) {
            e.setAutori(new ArrayList<>());
        }
        if (e.getDostupnost() == null) {
            e.setDostupnost(new ArrayList<>());
        }
        return e;
    }
}
