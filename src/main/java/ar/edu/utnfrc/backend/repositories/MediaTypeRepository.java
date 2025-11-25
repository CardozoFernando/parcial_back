package ar.edu.utnfrc.backend.repositories;

import ar.edu.utnfrc.backend.entities.MediaType;
import jakarta.persistence.TypedQuery;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MediaTypeRepository extends Repository<MediaType, Integer> {
    
    @Override
    public MediaType getById(Integer id) {
        return manager.find(MediaType.class, id);
    }
    
    @Override
    public Set<MediaType> getAll() {
        TypedQuery<MediaType> query = manager.createQuery("SELECT m FROM MediaType m", MediaType.class);
        return query.getResultList().stream().collect(Collectors.toSet());
    }
    
    @Override
    public Stream<MediaType> getAllStream() {
        TypedQuery<MediaType> query = manager.createQuery("SELECT m FROM MediaType m", MediaType.class);
        return query.getResultList().stream();
    }
    
    // ========== MÉTODOS ESPECÍFICOS ==========
    
    /**
     * Busca un tipo de media por su nombre exacto (case-sensitive).
     * 
     * @param name el nombre del tipo de media a buscar
     * @return el tipo de media encontrado o null si no existe
     */
    public MediaType findByName(String name) {
        TypedQuery<MediaType> query = manager.createQuery(
                "SELECT m FROM MediaType m WHERE m.name = :name", 
                MediaType.class);
        query.setParameter("name", name);
        try {
            return query.getSingleResult();
        } catch (jakarta.persistence.NoResultException e) {
            return null;
        }
    }
}

