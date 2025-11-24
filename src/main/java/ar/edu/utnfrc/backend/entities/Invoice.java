package ar.edu.utnfrc.backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "invoices")
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_invoice_id")
    @SequenceGenerator(name = "seq_invoice_id", sequenceName = "SEQ_INVOICE_ID", allocationSize = 1)
    @Column(name = "INVOICE_ID")
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
    
    @Column(name = "invoice_date")
    private LocalDate invoiceDate;
    
    @Column(name = "billing_address")
    private String billingAddress;
    
    @Column(name = "billing_city")
    private String billingCity;
    
    @Column(name = "billing_state")
    private String billingState;
    
    @Column(name = "billing_country")
    private String billingCountry;
    
    @Column(name = "billing_postal_code")
    private String billingPostalCode;
    
    @Column(name = "total")
    private Double total;
    
    @OneToMany(mappedBy = "invoice")
    private Set<InvoiceItem> items;
    
    // Métodos de validación
    public boolean hasValidTotal() {
        return total != null && total > 0.0;
    }
    
    public double calculateTotalFromItems() {
        if (items == null || items.isEmpty()) {
            return 0.0;
        }
        return items.stream()
                .mapToDouble(item -> (item.getUnitPrice() != null ? item.getUnitPrice() : 0.0) 
                        * (item.getQuantity() != null ? item.getQuantity() : 0))
                .sum();
    }
}