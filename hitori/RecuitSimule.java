package hitori;

import hitori.HitoriBoard;

import java.util.Random;

/**
 * Classe hitori.RecuitSimule utilisant l'algorithme du recuit simulé pour résoudre le jeu Hitori.
 */
class RecuitSimule {
    private final double coolingFactor;         // Taux de décroissance de la température
    private final double initialTemperature;    // Température initiale
    private final int iterationsPerTemperature; // Nombre d'itérations par température
      // Température de réinitialisation

    /**
     * Constructeur de la classe hitori.RecuitSimule.
     *
     * @param coolingFactor         Taux de décroissance de la température. Doit être inférieur à 1 et proche de 1.
     * @param initialTemperature    Température initiale. Plus elle est élevée, plus les solutions seront variées en début de résolution.
     * @param iterationsPerTemperature Nombre d'itérations à effectuer pour chaque température obtenue.
     */
    public RecuitSimule(double coolingFactor, double initialTemperature, int iterationsPerTemperature) {
        this.coolingFactor = coolingFactor;
        this.initialTemperature = initialTemperature;
        this.iterationsPerTemperature = iterationsPerTemperature;

    }

    /**
     * Résout le jeu Hitori en utilisant l'algorithme du recuit simulé.
     *
     * @param hitoriBoard Le plateau de jeu Hitori à résoudre.
     */
    public void solve(HitoriBoard hitoriBoard) {
        Random random = new Random();
        double temperature = this.initialTemperature;
        int currentScore = HitoriBoard.getFitnessScoreFromGrid(hitoriBoard.getGridHitori());
        int nextScore;
        int[][] nextGrid;

        /* Continue tant qu'aucune solution n'a été trouvée */
        while (currentScore > 0) {
            for (int i = 0; i < iterationsPerTemperature; i++) {
                /* Copie la grille puis modifie une cellule et récupère le score */
                nextGrid = HitoriBoard.copyGrid(hitoriBoard.getGridHitori());
                nextScore = currentScore + HitoriBoard.checkRandomCell(nextGrid, hitoriBoard.getDoublons());

                /*
                 * Si le score est inférieur ou égal au score actuel, ou si la méthode du recuit simulé accepte
                 * la solution avec une certaine probabilité, met à jour la grille actuelle.
                 */
                if (nextScore < currentScore || random.nextInt(100) < 1.00 / (1.00 + Math.exp((nextScore - currentScore) / temperature))) {
                    if (HitoriBoard.isConnected(nextGrid)) {
                        hitoriBoard.setGridHitori(nextGrid);
                        if (nextScore == 0) {
                            return;
                        } else {
                            currentScore = nextScore;
                        }
                    }
                }
            }

            /* Diminue la température */
            temperature *= this.coolingFactor;

            /* Réinitialise la température si le seuil bas est atteint */
            if (temperature < 0.000009) {
                temperature = 100;
            }
        }
    }
}
