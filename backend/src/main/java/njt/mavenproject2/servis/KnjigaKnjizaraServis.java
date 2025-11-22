/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package njt.mavenproject2.servis;

import java.util.List;
import java.util.stream.Collectors;
import njt.mavenproject2.dto.impl.KnjigaKnjizaraDto;
import njt.mavenproject2.entity.impl.Knjiga;
import njt.mavenproject2.entity.impl.KnjigaKnjizara;
import njt.mavenproject2.entity.impl.Knjizara;
import njt.mavenproject2.mapper.impl.KnjigaKnjizaraMapper;
import njt.mavenproject2.repository.impl.KnjigaKnjizaraRepository;
import njt.mavenproject2.repository.impl.KnjigaRepository;
import njt.mavenproject2.repository.impl.KnjizaraRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class KnjigaKnjizaraServis {

    private final KnjigaKnjizaraRepository repo;
    private final KnjigaRepository knjigaRepo;
    private final KnjizaraRepository knjizaraRepo;
    private final KnjigaKnjizaraMapper mapper;

    public KnjigaKnjizaraServis(KnjigaKnjizaraRepository repo, KnjigaRepository knjigaRepo,
                                KnjizaraRepository knjizaraRepo, KnjigaKnjizaraMapper mapper) {
        this.repo = repo; this.knjigaRepo = knjigaRepo; this.knjizaraRepo = knjizaraRepo; this.mapper = mapper;
    }

    public List<KnjigaKnjizaraDto> listForKnjiga(Long knjigaId) {
        return repo.findByKnjigaId(knjigaId).stream().map(mapper::toDo).collect(Collectors.toList());
    }

    @Transactional
    public KnjigaKnjizaraDto addOrSet(Long knjigaId, KnjigaKnjizaraDto dto) throws Exception {
        Knjiga k = knjigaRepo.findById(knjigaId);
        Knjizara s = knjizaraRepo.findById(dto.getKnjizaraId());
        KnjigaKnjizara kk = new KnjigaKnjizara(k, s, dto.getKolicina());
        repo.save(kk);
        return mapper.toDo(kk);
    }

    @Transactional
    public KnjigaKnjizaraDto updateKolicina(Long kkId, Integer novaKolicina) throws Exception {
        KnjigaKnjizara kk = repo.findById(kkId);
        kk.setKolicina(novaKolicina);
        repo.save(kk);
        return mapper.toDo(kk);
    }

    @Transactional
    public void remove(Long kkId) { repo.deleteById(kkId); }
}
