package ar.edu.utnfrc.backend.repositories;

import ar.edu.utnfrc.backend.entities.Album;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

public class AlbumRepository {
    
    private EntityManager em;
    
    public AlbumRepository(EntityManager em) {
        this.em = em;
    }
    
    public Optional<Album> findById(Integer id) {
        return Optional.ofNullable(em.find(Album.class, id));
    }
    
    public List<Album> findAll() {
        TypedQuery<Album> query = em.createQuery("SELECT a FROM Album a", Album.class);
        return query.getResultList();
    }
    
    public List<Album> findByTitleContaining(String title) {
        TypedQuery<Album> query = em.createQuery(
                "SELECT a FROM Album a WHERE LOWER(a.title) LIKE LOWER(:title)", 
                Album.class);
        query.setParameter("title", "%" + title + "%");
        return query.getResultList();
    }
    
    public List<Album> findByArtistId(Integer artistId) {
        TypedQuery<Album> query = em.createQuery(
                "SELECT a FROM Album a WHERE a.artist.id = :artistId", 
                Album.class);
        query.setParameter("artistId", artistId);
        return query.getResultList();
    }
    
    public void save(Album album) {
        if (album.getId() == null) {
            em.persist(album);
        } else {
            em.merge(album);
        }
    }
}