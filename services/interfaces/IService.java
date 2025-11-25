package enunciado.parcial.services.interfaces;

import java.util.List;

public interface IService<T, K> {

    /**
     * Busca una entidad por su ID, y si no existe la crea.
     */
    T getById(K id);

    /**
     * Busca una entidad por su nombre o descripción, y si no existe la crea.
     */
    T getOrCreateByName(String name);

    /**
     * Verifica si existe una entidad por su ID.
     */
    // boolean existsById(K id);

    /**
     * Listar todos los objetos de esta entidad.
     */
    List<T> getAll();

    // boolean existsByName(String nombre);
}
