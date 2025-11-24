package ar.edu.utnfrc.backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "playlist_track")
public class PlaylistTrack {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_playlist_track_id")
    @SequenceGenerator(name = "seq_playlist_track_id", sequenceName = "SEQ_PLAYLIST_TRACK_ID", allocationSize = 1)
    @Column(name = "PLAYLIST_TRACK_ID")
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "playlist_id")
    private Playlist playlist;
    
    @ManyToOne
    @JoinColumn(name = "track_id")
    private Track track;
}