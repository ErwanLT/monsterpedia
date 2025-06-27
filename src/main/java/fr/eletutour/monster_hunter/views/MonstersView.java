package fr.eletutour.monster_hunter.views;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.card.Card;
import com.vaadin.flow.component.card.CardVariant;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import fr.eletutour.monster_hunter.models.GameAppearance;
import fr.eletutour.monster_hunter.models.Monster;
import fr.eletutour.monster_hunter.service.MonstersService;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;

@Route(value = "monsters", layout = MainView.class)
@PageTitle("Monsters")
public class MonstersView extends VerticalLayout {

    private final MonstersService monstersService;
    private final VerticalLayout cardLayout = new VerticalLayout();
    List<Monster> allBigMonsters = new ArrayList<>();

    public MonstersView(MonstersService monstersService) {
        this.monstersService = monstersService;
        setSizeFull();
        add(cardLayout);
        updateCardList();
    }

    private void updateCardList() {
        cardLayout.removeAll();
        cardLayout.add(new Span("Loading monsters..."));
        var monstersByGame= monstersService.getMonstersByGame();
        cardLayout.removeAll();

        for (Map.Entry<String, List<MonsterPerGame>> entry : monstersByGame.entrySet()) {
            String game = entry.getKey();
            List<MonsterPerGame> monsterPerGames = entry.getValue();

            Set<String> displayedMonsterIds = new HashSet<>();

            FlexLayout content = new FlexLayout();
            content.setFlexWrap(FlexLayout.FlexWrap.WRAP);
            content.setJustifyContentMode(JustifyContentMode.START);
            content.setWidthFull();

            for (MonsterPerGame mpg : monsterPerGames) {
                String id = mpg.monster().getId().getOid();

                if (displayedMonsterIds.contains(id)) continue;
                displayedMonsterIds.add(id);

                Div card = new Div();
                card.setWidth("calc(25% - 20px)");
                card.getStyle().set("margin", "10px");
                Card monsterCard = createMonstercard(mpg.monster(), mpg.appearance());
                card.add(monsterCard);
                card.addClickListener(event -> {
                    getUI().ifPresent(u -> u.navigate(MonsterDetailView.class, mpg.monster().getId().getOid()));
                });
                monsterCard.add(mpg.appearance().getInfo());

                var dangerStr = mpg.appearance.getDanger();
                if (dangerStr == null || dangerStr.isEmpty()) {
                    dangerStr = "0";
                }
                int dangerLevel = Integer.parseInt(dangerStr);

                for (int i = 0; i < dangerLevel; i++) {
                    monsterCard.addToFooter(VaadinIcon.STAR.create());
                }

                content.add(card);
            }

            Details gameSection = new Details(game, content);
            gameSection.setOpened(false);
            cardLayout.add(gameSection);
        }
    }

    private Card createMonstercard(Monster monster, GameAppearance appearance) {
        Card monsterCard = new Card();
        monsterCard.addClassName("monster-card");
        monsterCard.addThemeVariants(CardVariant.LUMO_OUTLINED,
                CardVariant.LUMO_ELEVATED);

        if(!CollectionUtils.isEmpty(monster.getElements())) {
            Div elementsDiv = new Div();
            for (String element : monster.getElements()) {
                Image elementIcon = new Image("elements/" + element.toLowerCase() + ".png", element);
                elementIcon.setHeight(50, Unit.PIXELS);
                elementsDiv.add(new Div(elementIcon));
            }
            monsterCard.setHeaderPrefix(elementsDiv);
        }

        monsterCard.setTitle(monster.getName());
        monsterCard.setSubtitle(new Div(monster.getType()));

        Image monsterImage = new Image("icons/" + appearance.getImage(), monster.getName());
        monsterImage.setWidth("200px");
        monsterCard.setMedia(monsterImage);

        return monsterCard;
    }

    public record MonsterPerGame(Monster monster, GameAppearance appearance) {}
}
