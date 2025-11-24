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
@Table(name = "genres")
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_genre_id")
    @SequenceGenerator(name = "seq_genre_id", sequenceName = "SEQ_GENRE_ID", allocationSize = 1)
    @Column(name = "GENRE_ID")
    private Integer id;
    
    @Column(name = "name")
    private String name;
    
    @OneToMany(mappedBy = "genre")
    private Set<Track> tracks;
}