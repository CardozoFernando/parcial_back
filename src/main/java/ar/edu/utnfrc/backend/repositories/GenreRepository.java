package ar.edu.utnfrc.backend.repositories;

import ar.edu.utnfrc.backend.entities.Genre;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GenreRepository extends Repository<Genre, Integer> {
    
    @Override
    public Genre getById(Integer id) {
        return manager.find(Genre.class, id);
    }
    
    @Override
    public Set<Genre> getAll() {
        TypedQuery<Genre> query = manager.createQuery("SELECT g FROM Genre g", Genre.class);
        return query.getResultList().stream().collect(Collectors.toSet());
    }
    
    @Override
    public Stream<Genre> getAllStream() {
        TypedQuery<Genre> query = manager.createQuery("SELECT g FROM Genre g", Genre.class);
        return query.getResultList().stream();
    }
    
    // ========== MÉTODOS ESPECÍFICOS ==========
    
    /**
     * Busca un género por su nombre exacto (case-sensitive).
     * 
     * @param name el nombre del género a buscar
     * @return el género encontrado o null si no existe
     */
    public Genre findByName(String name) {
        TypedQuery<Genre> query = manager.createQuery(
                "SELECT g FROM Genre g WHERE g.name = :name", 
                Genre.class);
        query.setParameter("name", name);
        try {
            return query.getSingleResult();
        } catch (jakarta.persistence.NoResultException e) {
            return null;
        }
    }
    
    /**
     * Obtiene los géneros con mayor precio promedio (top N) excluyendo tracks de video.
     * Retorna una lista de Object[] donde:
     * [0] = Genre
     * [1] = Double (precio promedio)
     * [2] = Long (cantidad de tracks)
     * 
     * @param limit número máximo de géneros a retornar
     * @return lista de arrays con género, precio promedio y cantidad de tracks
     */
    @SuppressWarnings("unchecked")
    public List<Object[]> findGenresByAveragePrice(int limit) {
        String jpql = "SELECT g, AVG(t.unitPrice) as avgPrice, COUNT(t) as trackCount " +
                      "FROM Genre g JOIN g.tracks t " +
                      "WHERE t.mediaType.name != 'Protected MPEG-4 video file' " +
                      "GROUP BY g.id, g.name " +
                      "HAVING COUNT(t) > 0 " +
                      "ORDER BY avgPrice DESC";
        
        jakarta.persistence.Query query = manager.createQuery(jpql);
        query.setMaxResults(limit);
        return query.getResultList();
    }
}

