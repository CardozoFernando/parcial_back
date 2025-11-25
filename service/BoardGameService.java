package ar.edu.utnfrc.backend.service;

import ar.edu.utnfrc.backend.models.BoardGame;
import ar.edu.utnfrc.backend.models.Category;
import ar.edu.utnfrc.backend.models.Designer;
import ar.edu.utnfrc.backend.models.Publisher;
import ar.edu.utnfrc.backend.repository.BoardGameRepository;

import java.util.*;
import java.util.stream.Collectors;

public class BoardGameService {
    private final BoardGameRepository boardGameRepository;

    public BoardGameService(BoardGameRepository boardGameRepository) {
        this.boardGameRepository = boardGameRepository;
    }

    public BoardGame agregarBoardGame(String name, String year, String minAge, String avgRating, String usersRating, String minPlayers, String maxPlayers, Category c, Publisher p, Designer d) {
        BoardGame boardGame = new BoardGame();

        boardGame.setName(name);
        boardGame.setYearPublished(Integer.parseInt(year));
        boardGame.setMinAge(Integer.parseInt(minAge));
        boardGame.setAverageRating(Double.parseDouble(avgRating));
        boardGame.setUsersRating(Integer.parseInt(usersRating));

        int m = minPlayers.isBlank() ? 0 : Integer.parseInt(minPlayers);

        boardGame.setMinPlayers(m);
        boardGame.setMaxPlayers(Integer.parseInt(maxPlayers));

        boardGame.setCategory(c);
        boardGame.setPublisher(p);
        boardGame.setDesigner(d);

        return this.boardGameRepository.save(boardGame);
    }

    public BoardGame filtrarPorId(int id){
        return this.boardGameRepository.findById(id);
    }

    public Collection<BoardGame> buscarTodos(){
        return this.boardGameRepository.findAll();
    }

    public void cantidadJuegosImportados(){
        Long cantidad = this.boardGameRepository.findAll().stream().count();
        System.out.println("Cantidad de Juegos importados:"+cantidad);
    }

    public void categoriasConMenorPromedioDeRating() {
        Map<String, DoubleSummaryStatistics> statsPorCategoria = boardGameRepository.findAll().stream()
                .filter(game -> game.getUsersRating() > 500)
                .collect(Collectors.groupingBy(
                        game -> game.getCategory().getName(),
                        Collectors.summarizingDouble(BoardGame::getAverageRating)
                ));

        statsPorCategoria.entrySet().stream()
                .sorted(Comparator.comparingDouble(entry -> entry.getValue().getAverage()))
                .limit(5)
                .forEach(entry -> {
                    String categoria = entry.getKey();
                    double promedio = entry.getValue().getAverage();
                    long totalUsuarios = entry.getValue().getCount(); // cantidad de juegos, no usuarios

                    // Para mostrar la suma de usuarios calificados por categoría:
                    int sumaUsuarios = boardGameRepository.findAll().stream()
                            .filter(game -> game.getUsersRating() > 500)
                            .filter(game -> game.getCategory().getName().equals(categoria))
                            .mapToInt(BoardGame::getUsersRating)
                            .sum();

                    System.out.printf("%-20s %.2f (%d usuarios)%n", categoria, promedio, sumaUsuarios);
                });
    }

    public void mostrarJuegosAptos(int edad, int cantidadJugadores) {
        System.out.printf("Juegos aptos para %d jugadores y edad %d+%n", cantidadJugadores, edad);
        System.out.println("----------------------------------------------");

        int[] edades = {edad}; // para usar con isSuitableForAges

        boardGameRepository.findAll().stream()
                .filter(game -> game.getUsersRating() >= 100)
                .filter(game -> game.supportsPlayerCount(cantidadJugadores))
                .filter(game -> game.isSuitableForAges(edades))
                .sorted(Comparator.comparingDouble(BoardGame::getAverageRating).reversed())
                .forEach(game -> {
                    String puntos = ".".repeat(Math.max(1, 30 - game.getName().length()));
                    System.out.printf("%s %s Edad mínima: %d Jugadores: %d – %d%n",
                            game.getName(), puntos, game.getMinAge(), game.getMinPlayers(), game.getMaxPlayers());
                });
    }







}
