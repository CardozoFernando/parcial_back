package ar.edu.utnfrc.backend.services;

import ar.edu.utnfrc.backend.entities.*;
import ar.edu.utnfrc.backend.repositories.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Servicio para importar datos desde un archivo CSV.
 * 
 * Reglas de importación:
 * - Todas las columnas deben estar completas excepto composer (que puede estar vacío)
 * - Si alguna columna está vacía (excepto composer), la fila se descarta
 * - Si un elemento ya existe, se reutiliza; si no existe, se crea
 */
public class ImportService {
    
    private ArtistRepository artistRepository;
    private AlbumRepository albumRepository;
    private GenreRepository genreRepository;
    private MediaTypeRepository mediaTypeRepository;
    private TrackRepository trackRepository;
    
    // Contadores de inserciones
    private int artistsInserted = 0;
    private int albumsInserted = 0;
    private int genresInserted = 0;
    private int mediaTypesInserted = 0;
    private int tracksInserted = 0;
    private int rowsDiscarded = 0;
    
    public ImportService() {
        this.artistRepository = new ArtistRepository();
        this.albumRepository = new AlbumRepository();
        this.genreRepository = new GenreRepository();
        this.mediaTypeRepository = new MediaTypeRepository();
        this.trackRepository = new TrackRepository();
    }
    
    /**
     * Importa datos desde el archivo CSV ubicado en src/main/resources/data.csv
     * 
     * @return un objeto ImportResult con las estadísticas de la importación
     */
    public ImportResult importFromCsv() {
        // Reiniciar contadores
        artistsInserted = 0;
        albumsInserted = 0;
        genresInserted = 0;
        mediaTypesInserted = 0;
        tracksInserted = 0;
        rowsDiscarded = 0;
        
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(getClass().getResourceAsStream("/data.csv")))) {
            
            // Leer y descartar el encabezado
            String header = reader.readLine();
            if (header == null || !header.startsWith("trackName")) {
                throw new IllegalStateException("El archivo CSV no tiene el formato esperado");
            }
            
            String line;
            int lineNumber = 1; // Empezamos en 1 porque ya leímos el header
            
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                processCsvLine(line, lineNumber);
            }
            
            return new ImportResult(
                artistsInserted,
                albumsInserted,
                genresInserted,
                mediaTypesInserted,
                tracksInserted,
                rowsDiscarded
            );
            
        } catch (Exception e) {
            throw new RuntimeException("Error al importar datos desde CSV: " + e.getMessage(), e);
        }
    }
    
    /**
     * Procesa una línea del CSV.
     * 
     * @param line la línea del CSV a procesar
     * @param lineNumber el número de línea (para logging)
     */
    private void processCsvLine(String line, int lineNumber) {
        if (line == null || line.trim().isEmpty()) {
            rowsDiscarded++;
            return;
        }
        
        // Dividir por punto y coma
        String[] fields = line.split(";", -1); // -1 para incluir campos vacíos al final
        
        // Validar que tenga 9 campos
        if (fields.length != 9) {
            rowsDiscarded++;
            return;
        }
        
        // Extraer campos
        String trackName = fields[0].trim();
        String composer = fields[1].trim(); // Puede estar vacío
        String millisecondsStr = fields[2].trim();
        String bytesStr = fields[3].trim();
        String unitPriceStr = fields[4].trim();
        String albumTitle = fields[5].trim();
        String artistName = fields[6].trim();
        String genreName = fields[7].trim();
        String mediaTypeName = fields[8].trim();
        
        // Validar campos obligatorios (todos excepto composer)
        if (trackName.isEmpty() || 
            millisecondsStr.isEmpty() || 
            bytesStr.isEmpty() || 
            unitPriceStr.isEmpty() || 
            albumTitle.isEmpty() || 
            artistName.isEmpty() || 
            genreName.isEmpty() || 
            mediaTypeName.isEmpty()) {
            rowsDiscarded++;
            return;
        }
        
        // Validar y convertir tipos numéricos
        Integer milliseconds;
        Integer bytes;
        Double unitPrice;
        
        try {
            milliseconds = Integer.parseInt(millisecondsStr);
            bytes = Integer.parseInt(bytesStr);
            unitPrice = Double.parseDouble(unitPriceStr);
        } catch (NumberFormatException e) {
            rowsDiscarded++;
            return;
        }
        
        // Validar que los valores numéricos sean positivos
        if (milliseconds <= 0 || bytes <= 0 || unitPrice <= 0) {
            rowsDiscarded++;
            return;
        }
        
        // Procesar la fila válida
        try {
            processValidRow(trackName, composer, milliseconds, bytes, unitPrice, 
                          albumTitle, artistName, genreName, mediaTypeName);
        } catch (Exception e) {
            // Si hay error al procesar, descartar la fila
            rowsDiscarded++;
            System.err.println("Error procesando línea " + lineNumber + ": " + e.getMessage());
        }
    }
    
    /**
     * Procesa una fila válida del CSV, creando o reutilizando entidades.
     */
    private void processValidRow(String trackName, String composer, Integer milliseconds, 
                                Integer bytes, Double unitPrice, String albumTitle, 
                                String artistName, String genreName, String mediaTypeName) {
        
        // Obtener o crear Artist
        Artist artist = artistRepository.findByName(artistName);
        if (artist == null) {
            artist = new Artist();
            artist.setName(artistName);
            artistRepository.add(artist);
            artistsInserted++;
        }
        
        // Obtener o crear Genre
        Genre genre = genreRepository.findByName(genreName);
        if (genre == null) {
            genre = new Genre();
            genre.setName(genreName);
            genreRepository.add(genre);
            genresInserted++;
        }
        
        // Obtener o crear MediaType
        MediaType mediaType = mediaTypeRepository.findByName(mediaTypeName);
        if (mediaType == null) {
            mediaType = new MediaType();
            mediaType.setName(mediaTypeName);
            mediaTypeRepository.add(mediaType);
            mediaTypesInserted++;
        }
        
        // Obtener o crear Album (necesita el artista ya creado)
        Album album = albumRepository.findByTitleAndArtist(albumTitle, artist.getId());
        if (album == null) {
            album = new Album();
            album.setTitle(albumTitle);
            album.setArtist(artist);
            albumRepository.add(album);
            albumsInserted++;
        }
        
        // Crear Track
        Track track = new Track();
        track.setName(trackName);
        track.setComposer(composer.isEmpty() ? null : composer);
        track.setMilliseconds(milliseconds);
        track.setBytes(bytes);
        track.setUnitPrice(unitPrice);
        track.setAlbum(album);
        track.setGenre(genre);
        track.setMediaType(mediaType);
        
        trackRepository.add(track);
        tracksInserted++;
    }
    
    /**
     * Clase para almacenar los resultados de la importación.
     */
    public static class ImportResult {
        private final int artistsInserted;
        private final int albumsInserted;
        private final int genresInserted;
        private final int mediaTypesInserted;
        private final int tracksInserted;
        private final int rowsDiscarded;
        
        public ImportResult(int artistsInserted, int albumsInserted, int genresInserted,
                           int mediaTypesInserted, int tracksInserted, int rowsDiscarded) {
            this.artistsInserted = artistsInserted;
            this.albumsInserted = albumsInserted;
            this.genresInserted = genresInserted;
            this.mediaTypesInserted = mediaTypesInserted;
            this.tracksInserted = tracksInserted;
            this.rowsDiscarded = rowsDiscarded;
        }
        
        public int getArtistsInserted() { return artistsInserted; }
        public int getAlbumsInserted() { return albumsInserted; }
        public int getGenresInserted() { return genresInserted; }
        public int getMediaTypesInserted() { return mediaTypesInserted; }
        public int getTracksInserted() { return tracksInserted; }
        public int getRowsDiscarded() { return rowsDiscarded; }
        
        /**
         * Imprime el resumen de importación (método interno).
         */
        public void printSummary() {
            System.out.println("\n========================================");
            System.out.println("RESUMEN DE IMPORTACIÓN");
            System.out.println("========================================");
            System.out.println("Artistas insertados: " + artistsInserted);
            System.out.println("Álbumes insertados: " + albumsInserted);
            System.out.println("Géneros insertados: " + genresInserted);
            System.out.println("MediaTypes insertados: " + mediaTypesInserted);
            System.out.println("Tracks insertados: " + tracksInserted);
            System.out.println("Filas descartadas: " + rowsDiscarded);
            System.out.println("========================================\n");
        }
        
        /**
         * Imprime el Informe 1 — Resultado de la importación.
         * Formato oficial según el enunciado.
         */
        public void printInforme1() {
            System.out.println("\n========================================");
            System.out.println("Informe 1 — Resultado de la importación");
            System.out.println("========================================");
            System.out.println("Total de artistas insertados: " + artistsInserted);
            System.out.println("Total de álbumes insertados: " + albumsInserted);
            System.out.println("Total de géneros insertados: " + genresInserted);
            System.out.println("Total de media types insertados: " + mediaTypesInserted);
            System.out.println("Total de tracks insertados: " + tracksInserted);
            System.out.println("========================================\n");
        }
    }
}

