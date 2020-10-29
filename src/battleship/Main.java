package battleship;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        Player player1 = new Player("Player 1");
        Player player2 = new Player("Player 2");

        takePositions(player1, player2);

        /*
         * Keep asking the players to hit each other's ships until one of them loses all of his ships.
         * */
        do {
            // Player 1 starts the first move.
            String report = player1.takeShotAndReport(player2);

            // Is game over?
            if (report.contains("winner")) {
                System.out.println(report);
                System.out.println("\n\n*****Game Over*****\n\n");
                break;
            }

            System.out.println(report + "\n" +
                    "Press Enter and pass the move to another player");
            sc.nextLine();

            // Player 2 turn.
            report = player2.takeShotAndReport(player1);
            if (report.contains("winner")) {
                System.out.println(report);
                System.out.println("\n\n*****Game Over*****\n\n");
                break;
            }

            System.out.println(report + "\n" +
                    "Press Enter and pass the move to another player");
            sc.nextLine();

        } while (true);


    }

    /**
     * For each player in the game, ask them to place their ships on the field.
     *
     * @param players the players involved in the game, usually two players.
     */
    private static void takePositions(Player... players) {
        for (Player player : players) {
            player.takePosition();
            System.out.println("Press Enter and pass the move to another player");
            new Scanner(System.in).nextLine();
        }
    }

    /**
     * Print the passed field.
     *
     * @param field the field to be printed.
     */
    static void printField(char[][] field) {
        // Column header represents column coordinates.
        System.out.println("\n  1 2 3 4 5 6 7 8 9 10");

        // Row header represents row coordinates.
        char[] rows = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'};

        int rowIndex = 0;
        for (char[] vector : field) {
            System.out.print(rows[rowIndex] + " ");

            for (char symbol : vector) {
                System.out.print(symbol + " ");
            }
            rowIndex++;
            System.out.println();
        }

        System.out.println();
    }

}




















