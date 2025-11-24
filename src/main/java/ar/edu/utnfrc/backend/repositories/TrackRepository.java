package ar.edu.utnfrc.backend.repositories;

import ar.edu.utnfrc.backend.entities.Track;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TrackRepository extends Repository<Track, Integer> {
    
    @Override
    public Track getById(Integer id) {
        return manager.find(Track.class, id);
    }
    
    @Override
    public Set<Track> getAll() {
        TypedQuery<Track> query = manager.createQuery("SELECT t FROM Track t", Track.class);
        return query.getResultList().stream().collect(Collectors.toSet());
    }
    
    @Override
    public Stream<Track> getAllStream() {
        TypedQuery<Track> query = manager.createQuery("SELECT t FROM Track t", Track.class);
        return query.getResultList().stream();
    }
    
    // ========== MÉTODOS ESPECÍFICOS ==========
    
    public List<Track> findByNameContaining(String name) {
        TypedQuery<Track> query = manager.createQuery(
                "SELECT t FROM Track t WHERE LOWER(t.name) LIKE LOWER(:name)", 
                Track.class);
        query.setParameter("name", "%" + name + "%");
        return query.getResultList();
    }
    
    public List<Track> findByAlbumId(Integer albumId) {
        TypedQuery<Track> query = manager.createQuery(
                "SELECT t FROM Track t WHERE t.album.id = :albumId", 
                Track.class);
        query.setParameter("albumId", albumId);
        return query.getResultList();
    }
    
    public List<Track> findByGenreId(Integer genreId) {
        TypedQuery<Track> query = manager.createQuery(
                "SELECT t FROM Track t WHERE t.genre.id = :genreId", 
                Track.class);
        query.setParameter("genreId", genreId);
        return query.getResultList();
    }
}