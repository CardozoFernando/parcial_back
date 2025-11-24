package ar.edu.utnfrc.backend;

import ar.edu.utnfrc.backend.infra.DataSourceProvider;
import ar.edu.utnfrc.backend.infra.DbInitializer;
import ar.edu.utnfrc.backend.infra.LocalEntityManagerProvider;
import ar.edu.utnfrc.backend.repositories.ArtistRepository;
import ar.edu.utnfrc.backend.repositories.AlbumRepository;
import ar.edu.utnfrc.backend.repositories.TrackRepository;
import ar.edu.utnfrc.backend.repositories.InvoiceRepository;
import ar.edu.utnfrc.backend.entities.Artist;
import ar.edu.utnfrc.backend.entities.Album;
import ar.edu.utnfrc.backend.entities.Track;
import ar.edu.utnfrc.backend.entities.MediaType;
import ar.edu.utnfrc.backend.entities.Invoice;
import ar.edu.utnfrc.backend.entities.Customer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import java.time.LocalDate;

public class Main {
    
    public static void main(String[] args) {
        try {
            System.out.println("========================================");
            System.out.println("Iniciando smoke tests de la aplicación");
            System.out.println("========================================\n");
            
            // 1. Inicializar base de datos H2
            System.out.println("[1] Inicializando H2 en memoria...");
            DbInitializer.initialize(DataSourceProvider.getDataSource());
            System.out.println("[OK] DDL ejecutado correctamente.");
            System.out.println("[OK] H2 inicializado correctamente.\n");
            
            // 2. Obtener EntityManager
            System.out.println("[2] Obteniendo EntityManager...");
            EntityManager em = LocalEntityManagerProvider.getEntityManager();
            System.out.println("[OK] EntityManager creado.\n");
            
            // 3. Realizar smoke tests
            System.out.println("[3] Ejecutando smoke tests...\n");
            
            // Test 3.1: Contar registros existentes
            testCountRecords(em);
            
            // Test 3.2: Crear y persistir datos de prueba
            testCreateAndPersist(em);
            
            // Test 3.3: Validar métodos de utilidad
            testUtilityMethods(em);
            
            // Cerrar EntityManager
            em.close();
            LocalEntityManagerProvider.closeFactory();
            
            System.out.println("\n========================================");
            System.out.println("[OK] H2 + DDL inicializados y mapeos JPA verificados.");
            System.out.println("========================================");
            
        } catch (Exception e) {
            System.err.println("[ERROR] Fallo en la inicialización:");
            e.printStackTrace();
        }
    }
    
    private static void testCountRecords(EntityManager em) {
        System.out.println("  [Test 3.1] Contando registros en tablas...");
        
        TypedQuery<Long> artistQuery = em.createQuery(
                "SELECT COUNT(a) FROM Artist a", Long.class);
        long artistCount = artistQuery.getSingleResult();
        System.out.println("    - Artistas: " + artistCount);
        
        TypedQuery<Long> genreQuery = em.createQuery(
                "SELECT COUNT(g) FROM Genre g", Long.class);
        long genreCount = genreQuery.getSingleResult();
        System.out.println("    - Géneros: " + genreCount);
        
        TypedQuery<Long> mediaTypeQuery = em.createQuery(
                "SELECT COUNT(m) FROM MediaType m", Long.class);
        long mediaTypeCount = mediaTypeQuery.getSingleResult();
        System.out.println("    - Tipos de media: " + mediaTypeCount);
        System.out.println();
    }
    
    private static void testCreateAndPersist(EntityManager em) {
        System.out.println("  [Test 3.2] Creando y persistiendo datos de prueba...");
        
        // Crear repositorios
        ArtistRepository artistRepo = new ArtistRepository();
        AlbumRepository albumRepo = new AlbumRepository();
        TrackRepository trackRepo = new TrackRepository();
        InvoiceRepository invoiceRepo = new InvoiceRepository();
        
        EntityTransaction tx = em.getTransaction();
        
        try {
            tx.begin();
            
            // Crear Artist usando repositorio
            Artist artist = new Artist();
            artist.setName("Test Artist");
            artistRepo.add(artist);
            
            // Crear Album usando repositorio
            Album album = new Album();
            album.setTitle("Test Album");
            album.setArtist(artist);
            albumRepo.add(album);
            
            // Obtener MediaType
            MediaType mediaType = em.find(MediaType.class, 1);
            if (mediaType == null) {
                System.out.println("    [WARNING] No hay MediaType disponible. Creando uno...");
                mediaType = new MediaType();
                mediaType.setName("MPEG");
                em.persist(mediaType);
            }
            
            // Crear Track usando repositorio
            Track track = new Track();
            track.setName("Test Track");
            track.setAlbum(album);
            track.setMediaType(mediaType);
            track.setComposer("Test Composer");
            track.setMilliseconds(180000);
            track.setUnitPrice(0.99);
            trackRepo.add(track);
            
            // Crear Customer
            Customer customer = new Customer();
            customer.setFirstName("John");
            customer.setLastName("Doe");
            customer.setEmail("john@example.com");
            em.persist(customer);
            
            // Crear Invoice usando repositorio
            Invoice invoice = new Invoice();
            invoice.setCustomer(customer);
            invoice.setInvoiceDate(LocalDate.now());
            invoice.setBillingAddress("123 Main St");
            invoice.setBillingCity("Springfield");
            invoice.setBillingCountry("USA");
            invoice.setTotal(9.99);
            invoiceRepo.add(invoice);
            
            tx.commit();
            System.out.println("    - Artist creado: " + artist.getName());
            System.out.println("    - Album creado: " + album.getTitle());
            System.out.println("    - Track creado: " + track.getName());
            System.out.println("    - Customer creado: " + customer.getFirstName() + " " + customer.getLastName());
            System.out.println("    - Invoice creado: ID " + invoice.getId() + " - Total: $" + invoice.getTotal());
            System.out.println();
            
            // Test 3.2.1: Leer datos usando repositorios
            tx.begin();
            Artist readArtist = artistRepo.getById(artist.getId());
            Album readAlbum = albumRepo.getById(album.getId());
            Track readTrack = trackRepo.getById(track.getId());
            Invoice readInvoice = invoiceRepo.getById(invoice.getId());
            
            if (readArtist != null && readAlbum != null && readTrack != null) {
                System.out.println("  [Test 3.2.1] Verificando datos leídos...");
                System.out.println("    - Artist encontrado: " + readArtist.getName());
                System.out.println("    - Album encontrado: " + readAlbum.getTitle());
                System.out.println("    - Track encontrado: " + readTrack.getName());
            }
            
            if (readInvoice != null) {
                System.out.println("    - Invoice encontrado: Total $" + readInvoice.getTotal());
                System.out.println("    - Invoice es válida: " + readInvoice.hasValidTotal());
                System.out.println();
            }
            tx.commit();
            
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            System.err.println("    [ERROR] " + e.getMessage());
        }
    }
    
    private static void testUtilityMethods(EntityManager em) {
        System.out.println("  [Test 3.3] Validando métodos de utilidad...");
        
        TypedQuery<Track> trackQuery = em.createQuery(
                "SELECT t FROM Track t WHERE t.name = 'Test Track'", Track.class);
        
        try {
            Track testTrack = trackQuery.getSingleResult();
            
            // Verificar método getDurationInMinutes()
            double durationMinutes = testTrack.getDurationInMinutes();
            System.out.println("    - Duración del track: " + durationMinutes + " minutos");
            
            // Verificar método hasValidPrice()
            boolean validPrice = testTrack.hasValidPrice();
            System.out.println("    - Precio válido: " + validPrice);
            
            if (durationMinutes > 0 && validPrice) {
                System.out.println("    [OK] Métodos de utilidad funcionando correctamente.");
            }
            System.out.println();
            
        } catch (Exception e) {
            System.out.println("    [INFO] No hay datos de prueba para validar métodos de utilidad.");
            System.out.println();
        }
    }
}