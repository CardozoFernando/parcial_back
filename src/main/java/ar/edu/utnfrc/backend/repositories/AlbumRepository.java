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
    
    /**
     * Busca un álbum por su título exacto y artista (case-sensitive).
     * 
     * @param title el título del álbum a buscar
     * @param artistId el ID del artista
     * @return el álbum encontrado o null si no existe
     */
    public Album findByTitleAndArtist(String title, Integer artistId) {
        TypedQuery<Album> query = manager.createQuery(
                "SELECT a FROM Album a WHERE a.title = :title AND a.artist.id = :artistId", 
                Album.class);
        query.setParameter("title", title);
        query.setParameter("artistId", artistId);
        try {
            return query.getSingleResult();
        } catch (jakarta.persistence.NoResultException e) {
            return null;
        }
    }
    
    /**
     * Obtiene los álbumes más largos (top N) excluyendo tracks de video.
     * Retorna una lista de Object[] donde:
     * [0] = Album
     * [1] = Long (duración total en milliseconds)
     * 
     * @param limit número máximo de álbumes a retornar
     * @return lista de arrays con álbum y duración total
     */
    @SuppressWarnings("unchecked")
    public List<Object[]> findLongestAlbums(int limit) {
        String jpql = "SELECT a, SUM(t.milliseconds) as totalDuration " +
                      "FROM Album a JOIN a.tracks t " +
                      "WHERE t.mediaType.name != 'Protected MPEG-4 video file' " +
                      "GROUP BY a.id, a.title, a.artist.id " +
                      "ORDER BY totalDuration DESC";
        
        jakarta.persistence.Query query = manager.createQuery(jpql);
        query.setMaxResults(limit);
        return query.getResultList();
    }
}