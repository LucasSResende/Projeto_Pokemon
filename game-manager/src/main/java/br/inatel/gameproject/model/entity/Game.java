package br.inatel.gameproject.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(of = "id")
@Builder

public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;
    private String publisherId;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "game_id")
    private List<TimePlayed> timePlayed = new ArrayList<>();

    public void addTimePlayed(TimePlayed timePlayedOfGame) {
        timePlayed.add(timePlayedOfGame);
        timePlayedOfGame.setGame(this);
    }
}