/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package njt.mavenproject2.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import njt.mavenproject2.entity.impl.KnjigaAutor;
import njt.mavenproject2.repository.MyAppRepository;
import org.springframework.stereotype.Repository;

@Repository
public class KnjigaAutorRepository implements MyAppRepository<KnjigaAutor, Long> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<KnjigaAutor> findAll() {
        return entityManager.createQuery("SELECT ka FROM KnjigaAutor ka", KnjigaAutor.class)
                            .getResultList();
    }

    @Override
    public KnjigaAutor findById(Long id) throws Exception {
        KnjigaAutor ka = entityManager.find(KnjigaAutor.class, id);
        if (ka == null) throw new Exception("Veza knjiga–autor nije pronađena!");
        return ka;
    }

    @Override
    @Transactional
    public void save(KnjigaAutor entity) {
        if (entity.getId() == null) entityManager.persist(entity);
        else entityManager.merge(entity);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        KnjigaAutor ka = entityManager.find(KnjigaAutor.class, id);
        if (ka != null) entityManager.remove(ka);
    }
    // pomoćno: lista za konkretnu knjigu
    public List<KnjigaAutor> findByKnjigaId(Long knjigaId) {
        return entityManager.createQuery("SELECT ka FROM KnjigaAutor ka WHERE ka.knjiga.id = :kid", KnjigaAutor.class)
                 .setParameter("kid", knjigaId).getResultList();
    }
}
