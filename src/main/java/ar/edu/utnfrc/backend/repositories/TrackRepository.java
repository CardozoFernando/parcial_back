package ar.edu.utnfrc.backend.repositories;

import ar.edu.utnfrc.backend.entities.Track;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

public class TrackRepository {
    
    private EntityManager em;
    
    public TrackRepository(EntityManager em) {
        this.em = em;
    }
    
    public Optional<Track> findById(Integer id) {
        return Optional.ofNullable(em.find(Track.class, id));
    }
    
    public List<Track> findAll() {
        TypedQuery<Track> query = em.createQuery("SELECT t FROM Track t", Track.class);
        return query.getResultList();
    }
    
    public List<Track> findByNameContaining(String name) {
        TypedQuery<Track> query = em.createQuery(
                "SELECT t FROM Track t WHERE LOWER(t.name) LIKE LOWER(:name)", 
                Track.class);
        query.setParameter("name", "%" + name + "%");
        return query.getResultList();
    }
    
    public List<Track> findByAlbumId(Integer albumId) {
        TypedQuery<Track> query = em.createQuery(
                "SELECT t FROM Track t WHERE t.album.id = :albumId", 
                Track.class);
        query.setParameter("albumId", albumId);
        return query.getResultList();
    }
    
    public List<Track> findByGenreId(Integer genreId) {
        TypedQuery<Track> query = em.createQuery(
                "SELECT t FROM Track t WHERE t.genre.id = :genreId", 
                Track.class);
        query.setParameter("genreId", genreId);
        return query.getResultList();
    }
    
    public void save(Track track) {
        if (track.getId() == null) {
            em.persist(track);
        } else {
            em.merge(track);
        }
    }
}