/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package njt.mavenproject2.servis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import njt.mavenproject2.dto.impl.KnjigaDto;
import njt.mavenproject2.entity.impl.Autor;
import njt.mavenproject2.entity.impl.Knjiga;
import njt.mavenproject2.entity.impl.KnjigaAutor;
import njt.mavenproject2.entity.impl.KnjigaKnjizara;
import njt.mavenproject2.entity.impl.Knjizara;
import njt.mavenproject2.mapper.impl.KnjigaMapper;
import njt.mavenproject2.repository.impl.AutorRepository;
import njt.mavenproject2.repository.impl.KnjigaRepository;
import njt.mavenproject2.repository.impl.KnjizaraRepository;
import njt.mavenproject2.repository.impl.ZanrRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class KnjigaServis {

    private final KnjigaRepository repo;
    private final ZanrRepository zanrRepo;
    private final AutorRepository autorRepo;
    private final KnjizaraRepository knjizaraRepo;
    private final KnjigaMapper mapper;

    public KnjigaServis(
            KnjigaRepository repo,
            ZanrRepository zanrRepo,
            KnjigaMapper mapper,
            AutorRepository autorRepo,
            KnjizaraRepository knjizaraRepo
    ) {
        this.repo = repo;
        this.zanrRepo = zanrRepo;
        this.mapper = mapper;
        this.autorRepo = autorRepo;
        this.knjizaraRepo = knjizaraRepo;
    }

    public List<KnjigaDto> findAll() {
        return repo.findAll().stream().map(mapper::toDo).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public KnjigaDto findById(Long id) throws Exception {
        Knjiga k = repo.findOneFull(id); // JPQL sa join fetch – vraća sve vezano
        return mapper.toDo(k);
    }

    @Transactional
    public KnjigaDto create(KnjigaDto dto) throws Exception {
        System.out.println("[CREATE] dto.autori.size = " + (dto.getAutori() == null ? "null" : dto.getAutori().size()));
        System.out.println("[CREATE] dto.dostupnost.size = " + (dto.getDostupnost() == null ? "null" : dto.getDostupnost().size()));

        System.out.println("DTO dostupnost size = " + (dto.getDostupnost() == null ? 0 : dto.getDostupnost().size()));
        if (dto.getDostupnost() != null) {
            dto.getDostupnost().forEach(d
                    -> System.out.println(" -> shopId=" + d.knjizaraId + ", qty=" + d.kolicina));
        }

        Knjiga k = mapper.toEntity(dto);

        if (dto.getZanrId() != null) {
            k.setZanr(zanrRepo.findById(dto.getZanrId()));
        }

        // AUTORI
        if (dto.getAutori() != null) {
            for (KnjigaDto.AutorView a : dto.getAutori()) {
                if (a.id == null) {
                    continue;
                }
                Autor autor = autorRepo.findById(a.id);

                KnjigaAutor ka = new KnjigaAutor();
                ka.setKnjiga(k);               // owning
                ka.setAutor(autor);
                ka.setUloga(a.uloga);

                k.getAutori().add(ka);         // inverse
            }
        }

        // DOSTUPNOST
        if (dto.getDostupnost() != null) {
            for (KnjigaDto.DostupnostView d : dto.getDostupnost()) {
                if (d.knjizaraId == null) {
                    continue;
                }
                Knjizara kn = knjizaraRepo.findById(d.knjizaraId);

                KnjigaKnjizara kk = new KnjigaKnjizara();
                kk.setKnjiga(k);                         // owning
                kk.setKnjizara(kn);
                kk.setKolicina(d.kolicina == null ? 0 : d.kolicina);

                k.getDostupnost().add(kk);              // inverse
            }
        }

        repo.save(k); // zbog cascade=ALL deca se upisuju automatski
        return mapper.toDo(repo.findOneFull(k.getId()));
    }

    @Transactional
    public KnjigaDto update(Long id, KnjigaDto dto) throws Exception {
        System.out.println("[UPDATE] dto.dostupnost.size = " + dto.getDostupnost().size());
        dto.getDostupnost().forEach(d
                -> System.out.println(" -> shop=" + d.knjizaraId + ", qty=" + d.kolicina)
        );

        Knjiga existing = repo.findById(id);

        existing.setNaziv(dto.getNaziv());
        existing.setOpis(dto.getOpis());
        existing.setCena(dto.getCena());
        existing.setIsbn(dto.getIsbn());
        existing.setGodinaIzdanja(dto.getGodinaIzdanja());
        existing.setImageUrl(dto.getImageUrl());
        existing.setZanr(dto.getZanrId() != null ? zanrRepo.findById(dto.getZanrId()) : null);

        // refresh autori
        existing.getAutori().clear();
        if (dto.getAutori() != null) {
            for (KnjigaDto.AutorView a : dto.getAutori()) {
                if (a.id == null) {
                    continue;
                }
                Autor autor = autorRepo.findById(a.id);

                KnjigaAutor ka = new KnjigaAutor();
                ka.setKnjiga(existing);
                ka.setAutor(autor);
                ka.setUloga(a.uloga);
                existing.getAutori().add(ka);
            }
        }

        // REFRESH DOSTUPNOSTI – diff pristup, bez dupliranja
        Map<Long, KnjigaKnjizara> poKnjizari = existing.getDostupnost().stream()
                .filter(kk -> kk.getKnjizara() != null)
                .collect(Collectors.toMap(kk -> kk.getKnjizara().getId(), kk -> kk));

        if (dto.getDostupnost() != null) {
            for (KnjigaDto.DostupnostView d : dto.getDostupnost()) {
                if (d.knjizaraId == null) {
                    continue;
                }

                KnjigaKnjizara kk = poKnjizari.remove(d.knjizaraId); // postoji?
                if (kk == null) {
                    // nova veza
                    Knjizara kn = knjizaraRepo.findById(d.knjizaraId);
                    kk = new KnjigaKnjizara();
                    kk.setKnjiga(existing);
                    kk.setKnjizara(kn);
                    existing.getDostupnost().add(kk);
                }
                kk.setKolicina(d.kolicina == null ? 0 : Math.max(0, d.kolicina));

            }
        }

// što je ostalo u mapi je višak – ukloni (orphanRemoval će obrisati u bazi)
        poKnjizari.values().forEach(existing.getDostupnost()::remove);

        repo.save(existing);
        return mapper.toDo(repo.findOneFull(existing.getId()));
    }

    @Transactional
    public void deleteById(Long id) {
        repo.deleteById(id);
    }

    public List<KnjigaDto> findByGenre(Long zanrId) {
        return repo.findByGenre(zanrId).stream().map(mapper::toDo).collect(Collectors.toList());
    }

    public List<KnjigaDto> findCheaperThan(Double max) {
        return repo.findCheaperThan(max).stream().map(mapper::toDo).collect(Collectors.toList());
    }

    public List<KnjigaDto> search(String q, Long zanrId, Double max) {
        return repo.search(q, zanrId, max).stream().map(mapper::toDo).collect(Collectors.toList());
    }
}
