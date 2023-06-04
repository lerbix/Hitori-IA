package hitori;

public class Main {

    public static void main(String[] args) {

        int [][] grid_5x5_wikipedia = {
                {2, 2, 1, 5, 3},
                {2, 3, 1, 4, 5},
                {1, 1, 1, 3, 5},
                {1, 3, 5, 4, 2},
                {5, 4, 3, 2, 1}
        };

        int [][] grid_8x8_wikipedia = {
                {4, 8, 1, 6, 3, 2, 5, 7},
                {3, 6, 7, 2, 1, 6, 5, 4},
                {2, 3, 4, 8, 2, 8, 6, 1},
                {4, 1, 6, 5, 7, 7, 3, 5},
                {7, 2, 3, 1, 8, 5, 1, 2},
                {3, 5, 6, 7, 3, 1, 8, 4},
                {6, 4, 2, 3, 5, 4, 7, 8},
                {8, 7, 1, 4, 2, 3, 5, 6}
        };

        int[][] grid_10x10_hard = {
                {10,  8,  1,  9,  3, 10,  4,  1,  9,  6},
                { 4,  6,  6,  9,  2, 10,  7,  8,  3,  7},
                { 5,  7, 10,  5,  4,  3,  5,  9,  8,  5},
                { 3,  1,  5,  8,  1,  2,  1,  6,  1,  4},
                { 6,  4, 10, 10,  1,  6,  2,  6,  5,  6},
                {10,  3,  2,  1, 10,  7, 10,  5,  4,  8},
                { 7,  9,  8,  9,  5,  9,  3,  1,  9,  2},
                {10,  6, 10,  3, 10,  1, 10,  4,  2, 10},
                { 8,  2,  4,  5,  6,  6,  9,  2,  1,  3},
                { 6,  1,  6,  4,  8,  9,  6,  7,  6,  6},
        };


        int[][] grid_12x12_simple = {
                { 4,  8,  1, 10,  2,  3, 11, 10,  3,  8,  6, 11},
                { 6, 11,  3,  4, 10,  2,  9,  2,  8, 12, 10,  5},
                { 3,  5,  8, 10,  2,  2, 11, 11, 12,  8,  5, 12},
                { 8, 12,  7,  9,  8, 11,  1,  6, 12,  9, 10,  4},
                { 8, 10, 10,  1, 11,  6, 12,  8,  6,  7, 12,  2},
                { 7,  3, 12,  5, 10,  1, 10,  4,  3, 11, 11,  8},
                { 1,  5, 10,  6,  6,  9,  9, 11, 10,  4, 11, 12},
                { 1,  3,  8,   9,  5, 10,  2,  8, 10,  1,  7,  7},
                { 9, 11, 11,  6, 12, 10,  7,  3,  6,  5,  5,  7},
                {10,  9,  6,  4,  3,  3,  7,  6,  4, 11,  2,  1},
                {12, 12,  2,  7,  3,  8,  6,  5,  8,  3,  2, 10},
                { 7,  8,  9,  1,  7,  6, 10,  1,  5, 10, 12,  5},
        };


        int[][] grid_15x15_hard = {
                {11, 12,  4, 11, 11,  9,  6, 10, 14,  6,  5,  6,  8,  6,  3},
                {15,  7,  2, 12, 13,  9,  1,  5,  8, 10,  9,  4,  5, 11,  9},
                { 4, 15,  8,  9,  8,  1,  4, 11,  7,  6, 13,   7,  2,  4,  5},
                {10,  9,  5,  9, 15,  7,  4,  3,  7, 14,  9,   2,  9, 13,  9},
                {11, 14,  2,  6,  2, 12, 15,  2,  5,   2, 10,   2,  4,  2,  1},
                {14, 13,  8,  7,  9,   2,  7, 15,  4,   1,   7,   5, 14, 12,  4},
                { 9,  2, 10, 14,  1,   6,  5,   2, 15, 12,   6,   3, 11,  6,  7},
                { 7,  3,  7,  2, 10,   5,  6,   7,   4,   6,   8,  12, 10,  1, 10},
                {14,  2,  6,  4,  5,  13,  9,   1,   3,  13, 12,   2, 10,  7,  8},
                { 5,  6,  1,  5,  8,  14,  6, 12,   7, 11,   7,   8, 14, 10,  4},
                { 5, 10,  5, 11,  4,   7,  2,   5,   1,   7,   6,  15,   9,  7, 13},
                {13,  2, 15, 10, 12,   6, 10,   9,   2,   5,  11,   2,   3,  8, 14},
                { 2,  5,  2,  1,   2,   4,  8,  13,   6, 15,   2,  10,   2,  3, 12},
                {12,  2,  7, 15,   3,  15, 14,   6,   9,   4,   2,   9,   1,  5,  9},
                { 8,  1,  2, 13,   8,  10, 12,   4,  11,   2,   3,   4,   5,  2, 15}
        };




        int NombresEssai = 10;
        double temparatureInitial = 200;
        double coolingFactor = 0.9;
        int iterationsPerTemperature = 100;

        long totalTempsExecution = 0;

        for(int i = 0 ; i < NombresEssai ; i++) {
            long tempsDebut = System.currentTimeMillis();
            HitoriBoard hitoriBoard = new HitoriBoard(grid_8x8_wikipedia);
            RecuitSimule solver = new RecuitSimule(coolingFactor, temparatureInitial, iterationsPerTemperature);
            solver.solve(hitoriBoard);
            System.out.println(hitoriBoard);
            long tempsFin = System.currentTimeMillis() - tempsDebut;
            totalTempsExecution += tempsFin;
            System.out.println("Temps d'execution de l'Essai " + (i+1) + " : " + tempsFin + " ms");
            System.out.println("Solution est correcte : " + HitoriBoard.isValidSolution(hitoriBoard.getGridHitori()));
        }

        double moyenneTempsExecution = (double) totalTempsExecution / NombresEssai;
        System.out.println("Moyenne d'exécution : " + moyenneTempsExecution + " ms");

    }
}
