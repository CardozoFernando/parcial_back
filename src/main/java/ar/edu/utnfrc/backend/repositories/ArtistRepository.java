package ar.edu.utnfrc.backend.repositories;

import ar.edu.utnfrc.backend.entities.Artist;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ArtistRepository extends Repository<Artist, Integer> {
    
    @Override
    public Artist getById(Integer id) {
        return manager.find(Artist.class, id);
    }
    
    @Override
    public Set<Artist> getAll() {
        TypedQuery<Artist> query = manager.createQuery("SELECT a FROM Artist a", Artist.class);
        return query.getResultList().stream().collect(Collectors.toSet());
    }
    
    @Override
    public Stream<Artist> getAllStream() {
        TypedQuery<Artist> query = manager.createQuery("SELECT a FROM Artist a", Artist.class);
        return query.getResultList().stream();
    }
    
    // ========== MÉTODOS ESPECÍFICOS ==========
    
    public List<Artist> findByNameContaining(String name) {
        TypedQuery<Artist> query = manager.createQuery(
                "SELECT a FROM Artist a WHERE LOWER(a.name) LIKE LOWER(:name)", 
                Artist.class);
        query.setParameter("name", "%" + name + "%");
        return query.getResultList();
    }
    
    /**
     * Busca un artista por su nombre exacto (case-sensitive).
     * 
     * @param name el nombre del artista a buscar
     * @return el artista encontrado o null si no existe
     */
    public Artist findByName(String name) {
        TypedQuery<Artist> query = manager.createQuery(
                "SELECT a FROM Artist a WHERE a.name = :name", 
                Artist.class);
        query.setParameter("name", name);
        try {
            return query.getSingleResult();
        } catch (jakarta.persistence.NoResultException e) {
            return null;
        }
    }
}