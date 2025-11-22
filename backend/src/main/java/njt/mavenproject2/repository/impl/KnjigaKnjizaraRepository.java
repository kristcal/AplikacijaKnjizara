/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package njt.mavenproject2.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import njt.mavenproject2.entity.impl.KnjigaKnjizara;
import njt.mavenproject2.repository.MyAppRepository;
import org.springframework.stereotype.Repository;

@Repository
public class KnjigaKnjizaraRepository implements MyAppRepository<KnjigaKnjizara, Long> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<KnjigaKnjizara> findAll() {
        return entityManager.createQuery("SELECT kk FROM KnjigaKnjizara kk", KnjigaKnjizara.class)
                            .getResultList();
    }

    @Override
    public KnjigaKnjizara findById(Long id) throws Exception {
        KnjigaKnjizara kk = entityManager.find(KnjigaKnjizara.class, id);
        if (kk == null) throw new Exception("Veza knjiga–knjižara nije pronađena!");
        return kk;
    }

    @Override
    @Transactional
    public void save(KnjigaKnjizara entity) {
        if (entity.getId() == null) entityManager.persist(entity);
        else entityManager.merge(entity);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        KnjigaKnjizara kk = entityManager.find(KnjigaKnjizara.class, id);
        if (kk != null) entityManager.remove(kk);
    }
    
    public List<KnjigaKnjizara> findByKnjigaId(Long knjigaId) {
        return entityManager.createQuery("SELECT kk FROM KnjigaKnjizara kk WHERE kk.knjiga.id = :kid", KnjigaKnjizara.class)
                 .setParameter("kid", knjigaId).getResultList();
    }
    
    @Transactional
    public List<KnjigaKnjizara> findByKnjigaIdForUpdate(Long knjigaId) {
        var q = entityManager.createQuery("""
            select kk from KnjigaKnjizara kk
            join fetch kk.knjiga k
            left join fetch kk.knjizara j
            where k.id = :kid
            order by kk.kolicina desc
        """, KnjigaKnjizara.class);
        q.setParameter("kid", knjigaId);
        q.setLockMode(LockModeType.PESSIMISTIC_WRITE); // ← ključ za konkurenciju
        return q.getResultList();
    }

    
    
}