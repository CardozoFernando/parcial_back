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
@Table(name = "albums")
public class Album {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_album_id")
    @SequenceGenerator(name = "seq_album_id", sequenceName = "SEQ_ALBUM_ID", allocationSize = 1)
    @Column(name = "ALBUM_ID")
    private Integer id;
    
    @Column(name = "title")
    private String title;
    
    @ManyToOne
    @JoinColumn(name = "artist_id")
    private Artist artist;
    
    @OneToMany(mappedBy = "album")
    private Set<Track> tracks;
}