/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package njt.mavenproject2.servis;

import java.util.List;
import java.util.stream.Collectors;
import njt.mavenproject2.dto.impl.ZanrDto;
import njt.mavenproject2.entity.impl.Zanr;
import njt.mavenproject2.mapper.impl.ZanrMapper;
import njt.mavenproject2.repository.impl.ZanrRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ZanrServis {

    private final ZanrRepository zanrRepository;
    private final ZanrMapper zanrMapper;

    public ZanrServis(ZanrRepository repo, ZanrMapper mapper) {
        this.zanrRepository = repo;
        this.zanrMapper = mapper;
    }

    public List<ZanrDto> findAll() {
        return zanrRepository.findAll().stream().map(zanrMapper::toDo).collect(Collectors.toList());
    }

    public ZanrDto findById(Long id) throws Exception {
        return zanrMapper.toDo(zanrRepository.findById(id));
    }
    @Transactional
    public ZanrDto create(ZanrDto dto) {
        Zanr z = zanrMapper.toEntity(dto);
        zanrRepository.save(z);
        return zanrMapper.toDo(z);
    }
    @Transactional
    public ZanrDto update(ZanrDto dto) {
        Zanr z = zanrMapper.toEntity(dto);
        zanrRepository.save(z);
        return zanrMapper.toDo(z);
    }
    @Transactional
    public void deleteById(Long id) {
        zanrRepository.deleteById(id);
    }
}
