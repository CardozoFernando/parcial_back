package ar.edu.utnfrc.backend.services;

import ar.edu.utnfrc.backend.entities.Invoice;
import ar.edu.utnfrc.backend.repositories.InvoiceRepository;
import ar.edu.utnfrc.backend.services.interfaces.IService;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class InvoiceService implements IService<Invoice, Integer> {
    
    private InvoiceRepository repository;
    
    public InvoiceService() {
        this.repository = new InvoiceRepository();
    }
    
    @Override
    public void create(Invoice entity) {
        validateEntity(entity);
        repository.add(entity);
    }
    
    @Override
    public void update(Invoice entity) {
        validateEntity(entity);
        repository.update(entity);
    }
    
    @Override
    public void delete(Integer id) {
        Invoice entity = repository.getById(id);
        if (entity == null) {
            throw new IllegalArgumentException("Factura no encontrada con ID: " + id);
        }
        repository.delete(id);
    }
    
    @Override
    public Invoice getById(Integer id) {
        Invoice entity = repository.getById(id);
        if (entity == null) {
            throw new IllegalArgumentException("Factura no encontrada con ID: " + id);
        }
        return entity;
    }
    
    @Override
    public Set<Invoice> getAll() {
        return repository.getAll();
    }
    
    @Override
    public Stream<Invoice> getAllStream() {
        return repository.getAllStream();
    }
    
    private void validateEntity(Invoice entity) {
        if (entity.getCustomer() == null) {
            throw new IllegalArgumentException("La factura debe tener un cliente asignado");
        }
        if (entity.getInvoiceDate() == null) {
            throw new IllegalArgumentException("La fecha de factura no puede estar vacía");
        }
        if (entity.getTotal() == null || entity.getTotal() < 0) {
            throw new IllegalArgumentException("El total debe ser válido");
        }
        if (!entity.hasValidTotal()) {
            throw new IllegalArgumentException("El total de la factura no es válido");
        }
    }
    
    // ========== MÉTODOS ESPECÍFICOS ==========
    
    public List<Invoice> getInvoicesByCustomer(Integer customerId) {
        if (customerId == null || customerId <= 0) {
            throw new IllegalArgumentException("ID de cliente inválido");
        }
        return repository.findByCustomerId(customerId);
    }
    
    public List<Invoice> getInvoicesByDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Las fechas no pueden estar vacías");
        }
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha de fin");
        }
        return repository.findByInvoiceDateBetween(startDate, endDate);
    }
    
    public List<Invoice> getInvoicesByMinAmount(Double amount) {
        if (amount == null || amount < 0) {
            throw new IllegalArgumentException("El monto debe ser válido");
        }
        return repository.findByTotalGreaterThan(amount);
    }
}