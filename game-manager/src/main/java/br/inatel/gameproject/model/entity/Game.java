package br.inatel.gameproject.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String publisherId;
    private String name;

    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TimePlayed> timePlayed = new ArrayList<>();

    public void addTimePlayed(TimePlayed timePlayedOfGame) {
        timePlayed.add(timePlayedOfGame);
        timePlayedOfGame.setGame(this);
    }
}
