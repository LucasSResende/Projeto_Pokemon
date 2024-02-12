package br.inatel.gameproject.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

public class GameDTO {
    private UUID id;

    @NotBlank
    private String publisherId;

    @NotNull
    private String name;

    @NotNull
    private Map<LocalDate, Integer> timePlayed;

    @Autowired
    public GameDTO() {
        this.id = id;
        this.publisherId = publisherId;
        this.name = name;
        this.timePlayed = timePlayed;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(String publisherId) {
        this.publisherId = publisherId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<LocalDate, Integer> getTimePlayed() {
        return timePlayed;
    }

    public void setTimePlayed(Map<LocalDate, Integer> timePlayed) {
        this.timePlayed = timePlayed;
    }
}