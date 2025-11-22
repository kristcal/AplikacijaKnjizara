/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package njt.mavenproject2.repository.impl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import njt.mavenproject2.entity.impl.Knjizara;
import njt.mavenproject2.repository.MyAppRepository;
import org.springframework.stereotype.Repository;

@Repository
public class KnjizaraRepository implements MyAppRepository<Knjizara, Long> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Knjizara> findAll() {
        return entityManager.createQuery("SELECT k FROM Knjizara k", Knjizara.class).getResultList();
    }

    @Override
    public Knjizara findById(Long id) throws Exception {
        Knjizara knjizara = entityManager.find(Knjizara.class, id);
        if (knjizara == null) throw new Exception("Knjizara nije pronađena!");
        return knjizara;
    }

    @Override
    @Transactional
    public void save(Knjizara entity) {
        if (entity.getId() == null) entityManager.persist(entity);
        else entityManager.merge(entity);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Knjizara knjizara = entityManager.find(Knjizara.class, id);
        if (knjizara != null) entityManager.remove(knjizara);
    }
}