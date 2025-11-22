/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package njt.mavenproject2.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import njt.mavenproject2.entity.impl.Porudzbina;
import njt.mavenproject2.repository.MyAppRepository;

@Repository
public class PorudzbinaRepository implements MyAppRepository<Porudzbina, Long> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Porudzbina> findAll() {
        return entityManager
                .createQuery("SELECT p FROM Porudzbina p ORDER BY p.datum DESC", Porudzbina.class)
                .getResultList();
    }

    @Override
    public Porudzbina findById(Long id) throws Exception {
        Porudzbina p = entityManager.find(Porudzbina.class, id);
        if (p == null) throw new Exception("Porudžbina nije pronađena!");
        return p;
    }

    @Override
    @Transactional
    public void save(Porudzbina entity) {
        if (entity.getId() == null) entityManager.persist(entity);
        else entityManager.merge(entity);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Porudzbina p = entityManager.find(Porudzbina.class, id);
        if (p != null) entityManager.remove(p);
    }

    public List<Porudzbina> findByKorisnikId(Long korisnikId) {
        return entityManager.createQuery(
                "SELECT p FROM Porudzbina p WHERE p.korisnik.id = :kid ORDER BY p.datum DESC",
                Porudzbina.class
        ).setParameter("kid", korisnikId).getResultList();
    }

    /** Detalj sa stavkama i knjigom – bez N+1 upita. */
    @Transactional(readOnly = true)
    public Porudzbina findOneFull(Long id) throws Exception {
        List<Porudzbina> list = entityManager.createQuery("""
            select distinct p from Porudzbina p
            left join fetch p.stavke s
            left join fetch s.knjiga k
            where p.id = :id
        """, Porudzbina.class)
        .setParameter("id", id)
        .getResultList();

        if (list.isEmpty()) throw new Exception("Porudžbina ne postoji");
        return list.get(0);
    }
}
