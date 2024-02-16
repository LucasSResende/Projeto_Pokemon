package br.inatel.gameproject.model.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameDTO {
    private UUID id;

    @NotBlank(message = "PublisherId can not be empty")
    private String publisherId;
    @Size(max = 40, message = "Name can not exceeded 40 characters")
    @NotBlank(message = "Name can not be empty")
    private String name;
    private Map<LocalDate,
            @Min(value = 0, message = "Hour must be a positive integer")
            @Max(value = 24, message = "Hour can not exceeded 24")
                    Integer> timePlayed;

}