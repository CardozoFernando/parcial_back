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
@Table(name = "artists")
public class Artist {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_artist_id")
    @SequenceGenerator(name = "seq_artist_id", sequenceName = "SEQ_ARTIST_ID", allocationSize = 1)
    @Column(name = "ARTIST_ID")
    private Integer id;
    
    @Column(name = "name")
    private String name;
    
    @OneToMany(mappedBy = "artist")
    private Set<Album> albums;
}