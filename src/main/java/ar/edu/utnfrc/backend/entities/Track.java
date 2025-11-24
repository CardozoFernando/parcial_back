package ar.edu.utnfrc.backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tracks")
public class Track {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_track_id")
    @SequenceGenerator(name = "seq_track_id", sequenceName = "SEQ_TRACK_ID", allocationSize = 1)
    @Column(name = "track_id")
    private Integer id;
    
    @Column(name = "name")
    private String name;
    
    @ManyToOne
    @JoinColumn(name = "album_id")
    private Album album;
    
    @ManyToOne
    @JoinColumn(name = "media_type_id")
    private MediaType mediaType;
    
    @ManyToOne
    @JoinColumn(name = "genre_id")
    private Genre genre;
    
    @Column(name = "composer")
    private String composer;
    
    @Column(name = "milliseconds")
    private Integer milliseconds;
    
    @Column(name = "bytes")
    private Integer bytes;
    
    @Column(name = "unit_price")
    private Double unitPrice;
    
    // Métodos de utilidad y validación
    public double getDurationInMinutes() {
        return milliseconds != null ? milliseconds / 60000.0 : 0.0;
    }
    
    public boolean hasValidPrice() {
        return unitPrice != null && unitPrice > 0.0;
    }
}