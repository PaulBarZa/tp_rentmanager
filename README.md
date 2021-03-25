# Tp Rentmanager

## Lancement du projet

Lors du premier lancement il est nécessaire d'initialiser la base de données :

    mvn clean install exec:java 

Afin de lancer le projet avec le serveur tomcat 7 :

	mvn tomcat7:run
			
Le serveur tournera par défaut à l'adresse suivante : **http://localhost:8080/rentmanager**

Pour lancer le projet en ligne de commande il suffit de lancer depuis votre IDE la méthode *main*.
Cette méthode est située d'après le chemin absolu : **src/main/java/com/epf/rentmanager/ui/UI.java**


## Tests

Sont présents ici seulement des tests unitaires. La couverture de test couvre un total de **75%**.

**Lancer les tests** avec maven :

    mvn test

**Lancer les tests** à l'aide de la librairie jacoco :

    mvn clean jacoco:prepare-agent install jacoco:report

Un fichier html résumant la couverture de test se trouve ici : **target/sit/jacoco/index.html**


## Clen Code

L'ensemble du code respecte le plus possibles les notions de **Clean Code** demandées au début de ce TP. Très peu de fonctions dépassent les 10 - 15 lignes, la Javadoc a été réalisée sur toutes les fonctions (sauf getter et setter) et aucun commentaire autre que la Javadoc n'est écrit dans le code. Pour ce qui est du nom des fonctions et des variables, le choix d'utiliser l'anglais a été fait afin de garder de la cohérence avec le nom des différentes méthodes ou fonctions externes au projet.


### Checkstyle

Afin d'accéder au CheckStyle du projet :

    mvn site

Un fichier html correspondant au checkstyle est situé ici : **target/site/checkstyle.xml**


## Résultats obtenus

### Fonctionnalités attendues

L'ensemble du TP ainsi que l'ensemble des fonctionnalités a été réalisé dans ce projet. Toutes les fonctionnalités attendues ont été obtenues.

### Limite du projet 

 1 - Afin de tester tous les servlets, les fonctions *doGet* et *doPost* ont été passées en publique. 

**Problème de cela :** C'est un compromis entre, le principe d'encapsulation et le fait qu'une méthode doit avoir le moins de visibilité possible, et la testabilité des methodes.

**Ce que l'on aurait pu faire :** On aurait pu créer une classe enfant qui étend la classe principale et surcharge les methodes que l'on souhaite tester en les rendants publiques. La classe de test n'a donc pas accès à la classe protégée, elle reste donc privée.

 2 - La gestion des erreurs n'est pas optimales, une erreur plus compréhensible coté utlisateur aurait pu être implémenter (comme un toast ou une modale d'erreur).

 3 - Aucun test unitaire n'a été réalisé sur la partie interface en ligne de commande. De plus seul des tests unitaires ont été effectués sur ce projet, aucun test de bout en bout (end to end).
