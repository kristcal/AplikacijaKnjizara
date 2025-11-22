package njt.mavenproject2.servis;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

import njt.mavenproject2.dto.impl.PorudzbinaDto;
import njt.mavenproject2.entity.impl.Knjiga;
import njt.mavenproject2.entity.impl.Korisnik;
import njt.mavenproject2.entity.impl.Porudzbina;
import njt.mavenproject2.entity.impl.StavkaPorudzbine;
import njt.mavenproject2.mapper.impl.PorudzbinaMapper;
import njt.mavenproject2.repository.impl.KnjigaRepository;
import njt.mavenproject2.repository.impl.KorisnikRepository;
import njt.mavenproject2.repository.impl.PorudzbinaRepository;
import org.springframework.stereotype.Service;
import njt.mavenproject2.repository.impl.KnjigaKnjizaraRepository;

@Service
public class PorudzbinaServis {

    private final PorudzbinaRepository repo;
    private final KorisnikRepository korisnikRepo;
    private final KnjigaRepository knjigaRepo;
    private final PorudzbinaMapper mapper;
    private final KnjigaKnjizaraRepository kkRepo;

    public PorudzbinaServis(
            PorudzbinaRepository repo,
            KorisnikRepository korisnikRepo,
            KnjigaRepository knjigaRepo,
            PorudzbinaMapper mapper,
            KnjigaKnjizaraRepository kkRepo
    ) {
        this.repo = repo;
        this.korisnikRepo = korisnikRepo;
        this.knjigaRepo = knjigaRepo;
        this.mapper = mapper;
        this.kkRepo = kkRepo;
    }

    public List<PorudzbinaDto> findAll() {
        return repo.findAll().stream().map(mapper::toDo).collect(Collectors.toList());
    }
    
    
    public PorudzbinaDto findById(Long id) throws Exception {
        return mapper.toDo(repo.findById(id));
    }

    /**
     * CREATE: server postavlja datum; stavke dolaze iz DTO-a; ukupan iznos se
     * računa iz stavki
     */
    @Transactional
    public PorudzbinaDto create(PorudzbinaDto dto) throws Exception {
        if (dto.getKorisnikId() == null || dto.getStavke() == null || dto.getStavke().isEmpty()) {
            throw new IllegalArgumentException("korisnik_id i stavke su obavezni");
        }

        Korisnik k = korisnikRepo.findById(dto.getKorisnikId());
        if (k == null) {
            throw new Exception("Korisnik ne postoji");
        }

        Porudzbina p = new Porudzbina();
        p.setKorisnik(k);
        p.setDatum(java.time.LocalDateTime.now());
        p.setUkupanIznos(0d);

        double suma = 0d;
        for (var s : dto.getStavke()) {
            Knjiga knj = knjigaRepo.findById(s.getKnjigaId());
            if (knj == null) {
                throw new Exception("Knjiga ne postoji: id=" + s.getKnjigaId());
            }

            StavkaPorudzbine ps = new StavkaPorudzbine();
            ps.setPorudzbina(p);
            ps.setKnjiga(knj);
            ps.setKolicina(s.getKolicina() != null ? s.getKolicina() : 1);
            ps.setCenaK(s.getCena() != null ? s.getCena() : knj.getCena());

            p.getStavke().add(ps);

            suma += ps.getCenaK() * ps.getKolicina();
        }
        p.setUkupanIznos(suma);

        repo.save(p);
        return mapper.toDo(p);
    }

    /**
     * UPDATE: ne setuje datum iz DTO-a; možeš promeniti korisnika i/ili
     * kompletno zameniti stavke
     */
    @Transactional
    public PorudzbinaDto update(Long id, PorudzbinaDto dto) throws Exception {
        Porudzbina p = repo.findById(id);
        if (p == null) {
            throw new Exception("Porudžbina ne postoji");
        }

        // Datum je server-controlled -> NE uzimamo iz DTO-a.
        if (dto.getKorisnikId() != null) {
            Korisnik k = korisnikRepo.findById(dto.getKorisnikId());
            if (k == null) {
                throw new Exception("Korisnik ne postoji");
            }
            p.setKorisnik(k);
        }

        if (dto.getStavke() != null) {
            p.getStavke().clear();
            double suma = 0d;

            for (var s : dto.getStavke()) {
                Knjiga knj = knjigaRepo.findById(s.getKnjigaId());
                if (knj == null) {
                    throw new Exception("Knjiga ne postoji: id=" + s.getKnjigaId());
                }

                StavkaPorudzbine ps = new StavkaPorudzbine();
                ps.setPorudzbina(p);
                ps.setKnjiga(knj);
                ps.setKolicina(s.getKolicina() != null ? s.getKolicina() : 1);
                ps.setCenaK(s.getCena() != null ? s.getCena() : knj.getCena());

                p.getStavke().add(ps);
                suma += ps.getCenaK() * ps.getKolicina();
            }
            p.setUkupanIznos(suma);
        }

        repo.save(p);
        return mapper.toDo(p);
    }

    @Transactional
    public void deleteById(Long id) {
        repo.deleteById(id);
    }

    public List<PorudzbinaDto> findByKorisnik(Long korisnikId) {
        return repo.findByKorisnikId(korisnikId).stream()
                .map(mapper::toDo) // ili toRes ako koristiš bogatiji odgovor sa stavkama
                .collect(Collectors.toList());
    }

    @Transactional
    public PorudzbinaDto promeniStatus(Long id, String noviStatus) throws Exception {
        Porudzbina p = repo.findById(id);
        String stari = p.getStatus();

        // normalizuj ulaz (trim, upper)
        String target = (noviStatus == null ? "" : noviStatus.trim()).toUpperCase();

        if (stari != null && stari.equalsIgnoreCase("OBRADJENA") && target.equals("OBRADJENA")) {
            // već obrađena → ništa ne diramo (idempotentno)
            return mapper.toDo(p);
        }

        // Ako sada postavljamo na OBRADJENA → skini zalihe
        if (target.equals("OBRADJENA")) {
            skiniZaliheZa(p); // ← ključna linija
        }

        p.setStatus(target);
        repo.save(p);
        return mapper.toDo(p);
    }

    @Transactional
    protected void skiniZaliheZa(Porudzbina p) throws Exception {
        if (p.getStavke() == null || p.getStavke().isEmpty()) {
            return;
        }

        for (StavkaPorudzbine s : p.getStavke()) {
            if (s.getKnjiga() == null) {
                continue;
            }

            int potrebno = s.getKolicina() == null ? 0 : s.getKolicina();
            if (potrebno <= 0) {
                continue;
            }

            // Zaključa redove (ili red) zalihe za tu knjigu
            var redovi = kkRepo.findByKnjigaIdForUpdate(s.getKnjiga().getId());
            int skinuto = 0;

            for (var kk : redovi) {
                int naStanju = kk.getKolicina() == null ? 0 : kk.getKolicina();
                if (naStanju <= 0) {
                    continue;
                }

                int uzmi = Math.min(potrebno - skinuto, naStanju);
                if (uzmi <= 0) {
                    break;
                }

                kk.setKolicina(naStanju - uzmi);
                kkRepo.save(kk);

                skinuto += uzmi;
                if (skinuto >= potrebno) {
                    break;
                }
            }

            if (skinuto < potrebno) {
                throw new Exception("Nema dovoljno na stanju za: " + s.getKnjiga().getNaziv());
            }
        }
    }
    
    

}
