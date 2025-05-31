import arc.*;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.Font;

public class CPTJaden {
    public static void main(String[] args) {
        Console con = new Console("Connect 4", 1280,720);
        con.setBackgroundColor(new Color(112, 58, 255));
        displayMainMenu(con);
    }

    public static void displayMainMenu(Console con) {
        boolean blnExit = false;

        String strP1Name = "";
        String strP2Name = "";
        int intP1Wins = 0;
        int intP2Wins = 0;

        while (!blnExit) {
            con.clear();
            
            int intcardX = 390;
			int intcardY = 90;
			int intcardWidth = 500;
			int intcardHeight = 540;
			int intcenterX = intcardX + intcardWidth / 2;
			int intbuttonWidth = 400;
			int intbuttonHeight = 60;
			int intstartY = intcardY + 140;
			int intspacingY = 80;

			con.setDrawColor(new Color(146, 82, 255));
			con.fillRoundRect(intcardX, intcardY, intcardWidth, intcardHeight, 40, 40);

			con.setDrawColor(Color.BLACK);
			con.drawRoundRect(intcardX, intcardY, intcardWidth, intcardHeight, 40, 40);
            
            BufferedImage logo = con.loadImage("Connect 4 Logo.png");
			int intlogoX = (logo.getWidth()) / 2;
			int intlogoY = 20;
			con.drawImage(logo, intlogoX, intlogoY);
					
			String[] strlabels = {
				"(P)lay Game",
				"(V)iew Leaderboard",
				"(C)hoose Theme",
				"(T)heme Creator",
				"(Q)uit"
			};
			
			for (int i = 0; i < strlabels.length; i++) {
				drawButton(con, strlabels[i], intcenterX, intstartY + (i * intspacingY), intbuttonWidth, intbuttonHeight, Color.WHITE);
			}
			
            con.print("\nChoose an option: ");
            String strChoice = con.readLine().toLowerCase();

            if (strChoice.equals("p")) {
                if (strP1Name.equals("") || strP2Name.equals("")) {
                    con.print("Enter Player 1 name: ");
                    strP1Name = con.readLine();
                    con.print("Enter Player 2 name: ");
                    strP2Name = con.readLine();
                }
                con.setDrawColor(new Color(70, 30, 70));
				con.fillRect(0, 0, 700, 700);
                boolean blnPlayAgain = true;
                while (blnPlayAgain) {
					int intWinner = playGame(con, strP1Name, strP2Name, intP1Wins, intP2Wins);

					if (intWinner == 1) {
						intP1Wins++;
					} else if (intWinner == 2) {
						intP2Wins++;
					}
					
					con.print("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\nDo you want to play again? (y/n): ");
					String strResponse = con.readLine().toLowerCase();
					blnPlayAgain = strResponse.equals("y");
					con.clear();
					con.setDrawColor(new Color(70, 30, 70));
					con.fillRect(0, 0, 700, 700);
				}
            } else if (strChoice.equals("v")) {
					con.setDrawColor(new Color(70, 30, 70));
					con.fillRect(0, 0, 700, 700);
                    viewLeaderboard(con, strP1Name, intP1Wins, strP2Name, intP2Wins);
            } else if (strChoice.equals("c")) {
				con.setDrawColor(new Color(70, 30, 70));
				con.fillRect(0, 0, 700, 700);
                con.println("Choose Theme feature not implemented yet.");
                con.readLine();
            } else if (strChoice.equals("t")) {
				con.setDrawColor(new Color(70, 30, 70));
				con.fillRect(0, 0, 700, 700);
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
    
    public static void drawButton(Console con, String strLabel, int intcenterX, int inty, int intwidth, int intheight, Color fillColor) {
		int intx = intcenterX - intwidth / 2;
		con.setDrawColor(Color.BLACK);
		con.fillRoundRect(intx + 6, inty + 6, intwidth, intheight, 20, 20);
		con.setDrawColor(fillColor);
		con.fillRoundRect(intx, inty, intwidth, intheight, 20, 20);
		con.setDrawColor(Color.BLACK);
		con.drawRoundRect(intx, inty, intwidth, intheight, 20, 20);
		con.setDrawColor(Color.BLACK);
		Font fntOptTitle = con.loadFont("ArialNarrow7-9YJ9n.ttf", 24);
		con.setDrawFont(fntOptTitle);
		con.drawString(strLabel, intx + 20, inty + 12);
	}

    public static int playGame(Console con, String strP1, String strP2, int intP1Wins, int intP2Wins) {
        int[][] intBoard = new int[6][7];
        int intCurrentPlayer = 1;

        while (true) {
            drawBoard(con, intBoard, strP1, strP2, intP1Wins, intP2Wins);
            con.println();
            con.print("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n" + (intCurrentPlayer == 1 ? strP1 : strP2) + " (" + (intCurrentPlayer == 1 ? "Red" : "Yellow") + "), choose column (1-7): ");
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
        Color purple = new Color(112, 58, 255);
		Color yellow = new Color(255, 214, 100);
		con.setDrawColor(purple);
		con.fillRect(0, 0, 1280, 620);
		con.setDrawColor(yellow);
		con.fillRect(0, 620, 1280, 100);

		con.setDrawColor(Color.WHITE);
        con.println(strP1 + " (Red): " + intP1Wins + " wins " + " - Connect 4 - " + strP2 + " (Yellow): " + intP2Wins + " wins");
		
        con.println();
        BufferedImage img = con.loadImage("Connect 4 Board.png");
		int intimgX = (700 - img.getWidth()) / 2;
		int intimgY = (700 - img.getHeight()) / 4;
		con.drawImage(img, intimgX, intimgY);
		int intdiscSize = 60;
		int intgap = 8;
		int intstartX = intimgX + 8;
		int intstartY = intimgY + 15;

		for (int intCol = 0; intCol < 7; intCol++) {
			int intx = intimgX + 8 + intCol * (intdiscSize + intgap) + intdiscSize / 2 - 5;
			int inty = intimgY - 10;
			con.drawString(String.valueOf(intCol + 1), intx, inty);
		}

		for (int intRow = 0; intRow < 6; intRow++) {
			for (int intCol = 0; intCol < 7; intCol++) {
				int intx = intstartX + intCol * (intdiscSize + intgap);
				int inty = intstartY + intRow * (intdiscSize + intgap); 
				if (intBoard[intRow][intCol] == 1) {
					con.setDrawColor(Color.RED);
					con.fillOval(intx, inty, intdiscSize, intdiscSize);
				} else if (intBoard[intRow][intCol] == 2) {
					con.setDrawColor(Color.YELLOW);
					con.fillOval(intx, inty, intdiscSize, intdiscSize);
				}
            }
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
	public static void viewLeaderboard(Console con, String strP1, int intP1Wins, String strP2, int intP2Wins) {
		String[][] strLeaderboard = new String[100][2];
		int intCount = 0;

		TextOutputFile leaderBoardOut = new TextOutputFile("leaderboard.txt");
		leaderBoardOut.println(strP1);
		leaderBoardOut.println(Integer.toString(intP1Wins));
		leaderBoardOut.println(strP2);
		leaderBoardOut.println(Integer.toString(intP2Wins));
		leaderBoardOut.close();

		TextInputFile leaderBoard = new TextInputFile("leaderboard.txt");
		while (!leaderBoard.eof() && intCount < strLeaderboard.length) {
			strLeaderboard[intCount][0] = leaderBoard.readLine();
			strLeaderboard[intCount][1] = leaderBoard.readLine();
			intCount++;
		}
		leaderBoard.close();

		// Sort
		for (int i = 0; i < intCount - 1; i++) {
			for (int j = 0; j < intCount - i - 1; j++) {
				int intWins1 = Integer.parseInt(strLeaderboard[j][1]);
				int intWins2 = Integer.parseInt(strLeaderboard[j + 1][1]);
				if (intWins1 < intWins2) {
					String strTempName = strLeaderboard[j][0];
					String strTempWins = strLeaderboard[j][1];
					strLeaderboard[j][0] = strLeaderboard[j + 1][0];
					strLeaderboard[j][1] = strLeaderboard[j + 1][1];
					strLeaderboard[j + 1][0] = strTempName;
					strLeaderboard[j + 1][1] = strTempWins;
				}
			}
		}

		con.clear();
		con.println("LEADERBOARD");
		con.println("------------");
		for (int i = 0; i < intCount; i++) {
			con.println((i + 1) + ". " + strLeaderboard[i][0] + " - " + strLeaderboard[i][1] + " wins");
		}
		con.println();
		con.println("Press Enter to return to menu.");
		con.readLine();
	}
	
	public static void printCentered(Console con, String text) {
		int consoleWidth = 700;
		int charWidth = 12;
		int textWidth = text.length() * charWidth;
		int spaces = (consoleWidth - textWidth) / (2 * charWidth);
		for (int i = 0; i < spaces; i++) {
			con.print(" ");
		}
		con.println(text);
	}
}
