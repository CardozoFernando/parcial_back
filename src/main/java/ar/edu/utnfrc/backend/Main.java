package ar.edu.utnfrc.backend;

import ar.edu.utnfrc.backend.infra.DataSourceProvider;
import ar.edu.utnfrc.backend.infra.DbInitializer;
import ar.edu.utnfrc.backend.infra.LocalEntityManagerProvider;
import ar.edu.utnfrc.backend.services.ImportService;
import ar.edu.utnfrc.backend.repositories.GenreRepository;
import ar.edu.utnfrc.backend.entities.Genre;
import ar.edu.utnfrc.backend.repositories.AlbumRepository;
import ar.edu.utnfrc.backend.entities.Album;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class Main {
    
    public static void main(String[] args) {
        try {
            // 1. Inicializar base de datos H2 embebido en memoria
            System.out.println("Inicializando base de datos H2 en memoria...");
            DbInitializer.initialize(DataSourceProvider.getDataSource());
            System.out.println("Base de datos inicializada correctamente.\n");
            
            // 2. Importar datos desde CSV
            System.out.println("Importando datos desde data.csv...");
            ImportService importService = new ImportService();
            importService.importFromCsv();
            System.out.println("Importación completada.\n");
            
            // 3. Generar Informe 1 — Resultado de la importación
            generateInforme1();
            
            // 4. Generar Informe 2 — Listado de Álbumes más largos
            generateInforme2();
            
            // 5. Generar Informe 3 — Ranking de Precio promedio por género
            generateInforme3();
            
            // Cerrar EntityManagerFactory
            LocalEntityManagerProvider.closeFactory();
            
        } catch (Exception e) {
            System.err.println("[ERROR] Fallo en la ejecución:");
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    /**
     * Genera el Informe 1 — Resultado de la importación.
     * Muestra los totales reales de registros en la base de datos usando consultas COUNT directas.
     */
    private static void generateInforme1() {
        EntityManager em = LocalEntityManagerProvider.getEntityManager();
        
        try {
            TypedQuery<Long> artistQuery = em.createQuery("SELECT COUNT(a) FROM Artist a", Long.class);
            long totalArtists = artistQuery.getSingleResult();
            
            TypedQuery<Long> albumQuery = em.createQuery("SELECT COUNT(a) FROM Album a", Long.class);
            long totalAlbums = albumQuery.getSingleResult();
            
            TypedQuery<Long> genreQuery = em.createQuery("SELECT COUNT(g) FROM Genre g", Long.class);
            long totalGenres = genreQuery.getSingleResult();
            
            TypedQuery<Long> mediaTypeQuery = em.createQuery("SELECT COUNT(m) FROM MediaType m", Long.class);
            long totalMediaTypes = mediaTypeQuery.getSingleResult();
            
            TypedQuery<Long> trackQuery = em.createQuery("SELECT COUNT(t) FROM Track t", Long.class);
            long totalTracks = trackQuery.getSingleResult();
            
            System.out.println("\n========================================");
            System.out.println("Informe 1 — Resultado de la importación");
            System.out.println("========================================");
            System.out.println("Total de artistas insertados: " + totalArtists);
            System.out.println("Total de álbumes insertados: " + totalAlbums);
            System.out.println("Total de géneros insertados: " + totalGenres);
            System.out.println("Total de media types insertados: " + totalMediaTypes);
            System.out.println("Total de tracks insertados: " + totalTracks);
            System.out.println("========================================\n");
        } finally {
            em.close();
        }
    }
    
    /**
     * Genera el Informe 2 — Listado de Álbumes más largos.
     * Muestra los 10 álbumes más largos excluyendo tracks de video.
     */
    private static void generateInforme2() {
        AlbumRepository albumRepository = new AlbumRepository();
        List<Object[]> longestAlbums = albumRepository.findLongestAlbums(10);
        
        System.out.println("\n========================================");
        System.out.println("Informe 2 — Listado de Álbumes más largos");
        System.out.println("========================================");
        
        if (longestAlbums.isEmpty()) {
            System.out.println("No se encontraron álbumes.");
        } else {
            int position = 1;
            for (Object[] result : longestAlbums) {
                Album album = (Album) result[0];
                Long totalMilliseconds = (Long) result[1];
                
                String albumTitle = album.getTitle();
                String artistName = album.getArtist() != null ? album.getArtist().getName() : "Desconocido";
                String duration = formatDuration(totalMilliseconds);
                
                System.out.println(position + ". " + albumTitle + " - " + artistName);
                System.out.println("   Duración total: " + duration);
                position++;
            }
        }
        
        System.out.println("========================================\n");
    }
    
    /**
     * Formatea una duración en milliseconds a formato minutos:segundos.
     * Trunca la parte decimal de los segundos.
     * 
     * @param milliseconds duración en milliseconds
     * @return string formateado como "MM:SS"
     */
    private static String formatDuration(Long milliseconds) {
        if (milliseconds == null || milliseconds <= 0) {
            return "0:00";
        }
        
        long totalSeconds = milliseconds / 1000;
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;
        
        return String.format("%d:%02d", minutes, seconds);
    }
    
    /**
     * Genera el Informe 3 — Ranking de Precio promedio por género.
     * Muestra los 5 géneros con mayor precio promedio excluyendo tracks de video.
     */
    private static void generateInforme3() {
        GenreRepository genreRepository = new GenreRepository();
        List<Object[]> genresByPrice = genreRepository.findGenresByAveragePrice(5);
        
        System.out.println("\n========================================");
        System.out.println("Informe 3 — Ranking de Precio promedio por género");
        System.out.println("========================================");
        
        if (genresByPrice.isEmpty()) {
            System.out.println("No se encontraron géneros.");
        } else {
            for (Object[] result : genresByPrice) {
                Genre genre = (Genre) result[0];
                Double avgPrice = (Double) result[1];
                Long trackCount = (Long) result[2];
                
                String genreName = genre.getName();
                String formattedPrice = formatPrice(avgPrice);
                
                System.out.println(genreName + " – " + formattedPrice + " – " + trackCount);
            }
        }
        
        System.out.println("========================================\n");
    }
    
    /**
     * Formatea un precio a formato con 2 decimales.
     * 
     * @param price precio a formatear
     * @return string formateado con 2 decimales
     */
    private static String formatPrice(Double price) {
        if (price == null) {
            return "0.00";
        }
        return String.format("%.2f", price);
    }
}