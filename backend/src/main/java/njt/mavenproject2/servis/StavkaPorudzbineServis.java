/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package njt.mavenproject2.servis;

import java.util.List;
import java.util.stream.Collectors;
import njt.mavenproject2.dto.impl.StavkaPorudzbineDto;
import njt.mavenproject2.entity.impl.Knjiga;
import njt.mavenproject2.entity.impl.Porudzbina;
import njt.mavenproject2.entity.impl.StavkaPorudzbine;
import njt.mavenproject2.mapper.impl.StavkaPorudzbineMapper;
import njt.mavenproject2.repository.impl.KnjigaRepository;
import njt.mavenproject2.repository.impl.PorudzbinaRepository;
import njt.mavenproject2.repository.impl.StavkaPorudzbineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StavkaPorudzbineServis {

    private final StavkaPorudzbineRepository repo;
    private final PorudzbinaRepository porRepo;
    private final KnjigaRepository knjigaRepo;
    private final StavkaPorudzbineMapper mapper;

    public StavkaPorudzbineServis(StavkaPorudzbineRepository repo,
            PorudzbinaRepository porRepo,
            KnjigaRepository knjigaRepo,
            StavkaPorudzbineMapper mapper) {
        this.repo = repo;
        this.porRepo = porRepo;
        this.knjigaRepo = knjigaRepo;
        this.mapper = mapper;
    }

    public List<StavkaPorudzbineDto> listForPorudzbina(Long porudzbinaId) {
        return repo.findByPorudzbinaId(porudzbinaId).stream().map(mapper::toDo).collect(Collectors.toList());
    }

    @Transactional
    public StavkaPorudzbineDto add(Long porudzbinaId, StavkaPorudzbineDto dto) throws Exception {
        Porudzbina p = porRepo.findById(porudzbinaId);
        Knjiga k = knjigaRepo.findById(dto.getKnjigaId());

        StavkaPorudzbine s = mapper.toEntity(dto);

        // dodela rb ako nije poslat
        Integer rb = s.getRb();
        if (rb == null || rb <= 0) {
            rb = repo.findMaxRbForPorudzbina(porudzbinaId) + 1;
        }
        s.setRb(rb);

        s.setPorudzbina(p);
        s.setKnjiga(k);

        repo.save(s);

        // RE-IZRAČUNAJ UKUPAN IZNOS PORUDŽBINE
        p.setUkupanIznos(
                repo.findByPorudzbinaId(porudzbinaId).stream()
                        .mapToDouble(x -> (x.getCenaK() != null && x.getKolicina() != null) ? x.getCenaK() * x.getKolicina() : 0d)
                        .sum()
        );
        porRepo.save(p);

        return mapper.toDo(s);
    }

    @Transactional
    public StavkaPorudzbineDto update(Long stavkaId, StavkaPorudzbineDto dto) throws Exception {
        StavkaPorudzbine s = repo.findById(stavkaId);
        if (dto.getKolicina() != null) {
            s.setKolicina(dto.getKolicina());
        }
        if (dto.getCenaK() != null) {
            s.setCenaK(dto.getCenaK());
        }
        if (dto.getKnjigaId() != null) {
            s.setKnjiga(knjigaRepo.findById(dto.getKnjigaId()));
        }

        repo.save(s);

        // RE-IZRAČUNAJ UKUPAN IZNOS PORUDŽBINE
        Long porId = s.getPorudzbina().getId();
        Porudzbina p = porRepo.findById(porId);
        p.setUkupanIznos(
                repo.findByPorudzbinaId(porId).stream()
                        .mapToDouble(x -> (x.getCenaK() != null && x.getKolicina() != null) ? x.getCenaK() * x.getKolicina() : 0d)
                        .sum()
        );
        porRepo.save(p);

        return mapper.toDo(s);
    }

    @Transactional
    public void remove(Long stavkaId) {
        try {
            StavkaPorudzbine s = repo.findById(stavkaId);
            Long porId = s.getPorudzbina().getId();

            repo.deleteById(stavkaId);

            // RE-IZRAČUNAJ UKUPAN IZNOS PORUDŽBINE
            Porudzbina p = porRepo.findById(porId);
            p.setUkupanIznos(
                    repo.findByPorudzbinaId(porId).stream()
                            .mapToDouble(x -> (x.getCenaK() != null && x.getKolicina() != null) ? x.getCenaK() * x.getKolicina() : 0d)
                            .sum()
            );
            porRepo.save(p);
        } catch (Exception ignore) {
            // ako ne postoji stavka, idempotentno – bez bacanja greške
        }
    }
}
