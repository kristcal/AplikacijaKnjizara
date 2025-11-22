/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package njt.mavenproject2.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import njt.mavenproject2.entity.impl.Knjiga;
import njt.mavenproject2.repository.MyAppRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Korisnik
 */
@Repository
public class KnjigaRepository implements MyAppRepository<Knjiga, Long>{
    
    @PersistenceContext
    private EntityManager entityManager;
    
    
    @Override
    public List<Knjiga> findAll() {
        return entityManager.createQuery("SELECT k FROM Knjiga k", Knjiga.class).getResultList();
    }

    @Override
    public Knjiga findById(Long id) throws Exception {
        Knjiga knjiga = entityManager.find(Knjiga.class, id);
        if(knjiga == null){
            throw new Exception("Knjiga nije pronadjena!");
        }
        return knjiga;
    }

    @Override
    public void save(Knjiga entity) {
        if(entity.getId() == null){
            entityManager.persist(entity);
        }else{
            entityManager.merge(entity);
        }
    }

    @Override
    public void deleteById(Long id) {
        Knjiga knjiga = entityManager.find(Knjiga.class, id);
        if(knjiga != null){
            entityManager.remove(knjiga);
        }
    }
    
    public List<Knjiga> findByGenre(Long zanrId) {
        return entityManager.createQuery("SELECT k FROM Knjiga k WHERE k.zanr.id = :z ORDER BY k.id DESC", Knjiga.class)
                 .setParameter("z", zanrId)
                 .getResultList();
    }

    public List<Knjiga> findCheaperThan(Double max) {
        return entityManager.createQuery("SELECT k FROM Knjiga k WHERE k.cena <= :m ORDER BY k.cena ASC", Knjiga.class)
                 .setParameter("m", max)
                 .getResultList();
    }

    /**
     * Slobodna pretraga: naziv ili ISBN + opciono filter žanra i maksimalne cene.
     */
    public List<Knjiga> search(String q, Long zanrId, Double max) {
        String base = """
            SELECT k FROM Knjiga k
            WHERE (LOWER(k.naziv) LIKE LOWER(CONCAT('%', :q, '%'))
                   OR LOWER(k.isbn) LIKE LOWER(CONCAT('%', :q, '%')))
        """;
        String andGenre = (zanrId != null) ? " AND k.zanr.id = :z " : "";
        String andMax   = (max != null)    ? " AND k.cena <= :m "   : "";
        var query = entityManager.createQuery(base + andGenre + andMax + " ORDER BY k.id DESC", Knjiga.class)
                      .setParameter("q", q);
        if (zanrId != null) query.setParameter("z", zanrId);
        if (max != null)    query.setParameter("m", max);
        return query.getResultList();
    }
    public Knjiga findOneFull(Long id) throws Exception {
    String jpql = """
        select distinct k
        from Knjiga k
        left join fetch k.zanr z
        left join fetch k.autori ka
        left join fetch ka.autor a
        where k.id = :id
    """;
    List<Knjiga> list = entityManager
            .createQuery(jpql, Knjiga.class)
            .setParameter("id", id)
            .getResultList();
    if (list.isEmpty()) throw new Exception("Knjiga nije pronađena!");
    return list.get(0);
}
}
