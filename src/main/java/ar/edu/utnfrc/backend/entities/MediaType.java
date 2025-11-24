package ar.edu.utnfrc.backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "media_types")
public class MediaType {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_media_type_id")
    @SequenceGenerator(name = "seq_media_type_id", sequenceName = "SEQ_MEDIA_TYPE_ID", allocationSize = 1)
    @Column(name = "MEDIA_TYPE_ID")
    private Integer id;
    
    @Column(name = "name")
    private String name;
    
    @OneToMany(mappedBy = "mediaType")
    private Set<Track> tracks;
}