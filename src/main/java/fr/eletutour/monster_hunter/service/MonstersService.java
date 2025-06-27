package fr.eletutour.monster_hunter.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.eletutour.monster_hunter.models.GameAppearance;
import fr.eletutour.monster_hunter.models.Monster;
import fr.eletutour.monster_hunter.views.MonstersView.MonsterPerGame;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
public class MonstersService {

    private final List<Monster> allMonsters = new ArrayList<>();
    @Getter
    private final Map<String, List<MonsterPerGame>> monstersByGame = new TreeMap<>();

    @PostConstruct
    private void init(){
        loadMonsters();
        buildMonstersByGame();
    }

    private void loadMonsters() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            for(int i = 1; i<10; i++){
                File file = new ClassPathResource("static/monster.json").getFile();
                Monster[] monstersArray = objectMapper.readValue(file, Monster[].class);
                var pokemonFromGen = Arrays.asList(monstersArray);
                this.allMonsters.addAll(pokemonFromGen);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void buildMonstersByGame() {
        monstersByGame.clear();
        List<Monster> allBigMonsters = getAllLargeMonster();
        for (Monster monster : allBigMonsters) {
            for (GameAppearance appearance : monster.getGames()) {
                String game = appearance.getGame();
                monstersByGame
                    .computeIfAbsent(game, g -> new ArrayList<>())
                    .add(new MonsterPerGame(monster, appearance));
            }
        }
    }

    public List<Monster> getAllLargeMonster() {
        return allMonsters.stream()
                .filter(Monster::isLarge)
                .toList();
    }

    public Monster getMonsterById(String id) {
        return allMonsters.stream()
                .filter(monster -> monster.getId().getOid().equals(id))
                .findFirst()
                .orElse(null);
    }

}
