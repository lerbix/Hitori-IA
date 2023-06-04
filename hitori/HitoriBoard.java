package hitori;

import java.util.*;

class HitoriBoard {
    /**
     * Tableau 2D représentant la grille du puzzle Hitori.
     */
    private int[][] gridHitori;

    /**
     * Liste stockant les positions des nombres en double dans la grille.
     */
    private List<int[]> doublons;

    /**
     * Construit une nouvelle instance de hitori.HitoriBoard avec la grille fournie.
     * Initialise la variable gridHitori avec la grille donnée et
     * appelle la méthode initialisationDoublons(). pour initialiser les doublons
     *
     * @param grid Le tableau 2D représentant la grille du puzzle Hitori.
     */
    public HitoriBoard(int[][] grid) {
        this.gridHitori = grid;
        this.initialisationDoublons();
    }


    /**
     * Vérifie si une solution de grille Hitori est valide en respectant les règles du jeu.
     * Affiche un message d'erreur spécifiant quelle règle n'est pas respectée en cas d'erreur.
     *
     * @param grid Grille Hitori à vérifier
     * @return Vrai si la solution est valide, faux sinon
     */
    public static boolean isValidSolution(int[][] grid) {
        int gridSize = grid.length;

        // Vérification de la règle des occurrences uniques par ligne et colonne
        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                int cellValue = grid[row][col];
                if (cellValue > 0) {
                    // Vérification des doublons sur la même ligne
                    for (int otherCol = col + 1; otherCol < gridSize; otherCol++) {
                        if (grid[row][otherCol] == cellValue) {
                            System.out.println("Règle des occurrences uniques non respectée : Doublon trouvé sur la ligne " + (row + 1) + " (colonne " + (col + 1) + " et " + (otherCol + 1) + ")");
                            return false;
                        }
                    }
                    // Vérification des doublons sur la même colonne
                    for (int otherRow = row + 1; otherRow < gridSize; otherRow++) {
                        if (grid[otherRow][col] == cellValue) {
                            System.out.println("Règle des occurrences uniques non respectée : Doublon trouvé sur la colonne " + (col + 1) + " (ligne " + (row + 1) + " et " + (otherRow + 1) + ")");
                            return false;
                        }
                    }
                }
            }
        }

        // Vérification de la règle d'adjacence des cases marquées
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                if (grid[row][col] < 0) {
                    for (int[] dir : directions) {
                        int newRow = row + dir[0];
                        int newCol = col + dir[1];
                        if (newRow >= 0 && newRow < gridSize && newCol >= 0 && newCol < gridSize) {
                            if (grid[newRow][newCol] < 0) {
                                System.out.println("Règle d'adjacence des cases marquées non respectée : Cases adjacentes trouvées à la position (" + (row + 1) + ", " + (col + 1) + ")");
                                return false;
                            }
                        }
                    }
                }
            }
        }

        // Vérification de la règle de connectivité des cases non marquées
        if (!isConnected(grid)) {
            System.out.println("Règle de connectivité des cases non marquées non respectée : Certaines cases non marquées ne sont pas connectées entre elles.");
            return false;
        }

        // La solution est valide, toutes les règles sont respectées
        return true;
    }



    // Getter pour la grille
    public int[][] getGridHitori() {
        return gridHitori;
    }

    // Setter pour la grille
    public void setGridHitori(int[][] gridHitori) {
        this.gridHitori = gridHitori;
    }

    // Getter pour la liste des doublons
    public List<int[]> getDoublons() {
        return doublons;
    }



    /**
     * Initialise la liste des doublons dans la grille
     */
    public void initialisationDoublons() {
        doublons = new ArrayList<>();
        for (int i = 0; i < this.gridHitori.length; i++) {
            int[] ligne = this.gridHitori[i];
            for (int j = 0; j < ligne.length; j++) {
                if (contientDoublons(gridHitori, i, j)) {
                    this.doublons.add(new int[]{i, j});
                }
            }
        }
    }


    /**
     * Effectue une copie de la grille
     * @param grid Grille à copier
     * @return Copie de la grille
     */
    public static int[][] copyGrid(int[][] grid) {
        int[][] copy = new int[grid.length][];
        for (int i = 0; i < grid.length; i++) {
            copy[i] = Arrays.copyOf(grid[i], grid[i].length);
        }
        return copy;
    }

    /**
     * Calcule le score global d'une grille Hitori.
     * Le score est basé sur le nombre de doublons dans chaque ligne et chaque colonne de la grille.
     *
     * @param grid Grille Hitori
     * @return Score global de la grille
     */
    public static int getFitnessScoreFromGrid(int[][] grid) {
        int score = 0;
        int gridSize = grid.length;
        for (int row = 0; row < gridSize; row++) {
            boolean[] rowDuplicates = new boolean[gridSize + 1];
            boolean[] colDuplicates = new boolean[gridSize + 1];

            for (int col = 0; col < gridSize; col++) {
                int cellValue = grid[row][col];
                if (cellValue > 0) {
                    if (!rowDuplicates[cellValue]) {
                        rowDuplicates[cellValue] = true;
                    } else {
                        score++;
                    }
                }

                cellValue = grid[col][row];
                if (cellValue > 0) {
                    if (!colDuplicates[cellValue]) {
                        colDuplicates[cellValue] = true;
                    } else {
                        score++;
                    }
                }
            }
        }

        return score;
    }

    /**
     * Calcule le score d'une cellule spécifique dans une grille Hitori.
     * Le score est basé sur le nombre de doublons présents dans la ligne et la colonne de la cellule.
     *
     * @param grid Grille Hitori
     * @param rowIndex    Coordonnée i de la ligne de la cellule
     * @param colIndex    Coordonnée j de la colonne de la cellule
     * @return Score de la cellule
     */
    public static int getFitnessScoreFromCell(int[][] grid, int rowIndex, int colIndex) {
        int score = 0;
        int gridSize = grid.length;
        boolean[] rowDuplicates = new boolean[gridSize + 1];
        boolean[] colDuplicates = new boolean[gridSize + 1];

        for (int k = 0; k < gridSize; k++) {
            int cellValue = grid[rowIndex][k];
            if (cellValue > 0) {
                if (!rowDuplicates[cellValue]) {
                    rowDuplicates[cellValue] = true;
                } else {
                    score++;
                }
            }

            cellValue = grid[k][colIndex];
            if (cellValue > 0) {
                if (!colDuplicates[cellValue]) {
                    colDuplicates[cellValue] = true;
                } else {
                    score++;
                }
            }
        }

        return score;
    }


    /**
     * Vérifie si une case a des voisins checkés
     * @param grid Grille
     * @param i Coordonnée i de la ligne de la cellule
     * @param j Coordonnée j de la colonne de la cellule
     * @return Vrai si la case a des voisins noircis, faux sinon
     */
    public static boolean hasCheckedVoisins(int[][] grid, int i, int j) {
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int[] dir : directions) {
            int newRow = i + dir[0];
            int newCol = j + dir[1];
            if (newRow >= 0 && newRow < grid.length && newCol >= 0 && newCol < grid[newRow].length) {
                if (grid[newRow][newCol] < 0) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Vérifie si une case contient des doublons dans sa ligne ou sa colonne
     * @param grid Grille
     * @param i Coordonnée i de la ligne de la cellule
     * @param j Coordonnée j de la colonne de la cellule
     * @return Vrai si la case contient des doublons, faux sinon
     */
    public static boolean contientDoublons(int[][] grid, int i, int j) {
        int num = grid[i][j];
        // Vérifier les doublons sur la même ligne
        for (int col = 0; col < grid[i].length; col++) {
            if (col != j && grid[i][col] == num) {
                return true;
            }
        }
        // Vérifier les doublons sur la même colonne
        for (int row = 0; row < grid.length; row++) {
            if (row != i && grid[row][j] == num) {
                return true;
            }
        }
        return false;
    }

    /**
     * Vérifie si la grille est connectée.
     * @param grid Grille à vérifier.
     * @return Vrai si la grille est connectée, faux sinon.
     */
    public static boolean isConnected(int[][] grid) {
        int gridSize = grid.length;
        boolean[][] visited = new boolean[gridSize][gridSize];

        // Recherche d'une case non noircie pour commencer le parcours en largeur
        int startRow = 0;
        int startCol = 0;
        boolean foundStart = false;

        // Boucle pour trouver une case non noircie
        outerloop:
        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                if (grid[row][col] > 0) {
                    startRow = row;
                    startCol = col;
                    foundStart = true;
                    break outerloop;
                }
            }
        }

        // Si aucune case non noircie n'est trouvée, la grille est considérée comme non connectée
        if (!foundStart) {
            return false;
        }

        // Initialisation de la file d'attente pour le parcours en largeur
        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[]{startRow, startCol});
        visited[startRow][startCol] = true;

        int connectedCount = 0; // Compteur de cases connectées

        // Parcours en largeur pour explorer les cases connectées
        while (!queue.isEmpty()) {
            int[] cell = queue.poll();
            int row = cell[0];
            int col = cell[1];

            connectedCount++;

            // Ajout des voisins non visités à la file d'attente
            if (row - 1 >= 0 && !visited[row - 1][col] && grid[row - 1][col] > 0) {
                queue.offer(new int[]{row - 1, col});
                visited[row - 1][col] = true;
            }
            if (row + 1 < gridSize && !visited[row + 1][col] && grid[row + 1][col] > 0) {
                queue.offer(new int[]{row + 1, col});
                visited[row + 1][col] = true;
            }
            if (col - 1 >= 0 && !visited[row][col - 1] && grid[row][col - 1] > 0) {
                queue.offer(new int[]{row, col - 1});
                visited[row][col - 1] = true;
            }
            if (col + 1 < gridSize && !visited[row][col + 1] && grid[row][col + 1] > 0) {
                queue.offer(new int[]{row, col + 1});
                visited[row][col + 1] = true;
            }
        }

        // Vérification de la connectivité en comparant le nombre de cases connectées avec le nombre total de cases non noircies
        int nonBlackCount = countUncheckedCells(grid);
        return connectedCount == nonBlackCount;
    }

    /**
     * Compte le nombre de cases non noircies dans la grille.
     * @param grid Grille à compter.
     * @return Nombre de cases non noircies.
     */
    private static int countUncheckedCells(int[][] grid) {
        int count = 0;

        // Boucle pour compter les cases non noircies
        for (int[] row : grid) {
            for (int cell : row) {
                if (cell > 0) {
                    count++;
                }
            }
        }

        return count;
    }


    /**
     * Coche une case aléatoire parmi les éléments communs de la grille.
     * @param grid Grille sur laquelle effectuer le changement.
     * @param doublons Éléments doublons de la grille.
     * @return Différence de score après le changement.
     */
    public static int checkRandomCell(int[][] grid, List<int[]> doublons) {
        Random random = new Random();
        int[] randomCell;

        // Sélection d'une case aléatoire parmi les éléments communs jusqu'à trouver une case admissible
        do {
            randomCell = doublons.get(random.nextInt(doublons.size()));
        } while (!isAdmissible(grid, randomCell[0], randomCell[1]));

        int row = randomCell[0];
        int col = randomCell[1];

        // Calcul de l'ancien score avant le changement
        int oldScore = getFitnessScoreFromCell(grid, row, col);

        // Inversion de l'état de la case (cochée ou non)
        grid[row][col] = -grid[row][col];

        // Calcul du nouveau score après le changement
        int newScore = getFitnessScoreFromCell(grid, row, col);

        // Retour de la différence de score
        return newScore - oldScore;
    }



    /**
     * Vérifie si une case est admissible pour être cochée.
     * @param grid Grille sur laquelle effectuer la vérification.
     * @param i Coordonnée i de la ligne de la cellule.
     * @param j Coordonnée j de la colonne de la cellule.
     * @return Vrai si la case est admissible pour être cochée, faux sinon.
     */
    private static boolean isAdmissible(int[][] grid, int i, int j) {
        // Vérification si la case est déjà cochée
        if (grid[i][j] < 0) {
            return true;
        }
        // Vérification si les voisins ne sont pas cochés
        if (!hasCheckedVoisins(grid, i, j)) {
            return true;
        }

        return false;
    }






    /**
     * Retourne une représentation en chaîne du puzzle Hitori avec les valeurs alignées.
     *
     * @return Représentation en chaîne du puzzle Hitori
     */
    public String toString() {
        StringBuilder str = new StringBuilder();

        int gridSize = gridHitori.length;

        // Calcul de la largeur maximale des valeurs pour aligner les colonnes
        int maxCellValueWidth = String.valueOf(gridSize).length();

        // Ajout des valeurs de la grille avec alignement des colonnes
        for (int row = 0; row < gridSize; row++) {
            // Ajout des valeurs de la ligne avec alignement des colonnes
            for (int col = 0; col < gridSize; col++) {
                int cellValue = gridHitori[row][col];
                String cellString;
                if (cellValue > 0) {
                    cellString = String.valueOf(cellValue);
                } else {
                    cellString = "\u2613"; // Caractère Unicode "☒" pour les cases non marquées
                }
                str.append(" ").append(cellString).append(" ".repeat(maxCellValueWidth - cellString.length() + 1));
            }

            str.append("\n");

            // Ajout de la ligne de séparation entre les lignes de valeurs
            if (row < gridSize - 1) {
                str.append("-".repeat((maxCellValueWidth + 2) * gridSize - 1)).append("\n");
            }
        }

        return str.toString();
    }


}
