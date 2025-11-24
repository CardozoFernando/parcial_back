package ar.edu.utnfrc.backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;

@Entity
@Table(name = "PLAYLIST")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Playlist {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_playlist_id")
    @SequenceGenerator(name = "seq_playlist_id", sequenceName = "SEQ_PLAYLIST_ID", allocationSize = 1)
    @Column(name = "PLAYLIST_ID")
    private Integer id;
    
    @Column(name = "NAME", length = 120)
    private String name;
    
    @OneToMany(mappedBy = "playlist")
    private Set<PlaylistTrack> playlistTracks;
}