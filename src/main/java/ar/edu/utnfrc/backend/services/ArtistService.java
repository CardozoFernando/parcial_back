package ar.edu.utnfrc.backend.services;

import ar.edu.utnfrc.backend.entities.Artist;
import ar.edu.utnfrc.backend.repositories.ArtistRepository;
import ar.edu.utnfrc.backend.services.interfaces.IService;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class ArtistService implements IService<Artist, Integer> {
    
    private ArtistRepository repository;
    
    public ArtistService() {
        this.repository = new ArtistRepository();
    }
    
    @Override
    public void create(Artist entity) {
        validateEntity(entity);
        repository.add(entity);
    }
    
    @Override
    public void update(Artist entity) {
        validateEntity(entity);
        repository.update(entity);
    }
    
    @Override
    public void delete(Integer id) {
        Artist entity = repository.getById(id);
        if (entity == null) {
            throw new IllegalArgumentException("Artista no encontrado con ID: " + id);
        }
        repository.delete(id);
    }
    
    @Override
    public Artist getById(Integer id) {
        Artist entity = repository.getById(id);
        if (entity == null) {
            throw new IllegalArgumentException("Artista no encontrado con ID: " + id);
        }
        return entity;
    }
    
    @Override
    public Set<Artist> getAll() {
        return repository.getAll();
    }
    
    @Override
    public Stream<Artist> getAllStream() {
        return repository.getAllStream();
    }
    
    private void validateEntity(Artist entity) {
        if (entity.getName() == null || entity.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del artista no puede estar vacío");
        }
    }
    
    // ========== MÉTODOS ESPECÍFICOS ==========
    
    public List<Artist> searchByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de búsqueda no puede estar vacío");
        }
        return repository.findByNameContaining(name);
    }
}