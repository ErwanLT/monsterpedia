package fr.eletutour.monster_hunter.models;

import java.util.Arrays;
import java.util.Optional;

public enum GameTitle {
    MH3U("Monster Hunter 3 Ultimate", 1, "MH3U.png"),
    MH4U("Monster Hunter 4 Ultimate", 2, "MH4U.png"),
    MHGU("Monster Hunter Generations Ultimate", 3, "MHGU.png"),
    MHS1("Monster Hunter Stories", 4, "MHS1.png"),
    MHS2("Monster Hunter Stories 2", 5, "MHS2.png"),
    MHR("Monster Hunter Rise", 6, "MHR.png"),
    MHW("Monster Hunter World", 7, "MHW.png"),
    MHWILDS("Monster Hunter Wilds", 8, "MHWILDS.png"),;

    private final String displayName;
    private final int weight;
    private final String image;

    GameTitle(String displayName, int weight, String image) {
        this.displayName = displayName;
        this.weight = weight;
        this.image = image;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getWeight() {
        return weight;
    }

    public String getImage() {
        return image;
    }

    public static Optional<GameTitle> fromDisplayName(String name) {
        return Arrays.stream(values())
                .filter(gt -> gt.displayName.equalsIgnoreCase(name))
                .findFirst();
    }
}
