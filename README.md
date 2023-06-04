# ひとりにしてくれ – Projet d’I.A. 2022–2023

## Représentation du problème
Hitori est un puzzle logique japonais sur une grille de nombres. **Objectif** : éliminer des cases tout en respectant 3 règles :
1) une seule occurrence d'un chiffre par ligne et colonne
2) pas d'adjacence horizontale ou verticale entre cases marquées
3) connectivité entre cases non marquées.

Le problème du Hitori est résolu en utilisant une grille bidimensionnelle qui représente le plateau de jeu Hitori. Chaque case de la grille contient un nombre entier, les cases noircies étant représentées par des valeurs négatives.

La classe hitori.HitoriBoard définit la structure de la grille Hitori et fournit un certain nombre de méthodes pour gérer la grille, telles que vérifier la validité d'une solution Hitori, calculer le score d'adéquation (fitness) de la grille et effectuer des transformations sur la grille (comme cocher une case).

La classe hitori.RecuitSimule utilise l'algorithme du recuit simulé pour résoudre le jeu Hitori. L'algorithme du recuit simulé est une technique d'optimisation qui vise à trouver la meilleure solution possible en explorant l'espace des solutions et en acceptant les solutions qui améliorent la "fitness" ou la qualité de la solution.
## Structures de données
Les principales structures de données utilisées sont un tableau 2D d'entiers et une List
```java
class hitori.HitoriBoard {
    int[][] gridHitori;
    List<int[]> doublons;
    // reste du code... voir la classe hitori.HitoriBoard
}
```
`gridHitori` cette matrice est utilisée pour représenter la grille de jeu Hitori, avec chaque élément de la matrice représentant une case de la grille. Les valeurs négatives dans la matrice représentent les cases qui sont cochées.
- Chaque élément de la matrice représente une case de la grille.
- les entiers positifs dans la matrice représentent les valeurs des cases, tandis que les entiers négatifs représentent les cases cochées (ou noircies) dans le puzzle.
- Le tableau 2D est pratique pour accéder rapidement aux valeurs et aux états des cases dans la grille, en utilisant les coordonnées i et j.

`doublons` : Une liste d'arrays est également utilisée pour stocker les positions des cases doublons dans la grille.
- Utilisée pour stocker les positions des cases doublons dans la grille.
- Chaque élément de la liste est un tableau d'entiers (int[]) représentant les coordonnées (i, j) d'une case doublon.
- En utilisant une liste, il est plus facile de parcourir les positions des doublons et de les traiter individuellement.



## Fitness
La fitness de la solution Hitori est déterminée par le nombre de doublons dans chaque ligne et chaque colonne de la grille. Plus précisément, le score de fitness est calculé en comptant le nombre de doublons dans chaque ligne et chaque colonne et en sommant ces valeurs. Plus il y a de doublons, plus le score de fitness est élevé (et donc moins bon).

Cela est réalisé grâce aux méthodes `getFitnessScoreFromGrid` et `getFitnessScoreFromCell` qui calculent respectivement le score de fitness global de la grille et le score de fitness d'une cellule spécifique.

Ces deux méthodes fonctionnent en parcourant chaque ligne et chaque colonne et en vérifiant la présence de doublons. Si des doublons sont détectés, le score de fitness est augmenté.

## Fonctionnement

Pour résoudre une grille de Hitori, j'ai adopté une stratégie qui consiste à identifier et à travailler avec les cellules qui ont des doublons soit sur la même ligne soit sur la même colonne.
J'ai initialisé une liste qui stocke les coordonnées de ces cellules. Cette approche optimise le processus de résolution en ne modifiant que les cellules qui, une fois changées, peuvent affecter le score global de la grille.
Une fois les éléments communs rassemblés, on peut commencer à résoudre la grille. On effectue la résolution tant que notre "score" est supérieur à 0 et on effectue les étapes suivantes :

- Copie de la grille actuelle.
- Choix et modification d'une cellule aléatoire à partir de la liste des cellules dupliquées, puis calcul du score de cette cellule.
- Comparaison du nouveau score à l'ancien. Si le nouveau score est meilleur ou  si la méthode du recuit simulé accepte la solution avec une certaine probabilité alors :

    - On vérifie la validité de la grille modifiée avec `isConnected`
    - Si la grille est valide, le score et l'état de la grille sont sauvegardés. Si le score est nul, cela signifie que la grille est résolue et le programme s'arrête.
- Ajustement de la température. La température est multipliée par un facteur de réduction spécifié pour la faire diminuer progressivement.
- Si la température descend en dessous d'un certain seuil spécifié, elle est réinitialisée à une valeur prédéterminée pour permettre à l'algorithme de continuer à explorer de nouvelles solutions.

## Resulats
Nous allons exécuter l'algorithme de recuit simulé plusieurs fois sur différentes grilles disponible sur [Grille Hitori](grillesHitoriJava.txt) pour résoudre les puzzles Hitori et évalue les performances de l'algorithme en termes de temps d'exécution et de validité des solutions obtenues.
### Grille 10x10 Facile

| Essai | Temps d'exécution (ms) | Solution correcte |
|-------|-----------------------|-------------------|
| 1     | 77                    | true              |
| 2     | 86                    | true              |
| 3     | 84                    | true              |
| 4     | 58                    | true              |
| 5     | 77                    | true              |
| 6     | 202                   | true              |
| 7     | 69                    | true              |
| 8     | 14                    | true              |
| 9     | 151                   | true              |
| 10    | 70                    | true              |

Moyenne d'exécution : **88.8** ms

### Grille 15x15 hard ( Température initiale élevée )
| Essai | Temps d'exécution (ms) | Solution correcte |
|-------|-----------------------|-------------------|
| 1     | 5979                  | true              |
| 2     | 767                   | true              |
| 3     | 2605                  | true              |
| 4     | 10032                 | true              |
| 5     | 20151                 | true              |
| 6     | 4577                  | true              |
| 7     | 1787                  | true              |
| 8     | 44106                 | true              |
| 9     | 758                   | true              |
| 10    | 7987                  | true              |

Moyenne d'exécution : **9874.9** ms
Dans le premier test, où la température initiale était de 1000 (élevée), le temps d'exécution moyen était de 9874.9 ms. Les temps d'exécution individuels variaient grandement, avec un minimum de 767 ms et un maximum de 44106 ms. L'écart important dans les temps d'exécution indique une grande variabilité dans la performance de l'algorithme, probablement due à sa capacité à explorer un espace de solutions plus large grâce à la température initiale élevée. Cela signifie qu'il peut parfois trouver une bonne solution rapidement, mais d'autres fois, il peut prendre plus de temps.

### Grille 15x15 hard ( Température basse )
| Essai | Temps d'exécution (ms) | Solution correcte |
|-------|-----------------------|-------------------|
| 1     | 680                   | true              |
| 2     | 1999                  | true              |
| 3     | 3071                  | true              |
| 4     | 946                   | true              |
| 5     | 9155                  | true              |
| 6     | 10486                 | true              |
| 7     | 10717                 | true              |
| 8     | 15853                 | true              |
| 9     | 291                   | true              |
| 10    | 1205                  | true              |

Moyenne d'exécution : **5440.3** ms

Dans le second test, où le facteur de refroidissement était de 0.5 (faible), le temps d'exécution moyen était de 5440.3 ms, ce qui est nettement inférieur à celui du premier test. Comme dans le premier test, il y avait une variabilité dans les temps d'exécution individuels, mais l'écart était moins important, avec un minimum de 291 ms et un maximum de 15853 ms.
Le fait que le temps d'exécution moyen soit plus faible suggère que le système se refroidit plus rapidement, accélérant la convergence vers une solution. Cependant, cette accélération du refroidissement pourrait également augmenter le risque de se retrouver coincé dans un minimum local.
