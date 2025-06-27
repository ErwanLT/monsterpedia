package fr.eletutour.monster_hunter;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Theme("monster-hunter")
public class MonsterHunterApplication implements AppShellConfigurator {

	public static void main(String[] args) {
		SpringApplication.run(MonsterHunterApplication.class, args);
	}

}
