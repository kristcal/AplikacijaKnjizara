/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package njt.mavenproject2.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import njt.mavenproject2.entity.impl.StavkaPorudzbine;
import njt.mavenproject2.repository.MyAppRepository;
import org.springframework.stereotype.Repository;

@Repository
public class StavkaPorudzbineRepository implements MyAppRepository<StavkaPorudzbine, Long> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<StavkaPorudzbine> findAll() {
        return entityManager.createQuery("SELECT s FROM StavkaPorudzbine s", StavkaPorudzbine.class).getResultList();
    }

    @Override
    public StavkaPorudzbine findById(Long id) throws Exception {
        StavkaPorudzbine s = entityManager.find(StavkaPorudzbine.class, id);
        if (s == null) throw new Exception("Stavka porudžbine nije pronađena!");
        return s;
    }

    @Override
    @Transactional
    public void save(StavkaPorudzbine entity) {
        if (entity.getId() == null) entityManager.persist(entity);
        else entityManager.merge(entity);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        StavkaPorudzbine s = entityManager.find(StavkaPorudzbine.class, id);
        if (s != null) entityManager.remove(s);
    }
    
    public List<StavkaPorudzbine> findByPorudzbinaId(Long porudzbinaId) {
        return entityManager.createQuery("SELECT s FROM StavkaPorudzbine s WHERE s.porudzbina.id=:pid", StavkaPorudzbine.class)
                 .setParameter("pid", porudzbinaId)
                 .getResultList();
    }

    public Integer findMaxRbForPorudzbina(Long porudzbinaId) {
        Integer max = entityManager.createQuery("SELECT COALESCE(MAX(s.rb), 0) FROM StavkaPorudzbine s WHERE s.porudzbina.id = :pid", Integer.class)
                        .setParameter("pid", porudzbinaId)
                        .getSingleResult();
        return max;
    }
    
}
