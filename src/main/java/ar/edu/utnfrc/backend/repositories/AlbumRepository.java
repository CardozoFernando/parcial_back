package ar.edu.utnfrc.backend.repositories;

import ar.edu.utnfrc.backend.entities.Album;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AlbumRepository extends Repository<Album, Integer> {
    
    @Override
    public Album getById(Integer id) {
        return manager.find(Album.class, id);
    }
    
    @Override
    public Set<Album> getAll() {
        TypedQuery<Album> query = manager.createQuery("SELECT a FROM Album a", Album.class);
        return query.getResultList().stream().collect(Collectors.toSet());
    }
    
    @Override
    public Stream<Album> getAllStream() {
        TypedQuery<Album> query = manager.createQuery("SELECT a FROM Album a", Album.class);
        return query.getResultList().stream();
    }
    
    // ========== MÉTODOS ESPECÍFICOS ==========
    
    public List<Album> findByTitleContaining(String title) {
        TypedQuery<Album> query = manager.createQuery(
                "SELECT a FROM Album a WHERE LOWER(a.title) LIKE LOWER(:title)", 
                Album.class);
        query.setParameter("title", "%" + title + "%");
        return query.getResultList();
    }
    
    public List<Album> findByArtistId(Integer artistId) {
        TypedQuery<Album> query = manager.createQuery(
                "SELECT a FROM Album a WHERE a.artist.id = :artistId", 
                Album.class);
        query.setParameter("artistId", artistId);
        return query.getResultList();
    }
}