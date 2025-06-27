package fr.eletutour.monster_hunter.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class Monster {

    @JsonProperty("_id")
    private IdWrapper id;

    private String name;
    private String type;
    @JsonProperty("isLarge")
    private boolean isLarge;

    private List<String> subSpecies;
    private List<String> elements;
    private List<String> ailments;
    private List<String> weakness;

    private List<GameAppearance> games;
}
