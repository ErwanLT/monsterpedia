# MonsterPedia

MonsterPedia est une application web qui sert d'encyclopédie pour les monstres de la franchise de jeux vidéo Monster Hunter. Construite avec Java, Spring Boot et Vaadin, elle offre une interface riche et interactive pour explorer les créatures du jeu.

L'application charge ses données depuis un fichier `monster.json` local, permettant une consultation rapide et hors-ligne des informations.

## ✨ Fonctionnalités

- **Liste des monstres** : Affiche les monstres regroupés par jeu d'apparition dans des sections dépliables.
- **Détails des monstres** : Pour chaque monstre, l'application présente une vue détaillée avec :
  - Son nom et son type.
  - L'image la plus récente du monstre pour une identification rapide.
  - Ses éléments, afflictions et ses faiblesses élémentaires.
  - Une galerie complète de toutes ses apparences (sprites) à travers les différents jeux.
  - Une section dédiée aux sous-espèces, avec une navigation directe vers leurs pages de détails respectives.
- **Navigation intuitive** : Une interface utilisateur simple et réactive conçue avec Vaadin pour une expérience de consultation fluide.

## 🛠️ Technologies utilisées

- **Backend** : Java 21, Spring Boot 3
- **Frontend** : Vaadin Flow
- **Utilitaires** : Lombok

## 🚀 Démarrage rapide

### Prérequis

- JDK 21 ou une version supérieure
- Maven 3.x

### Étapes

1. **Clonez le dépôt** :
   ```bash
   git clone https://github.com/Eletutour/monsterpedia.git
   cd monsterpedia
   ```

2. **Lancez l'application** :
   ```bash
   mvn spring-boot:run
   ```

3. **Accédez à l'application** :
   Une fois l'application démarrée, ouvrez votre navigateur et allez à l'adresse `http://localhost:8080`.
