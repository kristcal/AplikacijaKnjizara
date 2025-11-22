/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package njt.mavenproject2.servis;

import java.util.List;
import java.util.stream.Collectors;
import njt.mavenproject2.dto.impl.KnjigaAutorDto;
import njt.mavenproject2.entity.impl.Autor;
import njt.mavenproject2.entity.impl.Knjiga;
import njt.mavenproject2.entity.impl.KnjigaAutor;
import njt.mavenproject2.mapper.impl.KnjigaAutorMapper;
import njt.mavenproject2.repository.impl.AutorRepository;
import njt.mavenproject2.repository.impl.KnjigaAutorRepository;
import njt.mavenproject2.repository.impl.KnjigaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class KnjigaAutorServis {

    private final KnjigaAutorRepository repo;
    private final KnjigaRepository knjigaRepo;
    private final AutorRepository autorRepo;
    private final KnjigaAutorMapper mapper;

    public KnjigaAutorServis(KnjigaAutorRepository repo, KnjigaRepository knjigaRepo,
            AutorRepository autorRepo, KnjigaAutorMapper mapper) {
        this.repo = repo;
        this.knjigaRepo = knjigaRepo;
        this.autorRepo = autorRepo;
        this.mapper = mapper;
    }

    public List<KnjigaAutorDto> listForKnjiga(Long knjigaId) {
        return repo.findByKnjigaId(knjigaId).stream().map(mapper::toDo).collect(Collectors.toList());
    }

    @Transactional
    public KnjigaAutorDto addAutorToKnjiga(Long knjigaId, KnjigaAutorDto dto) throws Exception {
        Knjiga k = knjigaRepo.findById(knjigaId);
        Autor a = autorRepo.findById(dto.getAutorId());
        KnjigaAutor ka = new KnjigaAutor(k, a, dto.getUloga());
        repo.save(ka);
        return mapper.toDo(ka);
    }

    @Transactional
    public void remove(Long knjigaAutorId) {
        repo.deleteById(knjigaAutorId);
    }
}
