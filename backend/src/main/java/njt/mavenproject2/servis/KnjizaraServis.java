/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package njt.mavenproject2.servis;

import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;
import njt.mavenproject2.dto.impl.KnjizaraDto;
import njt.mavenproject2.entity.impl.Knjizara;
import njt.mavenproject2.mapper.impl.KnjizaraMapper;
import njt.mavenproject2.repository.impl.KnjizaraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Korisnik
 */
@Service
public class KnjizaraServis {
    
    private final KnjizaraRepository knjizaraRepository;
    private final KnjizaraMapper knjizaraMapper;
    @Autowired
    public KnjizaraServis(KnjizaraRepository knjizaraRepository, KnjizaraMapper knjizaraMapper) {
        this.knjizaraRepository = knjizaraRepository;
        this.knjizaraMapper = knjizaraMapper;
    }
    
    public List<KnjizaraDto> findAll(){
        return knjizaraRepository.findAll()
                .stream()
                .map(knjizaraMapper::toDo)
                .collect(Collectors.toList());
    }
    
    public KnjizaraDto findById(Long id) throws Exception{
        return knjizaraMapper.toDo(knjizaraRepository.findById(id));
    }
    @Transactional
    public KnjizaraDto create(KnjizaraDto dto){
        Knjizara knjizara = knjizaraMapper.toEntity(dto);
        knjizaraRepository.save(knjizara);
        return knjizaraMapper.toDo(knjizara);
    }
    @Transactional
    public void deleteById(Long id) {
        knjizaraRepository.deleteById(id);
    }
    @Transactional
    public KnjizaraDto update(KnjizaraDto dto) {
        Knjizara updated = knjizaraMapper.toEntity(dto);
        knjizaraRepository.save(updated);
        return knjizaraMapper.toDo(updated);
    }

    
    
}
