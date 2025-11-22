/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package njt.mavenproject2.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import njt.mavenproject2.entity.impl.Autor;
import njt.mavenproject2.repository.MyAppRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Korisnik
 */
@Repository
public class AutorRepository implements MyAppRepository<Autor, Long> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Autor> findAll() {
        return entityManager.createQuery("SELECT a FROM Autor a", Autor.class).getResultList();
    }

    @Override
    public Autor findById(Long id) throws Exception {
        Autor autor = entityManager.find(Autor.class, id);
        if (autor == null) throw new Exception("Autor nije pronađen!");
        return autor;
    }

    @Override
    @Transactional
    public void save(Autor entity) {
        if (entity.getId() == null) entityManager.persist(entity);
        else entityManager.merge(entity);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Autor autor = entityManager.find(Autor.class, id);
        if (autor != null) entityManager.remove(autor);
    }
}
