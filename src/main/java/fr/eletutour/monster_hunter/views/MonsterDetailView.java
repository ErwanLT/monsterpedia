package fr.eletutour.monster_hunter.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import fr.eletutour.monster_hunter.models.GameAppearance;
import fr.eletutour.monster_hunter.models.GameTitle;
import fr.eletutour.monster_hunter.models.Monster;
import fr.eletutour.monster_hunter.service.MonstersService;
import org.jetbrains.annotations.NotNull;
import org.springframework.util.CollectionUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Route(value = "monster", layout = MainView.class)
@PageTitle("Monster Details")
public class MonsterDetailView extends VerticalLayout implements HasUrlParameter<String> {

    private final MonstersService monstersService;
    private Monster monster;

    public MonsterDetailView(MonstersService monstersService) {
        this.monstersService = monstersService;
    }

    @Override
    public void setParameter(BeforeEvent event, String parameter) {
        removeAll();

        monster = monstersService.getMonsterById(parameter);
        if (monster != null) {
            setAlignItems(Alignment.CENTER);

            H1 title = new H1(monster.getName());
            H2 subtitle = new H2(monster.getType());

            add(title, subtitle);

            if (!CollectionUtils.isEmpty(monster.getGames())) {
                Optional<GameAppearance> bestAppearance = monster.getGames().stream()
                        .max(Comparator.comparingInt(appearance ->
                                GameTitle.fromDisplayName(appearance.getGame())
                                        .map(GameTitle::getWeight)
                                        .orElse(0)
                        ));

                bestAppearance.ifPresent(appearance -> {
                    Image monsterImage = new Image("icons/" + appearance.getImage(), monster.getName());
                    monsterImage.setWidth("300px");
                    monsterImage.getStyle().set("margin", "auto");
                    add(monsterImage);
                });
            }

            // Section infos condensées
            HorizontalLayout infoRow = new HorizontalLayout();
            infoRow.setJustifyContentMode(JustifyContentMode.CENTER);
            infoRow.setAlignItems(Alignment.START);
            infoRow.setSpacing(true);

            infoRow.add(createElementInfoBok(monster.getElements()));
            infoRow.add(createAilmentBox(monster.getAilments()));
            infoRow.add(createWeaknessInfoBox(monster.getWeakness()));

            add(infoRow);

            // Section sprites (toutes les images du monstre)
            if (!CollectionUtils.isEmpty(monster.getGames())) {
                H3 spritesTitle = new H3("Sprites / Apparitions");
                add(spritesTitle);
                Div spritesDiv = new Div();
                spritesDiv.getStyle().set("display", "flex");
                spritesDiv.getStyle().set("gap", "12px");
                spritesDiv.getStyle().set("justify-content", "center");
                spritesDiv.getStyle().set("align-items", "center");
                spritesDiv.getStyle().set("flex-wrap", "wrap");
                monster.getGames().stream()
                    .sorted((a, b) -> {
                        int wa = GameTitle.fromDisplayName(a.getGame()).map(GameTitle::getWeight).orElse(0);
                        int wb = GameTitle.fromDisplayName(b.getGame()).map(GameTitle::getWeight).orElse(0);
                        return Integer.compare(wa, wb);
                    })
                    .forEach(appearance -> {
                        Image sprite = new Image("icons/" + appearance.getImage(), monster.getName() + " - " + appearance.getGame());
                        sprite.setWidth("120px");
                        spritesDiv.add(sprite);
                    });
                add(spritesDiv);
            }

            if(!CollectionUtils.isEmpty(monster.getSubSpecies())) {
                H3 subSpeciesTitle = new H3("Sous-espèces");
                add(subSpeciesTitle);
                HorizontalLayout subSpeciesDiv = new HorizontalLayout();

                for (String subSpecies : monster.getSubSpecies()) {
                    // Cherche le monstre correspondant dans allMonsters
                    Monster subMonster = monstersService.getAllLargeMonster().stream()
                        .filter(m -> m.getName().equalsIgnoreCase(subSpecies))
                        .findFirst()
                        .orElse(null);
                    if (subMonster != null && !CollectionUtils.isEmpty(subMonster.getGames())) {
                        // Prend la version la plus récente (poids max)
                        GameAppearance latest = subMonster.getGames().stream()
                            .max((a, b) -> {
                                int wa = GameTitle.fromDisplayName(a.getGame()).map(GameTitle::getWeight).orElse(0);
                                int wb = GameTitle.fromDisplayName(b.getGame()).map(GameTitle::getWeight).orElse(0);
                                return Integer.compare(wa, wb);
                            })
                            .orElse(null);
                        if (latest != null) {
                            VerticalLayout subSpeciesLayout = subspeciesInfo(subSpecies, latest, subMonster);
                            subSpeciesDiv.add(subSpeciesLayout);
                        }
                    }
                }
                add(subSpeciesDiv);
            }
        } else {
            add(new H1("Monstre introuvable"));
        }
    }

    @NotNull
    private VerticalLayout subspeciesInfo(String subSpecies, GameAppearance latest, Monster subMonster) {
        Image subSpeciesImage = new Image("icons/" + latest.getImage(), subSpecies);
        NativeLabel nativeLabel = new NativeLabel(subSpecies);
        subSpeciesImage.setWidth("120px");
        // Ajout du click listener pour naviguer vers la page de détail de la sous-espèce
        subSpeciesImage.addClickListener(e -> {
            getUI().ifPresent(ui -> ui.navigate(MonsterDetailView.class, subMonster.getId().getOid()));
        });
        return new VerticalLayout(subSpeciesImage, nativeLabel);
    }

    private Component createElementInfoBok(List<String> elements) {
        VerticalLayout box = new VerticalLayout();
        box.setSpacing(false);
        box.setPadding(false);
        box.setAlignItems(Alignment.CENTER);

        H3 label = new H3("Éléments");
        Div elementsDiv = new Div();
        elementsDiv.getStyle().set("display", "flex");
        elementsDiv.getStyle().set("gap", "6px");
        elementsDiv.getStyle().set("justify-content", "center");
        elementsDiv.getStyle().set("align-items", "center");
        elementsDiv.getStyle().set("flex-wrap", "wrap");
        elementsDiv.getStyle().set("max-width", "80px");
        if(!CollectionUtils.isEmpty(elements)) {
            for (String element : elements) {
                Image elementIcon = new Image("elements/" + element.toLowerCase() + ".png", element);
                elementIcon.setHeight(20, Unit.PIXELS);
                elementsDiv.add(elementIcon);
            }
        }
        box.add(label, elementsDiv);

        return box;
    }

    private Component createWeaknessInfoBox(List<String> weakness) {
        VerticalLayout box = new VerticalLayout();
        box.setSpacing(false);
        box.setPadding(false);
        box.setAlignItems(Alignment.CENTER);

        H3 label = new H3("Faiblesses");
        Div elementsDiv = new Div();
        elementsDiv.getStyle().set("display", "flex");
        elementsDiv.getStyle().set("gap", "6px");
        elementsDiv.getStyle().set("justify-content", "center");
        elementsDiv.getStyle().set("align-items", "center");
        elementsDiv.getStyle().set("flex-wrap", "wrap");
        elementsDiv.getStyle().set("max-width", "80px");
        if(!CollectionUtils.isEmpty(weakness)) {
            for (String weak : weakness) {
                Image elementIcon = new Image("elements/" + weak.toLowerCase() + ".png", weak);
                elementIcon.setHeight(20, Unit.PIXELS);
                elementsDiv.add(elementIcon);
            }
        }
        box.add(label, elementsDiv);

        return box;
    }

    private Component createAilmentBox(List<String> ailments) {
        VerticalLayout box = new VerticalLayout();
        box.setSpacing(false);
        box.setPadding(false);
        box.setAlignItems(Alignment.CENTER);

        H3 label = new H3("Afflictions");
        Div ailmentsDiv = new Div();
        ailmentsDiv.getStyle().set("display", "flex");
        ailmentsDiv.getStyle().set("gap", "6px");
        ailmentsDiv.getStyle().set("justify-content", "center");
        ailmentsDiv.getStyle().set("align-items", "center");
        ailmentsDiv.getStyle().set("flex-wrap", "wrap");
        ailmentsDiv.getStyle().set("max-width", "80px");
        if(!CollectionUtils.isEmpty(ailments)) {
            for (String ailment : ailments) {
                Image elementIcon = new Image("ailments/" + ailment.toLowerCase() + ".png", ailment);
                elementIcon.setHeight(20, Unit.PIXELS);
                ailmentsDiv.add(elementIcon);
            }
        }
        box.add(label, ailmentsDiv);
        return box;
    }
}
