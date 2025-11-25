package ar.edu.utnfrc.backend.services;

import ar.edu.utnfrc.backend.entities.Track;
import ar.edu.utnfrc.backend.repositories.TrackRepository;
import ar.edu.utnfrc.backend.services.interfaces.IService;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class TrackService implements IService<Track, Integer> {
    
    private TrackRepository repository;
    
    public TrackService() {
        this.repository = new TrackRepository();
    }
    
    @Override
    public void create(Track entity) {
        validateEntity(entity);
        repository.add(entity);
    }
    
    @Override
    public void update(Track entity) {
        validateEntity(entity);
        repository.update(entity);
    }
    
    @Override
    public void delete(Integer id) {
        Track entity = repository.getById(id);
        if (entity == null) {
            throw new IllegalArgumentException("Track no encontrado con ID: " + id);
        }
        repository.delete(id);
    }
    
    @Override
    public Track getById(Integer id) {
        Track entity = repository.getById(id);
        if (entity == null) {
            throw new IllegalArgumentException("Track no encontrado con ID: " + id);
        }
        return entity;
    }
    
    @Override
    public Set<Track> getAll() {
        return repository.getAll();
    }
    
    @Override
    public Stream<Track> getAllStream() {
        return repository.getAllStream();
    }
    
    private void validateEntity(Track entity) {
        if (entity.getName() == null || entity.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del track no puede estar vacío");
        }
        if (entity.getAlbum() == null) {
            throw new IllegalArgumentException("El track debe tener un álbum asignado");
        }
        if (entity.getUnitPrice() == null || entity.getUnitPrice() < 0) {
            throw new IllegalArgumentException("El precio debe ser válido");
        }
    }
    
    // ========== MÉTODOS ESPECÍFICOS ==========
    
    public List<Track> searchByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de búsqueda no puede estar vacío");
        }
        return repository.findByNameContaining(name);
    }
    
    public List<Track> getTracksByAlbum(Integer albumId) {
        if (albumId == null || albumId <= 0) {
            throw new IllegalArgumentException("ID de álbum inválido");
        }
        return repository.findByAlbumId(albumId);
    }
    
    public List<Track> getTracksByGenre(Integer genreId) {
        if (genreId == null || genreId <= 0) {
            throw new IllegalArgumentException("ID de género inválido");
        }
        return repository.findByGenreId(genreId);
    }
}