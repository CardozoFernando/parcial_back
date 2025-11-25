package ar.edu.utnfrc.backend.service;

import ar.edu.utnfrc.backend.models.Category;
import ar.edu.utnfrc.backend.models.Designer;
import ar.edu.utnfrc.backend.repository.DesignerRepository;

import java.util.Collection;

public class DesignerService {

    private final DesignerRepository designerRepository;

    public DesignerService(DesignerRepository designerRepository) {
        this.designerRepository = designerRepository;
    }


    public Designer guardarDesigner(String nombre){

        Designer designer = new Designer();
        designer.setName(nombre);
        return this.designerRepository.save(designer);
    }

    public Collection<Designer> buscarTodos(){
        return this.designerRepository.findAll();
    }

    public void cantidad(){
        Long cant = this.designerRepository.findAll().stream().count();


        System.out.println("Cantidad de Designers: "+cant);
    }

}
