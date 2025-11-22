/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package njt.mavenproject2.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import njt.mavenproject2.entity.impl.Zanr;
import njt.mavenproject2.repository.MyAppRepository;
import org.springframework.stereotype.Repository;

@Repository
public class ZanrRepository implements MyAppRepository<Zanr, Long> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Zanr> findAll() {
        return entityManager.createQuery("SELECT z FROM Zanr z", Zanr.class).getResultList();
    }

    @Override
    public Zanr findById(Long id) throws Exception {
        Zanr zanr = entityManager.find(Zanr.class, id);
        if (zanr == null) throw new Exception("Žanr nije pronađen!");
        return zanr;
    }

    @Override
    @Transactional
    public void save(Zanr entity) {
        if (entity.getId() == null) entityManager.persist(entity);
        else entityManager.merge(entity);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Zanr zanr = entityManager.find(Zanr.class, id);
        if (zanr != null) entityManager.remove(zanr);
    }
}