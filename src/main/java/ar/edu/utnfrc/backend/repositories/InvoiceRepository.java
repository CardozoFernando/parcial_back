package ar.edu.utnfrc.backend.repositories;

import ar.edu.utnfrc.backend.entities.Invoice;
import jakarta.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InvoiceRepository extends Repository<Invoice, Integer> {
    
    @Override
    public Invoice getById(Integer id) {
        return manager.find(Invoice.class, id);
    }
    
    @Override
    public Set<Invoice> getAll() {
        TypedQuery<Invoice> query = manager.createQuery("SELECT i FROM Invoice i", Invoice.class);
        return query.getResultList().stream().collect(Collectors.toSet());
    }
    
    @Override
    public Stream<Invoice> getAllStream() {
        TypedQuery<Invoice> query = manager.createQuery("SELECT i FROM Invoice i", Invoice.class);
        return query.getResultList().stream();
    }
    
    // ========== MÉTODOS ESPECÍFICOS ==========
    
    public List<Invoice> findByCustomerId(Integer customerId) {
        TypedQuery<Invoice> query = manager.createQuery(
                "SELECT i FROM Invoice i WHERE i.customer.id = :customerId", 
                Invoice.class);
        query.setParameter("customerId", customerId);
        return query.getResultList();
    }
    
    public List<Invoice> findByInvoiceDateBetween(LocalDate startDate, LocalDate endDate) {
        TypedQuery<Invoice> query = manager.createQuery(
                "SELECT i FROM Invoice i WHERE i.invoiceDate BETWEEN :startDate AND :endDate", 
                Invoice.class);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        return query.getResultList();
    }
    
    public List<Invoice> findByTotalGreaterThan(Double amount) {
        TypedQuery<Invoice> query = manager.createQuery(
                "SELECT i FROM Invoice i WHERE i.total > :amount", 
                Invoice.class);
        query.setParameter("amount", amount);
        return query.getResultList();
    }
}