package ar.edu.utnfrc.backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "invoice_items")
public class InvoiceItem {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_invoice_line_id")
    @SequenceGenerator(name = "seq_invoice_line_id", sequenceName = "SEQ_INVOICE_LINE_ID", allocationSize = 1)
    @Column(name = "INVOICE_LINE_ID")
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "invoice_id")
    private Invoice invoice;
    
    @ManyToOne
    @JoinColumn(name = "track_id")
    private Track track;
    
    @Column(name = "unit_price")
    private Double unitPrice;
    
    @Column(name = "quantity")
    private Integer quantity;
}