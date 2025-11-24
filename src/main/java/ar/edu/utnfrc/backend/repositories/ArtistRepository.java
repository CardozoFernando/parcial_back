package ar.edu.utnfrc.backend.repositories;

import ar.edu.utnfrc.backend.entities.Artist;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

public class ArtistRepository {
    
    private EntityManager em;
    
    public ArtistRepository(EntityManager em) {
        this.em = em;
    }
    
    public Optional<Artist> findById(Integer id) {
        return Optional.ofNullable(em.find(Artist.class, id));
    }
    
    public List<Artist> findAll() {
        TypedQuery<Artist> query = em.createQuery("SELECT a FROM Artist a", Artist.class);
        return query.getResultList();
    }
    
    public List<Artist> findByNameContaining(String name) {
        TypedQuery<Artist> query = em.createQuery(
                "SELECT a FROM Artist a WHERE LOWER(a.name) LIKE LOWER(:name)", 
                Artist.class);
        query.setParameter("name", "%" + name + "%");
        return query.getResultList();
    }
    
    public void save(Artist artist) {
        if (artist.getId() == null) {
            em.persist(artist);
        } else {
            em.merge(artist);
        }
    }
}