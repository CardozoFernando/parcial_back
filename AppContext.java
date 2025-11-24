package enunciado.parcial.app;

import java.util.concurrent.ConcurrentHashMap;

public class AppContext {

    // Mapa para almacenar los objetos.
    private final ConcurrentHashMap<String, Object> contextMap;

    // Constructor privado
    private AppContext() {
        contextMap = new ConcurrentHashMap<>();
    }

    // Singleton - Lazy Holder
    private static class Holder {
        // La instancia estática, se crea solo cuando se necesita.
        private static final AppContext INSTANCE = new AppContext();
    }

    // Obtener la instancia del Singleton
    public static AppContext getInstance() {
        return Holder.INSTANCE;
    }

    // Inserta o actualiza un valor
    public void put(String key, Object value) {
        contextMap.put(key, value);
    }

    // Recupera el valor bruto
    public Object get(String key) {
        return contextMap.get(key);
    }

    // Recupera el valor con un tipo específico
    public <T> T get(String key, Class<T> type) {
        return type.cast(contextMap.get(key));
    }

    // Elimina el valor asociado con la clave
    public void remove(String key) {
        contextMap.remove(key);
    }

    // Verifica si existe la clave
    public boolean contains(String key) {
        return contextMap.containsKey(key);
    }

    // Reemplaza solo si la clave existe
    public void set(String key, Object newValue) {
        if (contextMap.containsKey(key)) {
            contextMap.put(key, newValue);
        } else {
            throw new IllegalArgumentException("La clave no existe en el contexto.");
        }
    }

    public <S> void registerService(Class<S> entityClass, S service) {
        contextMap.put(entityClass.getName(), service);
    }

    @SuppressWarnings("unchecked")
    public <S> S getService(Class<S> serviceClass) {
        return (S) contextMap.get(serviceClass.getName());
    }
}
