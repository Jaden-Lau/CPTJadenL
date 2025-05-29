import arc.*;

public class CPTJaden {
    public static void main(String[] args) {
        Console con = new Console();
        displayMainMenu(con);
    }

    public static void displayMainMenu(Console con) {
        boolean blnExit = false;

        // Store player names and win counts
        String strP1 = "";
        String strP2 = "";
        int intP1Wins = 0;
        int intP2Wins = 0;

        while (!blnExit) {
            con.clear();
            con.println("=== CONNECT 4 ===");
            con.println("(P)lay Game");
            con.println("(V)iew Leaderboard");
            con.println("(C)hoose Theme");
            con.println("(T)heme Creator");
            con.println("(Q)uit");
            con.print("Choose an option: ");
            String strChoice = con.readLine().toLowerCase();

            if (strChoice.equals("p")) {
                // Ask for player names if not yet set
                if (strP1.equals("") || strP2.equals("")) {
                    con.print("Enter Player 1 name: ");
                    strP1 = con.readLine();
                    con.print("Enter Player 2 name: ");
                    strP2 = con.readLine();
                }
                
                boolean blnPlayAgain = true;
                while (blnPlayAgain) {
					int intWinner = playGame(con, strP1, strP2, intP1Wins, intP2Wins);

					if (intWinner == 1) {
						intP1Wins++;
					} else if (intWinner == 2) {
						intP2Wins++;
					}
					
					con.print("\nDo you want to play again? (y/n): ");
					String strResponse = con.readLine().toLowerCase();
					blnPlayAgain = strResponse.equals("y");
				}
            } else if (strChoice.equals("v")) {
                con.println("View Leaderboard feature not implemented yet.");
                con.readLine();
            } else if (strChoice.equals("c")) {
                con.println("Choose Theme feature not implemented yet.");
                con.readLine();
            } else if (strChoice.equals("t")) {
                con.println("Theme Creator feature not implemented yet.");
                con.readLine();
            } else if (strChoice.equals("q")) {
                blnExit = true;
            } else {
                con.println("Invalid choice. Please try again.");
                con.readLine();
            }
        }

        con.println("Thanks for playing!");
    }

    public static int playGame(Console con, String strP1, String strP2, int intP1Wins, int intP2Wins) {
        int[][] intBoard = new int[6][7];
        int intCurrentPlayer = 1;

        while (true) {
            drawBoard(con, intBoard, strP1, strP2, intP1Wins, intP2Wins);
            con.println();
            con.print((intCurrentPlayer == 1 ? strP1 : strP2) + " (" + (intCurrentPlayer == 1 ? "Red" : "Yellow") + "), choose column (1-7): ");
            int intCol = con.readInt() - 1;

            if (intCol < 0 || intCol > 6) {
                con.println("Invalid column. Try again.");
                continue;
            }

            boolean blnPlaced = dropPiece(intBoard, intCol, intCurrentPlayer);
            if (!blnPlaced) {
                con.println("That column is full. Try another one.");
                continue;
            }

            if (checkWin(intBoard, intCurrentPlayer)) {
                drawBoard(con, intBoard, strP1, strP2, intP1Wins, intP2Wins);
                con.println();
                con.println((intCurrentPlayer == 1 ? strP1 : strP2) + " wins!");
                return intCurrentPlayer;
            }

            intCurrentPlayer = (intCurrentPlayer == 1) ? 2 : 1;
        }
    }

    public static boolean dropPiece(int[][] intBoard, int intCol, int intPlayer) {
        for (int intRow = 5; intRow >= 0; intRow--) {
            if (intBoard[intRow][intCol] == 0) {
                intBoard[intRow][intCol] = intPlayer;
                return true;
            }
        }
        return false;
    }

    public static void drawBoard(Console con, int[][] intBoard, String strP1, String strP2, int intP1Wins, int intP2Wins) {
        con.clear();
        con.println(strP1 + " (Red): " + intP1Wins + " wins " + " - Connect 4 - " + strP2 + " (Yellow): " + intP2Wins + " wins");
        for (int intCol = 1; intCol <= 7; intCol++) {
            con.print(" " + intCol + " ");
        }
        con.println();

        for (int intRow = 0; intRow < 6; intRow++) {
            for (int intCol = 0; intCol < 7; intCol++) {
                if (intBoard[intRow][intCol] == 0) {
                    con.print("[ ]");
                } else if (intBoard[intRow][intCol] == 1) {
                    con.print("[R]");
                } else {
                    con.print("[Y]");
                }
            }
            con.println();
        }
    }

    public static boolean checkWin(int[][] intBoard, int intPlayer) {
        // Horizontal
        for (int intRow = 0; intRow < 6; intRow++) {
            for (int intCol = 0; intCol <= 3; intCol++) {
                if (intBoard[intRow][intCol] == intPlayer &&
                    intBoard[intRow][intCol+1] == intPlayer &&
                    intBoard[intRow][intCol+2] == intPlayer &&
                    intBoard[intRow][intCol+3] == intPlayer) {
                    return true;
                }
            }
        }

        // Vertical
        for (int intCol = 0; intCol < 7; intCol++) {
            for (int intRow = 0; intRow <= 2; intRow++) {
                if (intBoard[intRow][intCol] == intPlayer &&
                    intBoard[intRow+1][intCol] == intPlayer &&
                    intBoard[intRow+2][intCol] == intPlayer &&
                    intBoard[intRow+3][intCol] == intPlayer) {
                    return true;
                }
            }
        }

        // Diagonal down-right
        for (int intRow = 0; intRow <= 2; intRow++) {
            for (int intCol = 0; intCol <= 3; intCol++) {
                if (intBoard[intRow][intCol] == intPlayer &&
                    intBoard[intRow+1][intCol+1] == intPlayer &&
                    intBoard[intRow+2][intCol+2] == intPlayer &&
                    intBoard[intRow+3][intCol+3] == intPlayer) {
                    return true;
                }
            }
        }

        // Diagonal up-right
        for (int intRow = 3; intRow < 6; intRow++) {
            for (int intCol = 0; intCol <= 3; intCol++) {
                if (intBoard[intRow][intCol] == intPlayer &&
                    intBoard[intRow-1][intCol+1] == intPlayer &&
                    intBoard[intRow-2][intCol+2] == intPlayer &&
                    intBoard[intRow-3][intCol+3] == intPlayer) {
                    return true;
                }
            }
        }

        return false;
    }
}
