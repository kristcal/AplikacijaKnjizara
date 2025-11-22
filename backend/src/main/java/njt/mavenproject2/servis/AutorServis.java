/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package njt.mavenproject2.servis;

import java.util.List;
import java.util.stream.Collectors;
import njt.mavenproject2.dto.impl.AutorDto;
import njt.mavenproject2.entity.impl.Autor;
import njt.mavenproject2.mapper.impl.AutorMapper;
import njt.mavenproject2.repository.impl.AutorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AutorServis {

    private final AutorRepository repo;
    private final AutorMapper mapper;

    public AutorServis(AutorRepository repo, AutorMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    public List<AutorDto> findAll() {
        return repo.findAll().stream().map(mapper::toDo).collect(Collectors.toList());
    }

    public AutorDto findById(Long id) throws Exception {
        return mapper.toDo(repo.findById(id));
    }
    @Transactional
    public AutorDto create(AutorDto dto) {
        Autor a = mapper.toEntity(dto);
        repo.save(a);
        return mapper.toDo(a);
    }
    @Transactional
    public AutorDto update(AutorDto dto) {
        Autor a = mapper.toEntity(dto);
        repo.save(a);
        return mapper.toDo(a);
    }
    @Transactional
    public void deleteById(Long id) {
        repo.deleteById(id);
    }
}
