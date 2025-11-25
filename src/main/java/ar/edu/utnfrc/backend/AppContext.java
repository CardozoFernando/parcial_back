package ar.edu.utnfrc.backend;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Contexto de aplicación que permite almacenar y compartir objetos y servicios
 * de forma global en la aplicación usando el patrón Singleton.
 * 
 * Este contexto es thread-safe y permite:
 * - Almacenar objetos con claves String
 * - Registrar y obtener servicios por tipo
 * - Compartir instancias entre diferentes partes de la aplicación
 */
public class AppContext {

    // Mapa para almacenar los objetos (thread-safe)
    private final ConcurrentHashMap<String, Object> contextMap;

    // Constructor privado
    private AppContext() {
        contextMap = new ConcurrentHashMap<>();
    }

    // Singleton - Lazy Holder (patrón de inicialización perezosa thread-safe)
    private static class Holder {
        // La instancia estática, se crea solo cuando se necesita.
        private static final AppContext INSTANCE = new AppContext();
    }

    /**
     * Obtiene la instancia única del contexto de aplicación (Singleton).
     * 
     * @return la instancia única de AppContext
     */
    public static AppContext getInstance() {
        return Holder.INSTANCE;
    }

    /**
     * Inserta o actualiza un valor en el contexto.
     * 
     * @param key la clave para almacenar el valor
     * @param value el valor a almacenar
     */
    public void put(String key, Object value) {
        contextMap.put(key, value);
    }

    /**
     * Recupera el valor bruto asociado con la clave.
     * 
     * @param key la clave del valor a recuperar
     * @return el valor asociado con la clave, o null si no existe
     */
    public Object get(String key) {
        return contextMap.get(key);
    }

    /**
     * Recupera el valor con un tipo específico (con casting seguro).
     * 
     * @param <T> el tipo del valor a recuperar
     * @param key la clave del valor a recuperar
     * @param type la clase del tipo esperado
     * @return el valor casteado al tipo especificado
     * @throws ClassCastException si el valor no es del tipo esperado
     */
    public <T> T get(String key, Class<T> type) {
        return type.cast(contextMap.get(key));
    }

    /**
     * Elimina el valor asociado con la clave.
     * 
     * @param key la clave del valor a eliminar
     * @return el valor eliminado, o null si no existía
     */
    public Object remove(String key) {
        return contextMap.remove(key);
    }

    /**
     * Verifica si existe una clave en el contexto.
     * 
     * @param key la clave a verificar
     * @return true si la clave existe, false en caso contrario
     */
    public boolean contains(String key) {
        return contextMap.containsKey(key);
    }

    /**
     * Reemplaza un valor solo si la clave existe.
     * 
     * @param key la clave del valor a reemplazar
     * @param newValue el nuevo valor
     * @throws IllegalArgumentException si la clave no existe en el contexto
     */
    public void set(String key, Object newValue) {
        if (contextMap.containsKey(key)) {
            contextMap.put(key, newValue);
        } else {
            throw new IllegalArgumentException("La clave no existe en el contexto: " + key);
        }
    }

    /**
     * Registra un servicio en el contexto usando su clase como clave.
     * 
     * @param <S> el tipo del servicio
     * @param serviceClass la clase del servicio
     * @param service la instancia del servicio a registrar
     */
    public <S> void registerService(Class<S> serviceClass, S service) {
        contextMap.put(serviceClass.getName(), service);
    }

    /**
     * Obtiene un servicio del contexto por su clase.
     * 
     * @param <S> el tipo del servicio
     * @param serviceClass la clase del servicio a obtener
     * @return la instancia del servicio, o null si no está registrado
     */
    @SuppressWarnings("unchecked")
    public <S> S getService(Class<S> serviceClass) {
        return (S) contextMap.get(serviceClass.getName());
    }

    /**
     * Limpia todo el contexto, eliminando todos los objetos almacenados.
     */
    public void clear() {
        contextMap.clear();
    }

    /**
     * Obtiene el número de elementos almacenados en el contexto.
     * 
     * @return el número de elementos en el contexto
     */
    public int size() {
        return contextMap.size();
    }
}

