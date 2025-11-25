package ar.edu.utnfrc.backend.service;

import ar.edu.utnfrc.backend.models.BoardGame;
import ar.edu.utnfrc.backend.models.Category;
import ar.edu.utnfrc.backend.repository.CategoryRepository;

import java.util.Collection;

public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category guardarCategory(String nombre){

        Category category = new Category();
        category.setName(nombre);
        return this.categoryRepository.save(category);
    }

    public Collection<Category> buscarTodos(){
        return this.categoryRepository.findAll();
    }

    public void cantidad(){
        Long cant = this.categoryRepository.findAll().stream().count();

        System.out.println("Cantidad de Categories: "+cant);
    }
}
