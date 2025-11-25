package ar.edu.utnfrc.backend.services;

import ar.edu.utnfrc.backend.entities.Album;
import ar.edu.utnfrc.backend.repositories.AlbumRepository;
import ar.edu.utnfrc.backend.services.interfaces.IService;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class AlbumService implements IService<Album, Integer> {
    
    private AlbumRepository repository;
    
    public AlbumService() {
        this.repository = new AlbumRepository();
    }
    
    @Override
    public void create(Album entity) {
        validateEntity(entity);
        repository.add(entity);
    }
    
    @Override
    public void update(Album entity) {
        validateEntity(entity);
        repository.update(entity);
    }
    
    @Override
    public void delete(Integer id) {
        Album entity = repository.getById(id);
        if (entity == null) {
            throw new IllegalArgumentException("Álbum no encontrado con ID: " + id);
        }
        repository.delete(id);
    }
    
    @Override
    public Album getById(Integer id) {
        Album entity = repository.getById(id);
        if (entity == null) {
            throw new IllegalArgumentException("Álbum no encontrado con ID: " + id);
        }
        return entity;
    }
    
    @Override
    public Set<Album> getAll() {
        return repository.getAll();
    }
    
    @Override
    public Stream<Album> getAllStream() {
        return repository.getAllStream();
    }
    
    private void validateEntity(Album entity) {
        if (entity.getTitle() == null || entity.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("El título del álbum no puede estar vacío");
        }
        if (entity.getArtist() == null) {
            throw new IllegalArgumentException("El álbum debe tener un artista asignado");
        }
    }
    
    // ========== MÉTODOS ESPECÍFICOS ==========
    
    public List<Album> searchByTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("El título de búsqueda no puede estar vacío");
        }
        return repository.findByTitleContaining(title);
    }
    
    public List<Album> getAlbumsByArtist(Integer artistId) {
        if (artistId == null || artistId <= 0) {
            throw new IllegalArgumentException("ID de artista inválido");
        }
        return repository.findByArtistId(artistId);
    }
}