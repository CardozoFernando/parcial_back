package ar.edu.utnfrc.backend.services.interfaces;

import java.util.Set;
import java.util.stream.Stream;

public interface IService<T, K> {
    
    void create(T entity);
    
    void update(T entity);
    
    void delete(K id);
    
    T getById(K id);
    
    Set<T> getAll();
    
    Stream<T> getAllStream();
}