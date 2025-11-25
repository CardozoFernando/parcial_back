package ar.edu.utnfrc.backend.service;

import ar.edu.utnfrc.backend.models.BoardGame;
import ar.edu.utnfrc.backend.models.Category;
import ar.edu.utnfrc.backend.models.Designer;
import ar.edu.utnfrc.backend.models.Publisher;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class ImportService {

    private final BoardGameService boardGameService;
    private final CategoryService categoryService;
    private final DesignerService designerService;
    private final PublisherService publisherService;

    public ImportService(BoardGameService boardGameService, CategoryService categoryService, DesignerService designerService, PublisherService publisherService) {
        this.boardGameService = boardGameService;
        this.categoryService = categoryService;
        this.designerService = designerService;
        this.publisherService = publisherService;
    }

//name;category;year_published;designer;min_age;average_rating;users_rated;min_players;max_players;publisher
    public void importar(String nombreArchivo) throws IOException {

        Map<String, Category> categoryMap = new HashMap<>();
        Map<String, Designer> designerMap  = new HashMap<>();
        Map<String, Publisher> publisherMap  = new HashMap<>();

        Path path = Path.of(nombreArchivo);

        Files.lines(path).skip(1).forEach((String linea)->{

//name;category;year_published;designer;min_age;average_rating;users_rated;min_players;max_players;publisher
            var campos = linea.split(";");

            var nombreCategory = campos[1];
            var nombrePublisher = campos[campos.length-1];
            var nombreDesigner = campos[3];


            // Category
            Category c = categoryMap.get(nombreCategory);
            if (c==null){
                c = this.categoryService.guardarCategory(nombreCategory);
                categoryMap.put(nombreCategory,c);
            }

            // Publisher
            Publisher p = publisherMap.get(nombrePublisher);

            if (p==null){
                p = this.publisherService.guardarPublisher(nombrePublisher);
                publisherMap.put(nombrePublisher,p);
            }

            // Designer
            Designer d = designerMap.get(nombreDesigner);
            if (d==null){
                d = this.designerService.guardarDesigner(nombreDesigner);
                designerMap.put(nombreDesigner,d);
            }
//name;category;year_published;designer;min_age;average_rating;users_rated;min_players;max_players;publisher

//    public BoardGame agregarBoardGame(String name, String year, String minAge, String avgRating, String usersRating, String minPlayers, String maxPlayers, Category c, Publisher p, Designer d)
            BoardGame boardGame = this.boardGameService.agregarBoardGame(campos[0],campos[2],campos[4],campos[5],campos[6],campos[7],campos[8],c,p,d);


        });


    }


}
