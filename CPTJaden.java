import arc.*;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.Font;
import java.awt.FontMetrics;

public class CPTJaden {
	static Color colP1Theme = Color.RED;
    static Color colP2Theme = Color.YELLOW;
    static Color colBoardTheme = Color.BLUE;
    static String strGameTitle = "Connect 4";
    
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
                con.setDrawColor(new Color(112, 58, 255));
				con.fillRect(0, 0, 1280, 720);
                boolean blnPlayAgain = true;
                while(blnPlayAgain) {
					int intWinner = playGame(con, strP1Name, strP2Name, intP1Wins, intP2Wins);
					if (intWinner == 1) {
						intP1Wins++;
					} else if (intWinner == 2) {
						intP2Wins++;
					}
					
					if (intWinner == 1 || intWinner == 2) {
						con.clear();
						con.setDrawColor(new Color(0, 0, 0, 150));
						con.fillRect(0, 0, 1280, 720);
						
						int intBoxHeight = 120;
						int intBoxY = 720 - intBoxHeight;
						con.setDrawColor(new Color(255, 255, 255));
						con.fillRect(0, intBoxY, 1280, intBoxHeight);

						String strWinnerName = (intWinner == 1) ? strP1Name : strP2Name;						
						String strWinMessage = strWinnerName + " Wins!";
						FontMetrics fm = con.getDrawFontMetrics();
						int intTextWidth = fm.stringWidth(strWinMessage);
						int intX = (1280 - intTextWidth) / 2;
						int intY = intBoxY + 75;

						con.setDrawColor(new Color(0, 0, 0));
						con.drawString(strWinMessage, intX, intY);
						
						con.sleep(3000);
					}
					
					con.clear();
					con.setDrawColor(new Color(112, 58, 255));
					con.fillRect(0, 0, 1280, 720);
					printCentered(con, "Do you want to play again? (y/n): ");
					String strResponse = con.readLine().toLowerCase();
					blnPlayAgain = strResponse.equals("y");
				}
            } else if (strChoice.equals("v")) {
				con.setDrawColor(new Color(112, 58, 255));
				con.fillRect(0, 0, 1280, 720);
				viewLeaderboard(con, strP1Name, intP1Wins, strP2Name, intP2Wins);
            } else if (strChoice.equals("c")) {
				String[] strThemeFiles = {
					"classic.txt",
					"christmas.txt",
					"vintage.txt",
					"galactic.txt",
				};

				con.setDrawColor(new Color(112, 58, 255));
				con.fillRect(0, 0, 1280, 720);

				// Display available themes
				con.println("Choose a theme:");
				for (int i = 0; i < strThemeFiles.length; i++) {
					con.println((i + 1) + ". " + strThemeFiles[i]);
				}

				con.print("Enter the number of your theme choice: ");
				int intThemeChoice = con.readInt();
				
				// Input validation
				if (intThemeChoice < 1 || intThemeChoice > strThemeFiles.length) {
					con.println("Invalid choice.");
					con.readLine();
				} else {
					String strSelectedTheme = strThemeFiles[intThemeChoice - 1];

					// Save selection
					TextOutputFile outTheme = new TextOutputFile("lasttheme.txt");
					outTheme.println(strSelectedTheme);
					outTheme.close();

					// Read the selected theme file
					TextInputFile themeIn = new TextInputFile(strSelectedTheme);
					String strThemeName = themeIn.readLine();

					// Read P1 RGB
					int intP1R = Integer.parseInt(themeIn.readLine());
					int intP1G = Integer.parseInt(themeIn.readLine());
					int intP1B = Integer.parseInt(themeIn.readLine());

					// Read P2 RGB
					int intP2R = Integer.parseInt(themeIn.readLine());
					int intP2G = Integer.parseInt(themeIn.readLine());
					int intP2B = Integer.parseInt(themeIn.readLine());

					// Read Board RGB
					int intBoardR = Integer.parseInt(themeIn.readLine());
					int intBoardG = Integer.parseInt(themeIn.readLine());
					int intBoardB = Integer.parseInt(themeIn.readLine());

					// Read game title
					strGameTitle = themeIn.readLine();

					// Assign colors
					colP1Theme = new Color(intP1R, intP1G, intP1B);
					colP2Theme = new Color(intP2R, intP2G, intP2B);
					colBoardTheme = new Color(intBoardR, intBoardG, intBoardB);

					themeIn.close();

					con.println("Theme \"" + strThemeName + "\" selected!");
					con.println();
					con.println("Press Enter to return to menu.");
					con.readLine();
				}
            } else if (strChoice.equals("t")) {
				con.setDrawColor(new Color(112, 58, 255));
				con.fillRect(0, 0, 1280, 720);
                con.println("Theme Creator feature not implemented yet.");
                con.println();
				con.println("Press Enter to return to menu.");
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
            con.print("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n" + (intCurrentPlayer == 1 ? strP1 : strP2) + ", choose column (1-7): ");
            int intCol = con.readInt() - 1;

            if (intCol < 0 || intCol > 6) {
                continue;
            }

            boolean blnPlaced = dropPiece(intBoard, intCol, intCurrentPlayer);
            if (!blnPlaced) {
                continue;
            }

            if (checkWin(intBoard, intCurrentPlayer)) {
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
		printCentered(con, strGameTitle);
		
		Font scoreFont = con.loadFont("ArialNarrow7-9YJ9n.ttf", 22);
		Font nameFont = con.loadFont("ArialNarrow7-9YJ9n.ttf", 18);
		
		int intboxX = 80;
		int intboxY = 250;
		int intboxWidth = 120;
		int intboxHeight = 120;
		int intboxX2 = 1280 - intboxWidth - 80;

		con.setDrawColor(Color.WHITE);
		con.fillRoundRect(intboxX, intboxY, intboxWidth, intboxHeight, 20, 20);
		con.setDrawColor(Color.BLACK);
		con.drawRoundRect(intboxX, intboxY, intboxWidth, intboxHeight, 20, 20);
		con.setDrawFont(nameFont);
		drawCenteredString(con, strP1.toUpperCase(), intboxX, intboxY, intboxWidth, intboxY + 30, nameFont, Color.BLACK);
		con.setDrawFont(scoreFont);
		drawCenteredString(con, String.valueOf(intP1Wins), intboxX, intboxY, intboxWidth, intboxY + 75, scoreFont, Color.BLACK);

		con.setDrawColor(Color.WHITE);
		con.fillRoundRect(intboxX2, intboxY, intboxWidth, intboxHeight, 20, 20);
		con.setDrawColor(Color.BLACK);
		con.drawRoundRect(intboxX2, intboxY, intboxWidth, intboxHeight, 20, 20);
		con.setDrawFont(nameFont);
		drawCenteredString(con, strP2.toUpperCase(), intboxX2, intboxY, intboxWidth, intboxY + 30, nameFont, Color.BLACK);
		con.setDrawFont(scoreFont);
		drawCenteredString(con, String.valueOf(intP2Wins), intboxX2, intboxY, intboxWidth, intboxY + 75, scoreFont, Color.BLACK);
		
        con.println();
		int intdiscSize = 60;
		int intgap = 10;

		Font numberFont = con.loadFont("ArialNarrow7-9YJ9n.ttf", 22);
		con.setDrawFont(numberFont);
		FontMetrics fm = con.getDrawFontMetrics();
		
        int intBoardX = 390;
		int intBoardY = 120;    
		int intBoardWidth = 490;
		int intBoardHeight = 420;

		con.setDrawColor(colBoardTheme);
		con.fillRect(intBoardX, intBoardY, intBoardWidth, intBoardHeight);

		for (int intCol = 0; intCol < 7; intCol++) {
			String strcolNumber = String.valueOf(intCol + 1);
			int inttextWidth = fm.stringWidth(strcolNumber);
			int intcenterX = 390 + intCol * (intdiscSize + intgap) + intdiscSize / 2;
			int inttextX = intcenterX+6 - inttextWidth / 2;
			int inttextY = 90;
			con.setDrawColor(Color.WHITE);
			con.drawString(strcolNumber, inttextX, inttextY);
		}

		for (int intRow = 0; intRow < 6; intRow++) {
			for (int intCol = 0; intCol < 7; intCol++) {
				int intX = 400 + intCol * (intdiscSize + intgap);
				int intY = 130 + intRow * (intdiscSize + intgap);

				if (intBoard[intRow][intCol] == 0) {
					con.setDrawColor(Color.WHITE);
				} else if (intBoard[intRow][intCol] == 1) {
					con.setDrawColor(colP1Theme);
				} else {
					con.setDrawColor(colP2Theme);
				}

				con.fillOval(intX-5, intY-5, intdiscSize, intdiscSize);
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

	public static void drawCenteredString(Console con, String strText, int intboxX, int intboxY, int intboxWidth, int intyPosition, Font font, Color color) {
		con.setDrawFont(font);
		FontMetrics fm = con.getDrawFontMetrics();
		int inttextWidth = fm.stringWidth(strText);
		int inttextX = intboxX + (intboxWidth - inttextWidth) / 2;
		con.setDrawColor(color);
		con.drawString(strText, inttextX, intyPosition);
	}
	public static void printCentered(Console con, String strtext) {
		int intconsoleWidth = 1280;
		int intcharWidth = 12;
		int inttextWidth = strtext.length() * intcharWidth;
		int intspaces = (intconsoleWidth - inttextWidth) / (2 * intcharWidth);
		for (int i = 0; i < intspaces; i++) {
			con.print(" ");
		}
		con.println(strtext);
	}
	
}
