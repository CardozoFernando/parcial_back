package ar.edu.utnfrc.backend.service;

import ar.edu.utnfrc.backend.models.Category;
import ar.edu.utnfrc.backend.models.Designer;
import ar.edu.utnfrc.backend.models.Publisher;
import ar.edu.utnfrc.backend.repository.PublisherRepository;

import java.util.Collection;

public class PublisherService {

    private final PublisherRepository publisherRepository;


    public PublisherService(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    public Publisher guardarPublisher(String nombre){

        Publisher publisher = new Publisher();
        publisher.setName(nombre);
        return this.publisherRepository.save(publisher);
    }

    public Collection<Publisher> buscarTodos(){
        return this.publisherRepository.findAll();
    }

    public void cantidad(){
        Long cant = this.publisherRepository.findAll().stream().count();

        System.out.println("Cantidad de Publishers: "+cant);
    }

}
