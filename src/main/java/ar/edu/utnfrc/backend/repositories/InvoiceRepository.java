package ar.edu.utnfrc.backend.repositories;

import ar.edu.utnfrc.backend.entities.Invoice;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class InvoiceRepository {
    
    private EntityManager em;
    
    public InvoiceRepository(EntityManager em) {
        this.em = em;
    }
    
    public Optional<Invoice> findById(Integer id) {
        return Optional.ofNullable(em.find(Invoice.class, id));
    }
    
    public List<Invoice> findAll() {
        TypedQuery<Invoice> query = em.createQuery("SELECT i FROM Invoice i", Invoice.class);
        return query.getResultList();
    }
    
    public List<Invoice> findByCustomerId(Integer customerId) {
        TypedQuery<Invoice> query = em.createQuery(
                "SELECT i FROM Invoice i WHERE i.customer.id = :customerId", 
                Invoice.class);
        query.setParameter("customerId", customerId);
        return query.getResultList();
    }
    
    public List<Invoice> findByInvoiceDateBetween(LocalDate startDate, LocalDate endDate) {
        TypedQuery<Invoice> query = em.createQuery(
                "SELECT i FROM Invoice i WHERE i.invoiceDate BETWEEN :startDate AND :endDate", 
                Invoice.class);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        return query.getResultList();
    }
    
    public List<Invoice> findByTotalGreaterThan(Double amount) {
        TypedQuery<Invoice> query = em.createQuery(
                "SELECT i FROM Invoice i WHERE i.total > :amount", 
                Invoice.class);
        query.setParameter("amount", amount);
        return query.getResultList();
    }
    
    public void save(Invoice invoice) {
        if (invoice.getId() == null) {
            em.persist(invoice);
        } else {
            em.merge(invoice);
        }
    }
}