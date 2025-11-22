/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package njt.mavenproject2.servis;

import java.util.List;
import java.util.stream.Collectors;
import njt.mavenproject2.dto.impl.KorisnikDto;
import njt.mavenproject2.entity.impl.Korisnik;
import njt.mavenproject2.mapper.impl.KorisnikMapper;
import njt.mavenproject2.repository.impl.KorisnikRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class KorisnikServis {

    private final KorisnikRepository repo;
    private final KorisnikMapper mapper;

    public KorisnikServis(KorisnikRepository repo, KorisnikMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    public List<KorisnikDto> findAll() {
        return repo.findAll().stream().map(mapper::toDo).collect(Collectors.toList());
    }

    public KorisnikDto findById(Long id) throws Exception {
        return mapper.toDo(repo.findById(id));
    }
    @Transactional
    public KorisnikDto create(KorisnikDto dto) {
        Korisnik k = mapper.toEntity(dto);
        repo.save(k);
        return mapper.toDo(k);
    }
    @Transactional
    public KorisnikDto update(Long id, KorisnikDto dto) throws Exception {
        Korisnik existing = repo.findById(id);
        existing.setIme(dto.getIme());
        existing.setPrezime(dto.getPrezime());
        existing.setEmail(dto.getEmail());
        existing.setLozinka(dto.getLozinka());
        repo.save(existing);
        return mapper.toDo(existing);
    }
    @Transactional
    public void deleteById(Long id) {
        repo.deleteById(id);
    }
}
