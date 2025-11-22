package njt.mavenproject2.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import njt.mavenproject2.entity.impl.Korisnik;
import njt.mavenproject2.repository.MyAppRepository;
import org.springframework.stereotype.Repository;

@Repository
public class KorisnikRepository implements MyAppRepository<Korisnik, Long> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Korisnik> findAll() {
        return entityManager.createQuery("SELECT k FROM Korisnik k", Korisnik.class).getResultList();
    }

    @Override
    public Korisnik findById(Long id) throws Exception {
        Korisnik korisnik = entityManager.find(Korisnik.class, id);
        if (korisnik == null) throw new Exception("Korisnik nije pronađen!");
        return korisnik;
    }

    @Transactional
    @Override
    public void save(Korisnik entity) {
        if (entity.getId() == null) entityManager.persist(entity);
        else entityManager.merge(entity);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        Korisnik korisnik = entityManager.find(Korisnik.class, id);
        if (korisnik != null) entityManager.remove(korisnik);
    }

    // --- DODATO: nalazi korisnika po email-u (za login) ---
    public Korisnik findByEmail(String email) {
        try {
            return entityManager.createQuery(
                    "SELECT k FROM Korisnik k WHERE LOWER(k.email) = LOWER(:email)",
                    Korisnik.class
            ).setParameter("email", email).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
