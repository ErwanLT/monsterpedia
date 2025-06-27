package fr.eletutour.monster_hunter.models;

import java.util.Arrays;
import java.util.Optional;

public enum GameTitle {
    MH3U("Monster Hunter 3 Ultimate", 1),
    MH4U("Monster Hunter 4 Ultimate", 2),
    MHGU("Monster Hunter Generations Ultimate", 3),
    MHS1("Monster Hunter Stories", 4),
    MHS2("Monster Hunter Stories 2", 5),
    MHR("Monster Hunter Rise", 6),
    MHW("Monster Hunter World", 7),
    MHWILDS("Monster Hunter Wilds", 8);

    private final String displayName;
    private final int weight;

    GameTitle(String displayName, int weight) {
        this.displayName = displayName;
        this.weight = weight;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getWeight() {
        return weight;
    }

    public static Optional<GameTitle> fromDisplayName(String name) {
        return Arrays.stream(values())
                .filter(gt -> gt.displayName.equalsIgnoreCase(name))
                .findFirst();
    }
}
