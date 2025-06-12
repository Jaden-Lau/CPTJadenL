import arc.*;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.Font;
import java.awt.FontMetrics;
import java.util.Random;

public class CPTJaden {
    // Global variables for colour themes and fonts
    static Color colP1Theme = Color.RED;
    static Color colP2Theme = Color.YELLOW;
    static Color colBoardTheme = Color.BLUE;
    static String strGameTitle = "Connect 4";
    static Font FONT_SCORE;
    static Font FONT_NAME;
    static Font FONT_NUMBER;
    static Font FONT_INSTRUCTION;
    static Font FONT_WINNING;

    public static void main(String[] args) {
        // Setup console, title, and create background colour
        Console con = new Console("Connect 4", 1280, 720);
        con.setBackgroundColor(new Color(112, 58, 255));
        
        FONT_SCORE = con.loadFont("ArialNarrow7-9YJ9n.ttf", 22);
        FONT_NAME = con.loadFont("ArialNarrow7-9YJ9n.ttf", 18);
        FONT_NUMBER = con.loadFont("ArialNarrow7-9YJ9n.ttf", 22);
        FONT_INSTRUCTION = con.loadFont("ArialNarrow7-9YJ9n.ttf", 24);
        FONT_WINNING = con.loadFont("ArialNarrow7-9YJ9n.ttf", 36);
        
        displayMainMenu(con);
    }

    public static void displayMainMenu(Console con) {
        // initialize variables need to play game
        boolean blnExit = false;

        String strP1Name = "";
        String strP2Name = "";
        int intP1Wins = 0;
        int intP2Wins = 0;

        // This while loop will run until the user decides to quit the game
        while (!blnExit) {
            // initialize variables for the main menu
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

            // Draw rectangle for where the main buttons will be in
            con.setDrawColor(new Color(146, 82, 255));
            con.fillRoundRect(intcardX, intcardY, intcardWidth, intcardHeight, 40, 40);

            // Outline for the main buttons
            con.setDrawColor(Color.BLACK);
            con.drawRoundRect(intcardX, intcardY, intcardWidth, intcardHeight, 40, 40);


            // Draw the Connect 4 Logo, positioned to align with the rest of the main menu
            BufferedImage logo = con.loadImage("Connect 4 Logo.png");
            int intlogoX = (logo.getWidth()) / 2; // Corrected to center the logo relative to console width
            int intlogoY = 20;
            con.drawImage(logo, intlogoX, intlogoY);

            // Array of strings for the game/main menu options
            String[] strlabels = {
                "(P)lay Game",
                "(V)iew Leaderboard",
                "(C)hoose Theme",
                "(T)heme Creator",
                "(Q)uit",
            };

            // Will draw all the main menu buttons for how much is in the array
            for (int i = 0; i < strlabels.length; i++) {
                drawButton(con, strlabels[i], intcenterX, intstartY + (i * intspacingY), intbuttonWidth, intbuttonHeight, Color.WHITE);
            }

            // Will ask the user to type in the option they want according to the main menu (p,v,c,t,q,[s])
            con.print("\nChoose an option: ");
            String strChoice = con.readLine().toLowerCase();

            // This is where the program takes in what the user inputted before, and run a code based on the user's choice
            if (strChoice.equals("p")) {
                // Asking the user to input P1 and P2 name
                if (strP1Name.equals("") || strP2Name.equals("")) {
                    con.print("Enter Player 1 name: ");
                    strP1Name = con.readLine();
                    con.print("Enter Player 2 name: ");
                    strP2Name = con.readLine();
                }
                // Set play game background and variable if the player wants to play again
                con.setDrawColor(new Color(112, 58, 255));
                con.setBackgroundColor(new Color(112, 58, 255));
                boolean blnPlayAgain = true;
                // When blnPlayAgain is true, it will continue to run the main gameplay function
                while (blnPlayAgain) {
                    int intWinner = playGame(con, strP1Name, strP2Name, intP1Wins, intP2Wins);
                    if (intWinner == 1) {
                        intP1Wins++;
                    } else if (intWinner == 2) {
                        intP2Wins++;
                    }
                    // Asks the user if they want to play again. If they want to play again, the board will reset, the win will be counted to whom ever won.
                    // If they don't want to play again, it will leave the while loop
                    printCentered(con, "Do you want to play again? (y/n): ");
                    String strResponse = con.readLine().toLowerCase();
                    blnPlayAgain = strResponse.equals("y");
                    con.setBackgroundColor(new Color(112, 58, 255));
                }
            } else if (strChoice.equals("v")) {
                // Display the leaderboard according to the amount of wins for each player
                con.setBackgroundColor(new Color(112, 58, 255));
                viewLeaderboard(con, strP1Name, intP1Wins, strP2Name, intP2Wins);
            } else if (strChoice.equals("c")) {
                con.clear();
                // Array of the different themes the user can choose from
                String[] strThemeFiles = {
                    "classic.txt",
                    "christmas.txt",
                    "vintage.txt",
                    "galactic.txt",
                    "owntheme.txt",
                };

                con.setBackgroundColor(new Color(112, 58, 255));

                // Display available themes
                con.println("Choose a theme (in order to use owntheme.txt, must create own theme in Theme Creator menu):");
                for (int i = 0; i < strThemeFiles.length; i++) {
                    con.println((i + 1) + ". " + strThemeFiles[i]);
                }

                // Asks the user to choose the theme they want to use
                con.print("Enter the number of your theme choice: ");
                int intThemeChoice = con.readInt();

                // Input validation
                if (intThemeChoice < 1 || intThemeChoice > strThemeFiles.length) {
                    con.println("Invalid choice.");
                    con.readLine();
                } else {
                    String strSelectedTheme = strThemeFiles[intThemeChoice - 1];

                    // Save selection to lasttheme.txt
                    TextOutputFile outTheme = new TextOutputFile("lasttheme.txt");
                    outTheme.println(strSelectedTheme);
                    outTheme.close();

                    // Open the selected theme file for reading
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

                    // Assign colors to global variables
                    colP1Theme = new Color(intP1R, intP1G, intP1B);
                    colP2Theme = new Color(intP2R, intP2G, intP2B);
                    colBoardTheme = new Color(intBoardR, intBoardG, intBoardB);

                    themeIn.close();

                    // Confirms to the user what theme they selected
                    con.println("Theme \"" + strThemeName + "\" selected!");
                    con.println();
                    con.println("Press Enter to return to menu.");
                    con.readLine();
                }
            } else if (strChoice.equals("t")) {
                // Perform code to create the player's own theme
                createTheme(con);
                con.println("\nPress Enter to return to menu.");
                con.readLine();
            } else if (strChoice.equals("q")) {
                // If the user types q in main menu, the program will stop
                blnExit = true;
            } else if (strChoice.equals("s")) {
                // Secret joke menu that will display a joke
                showSecretJoke(con);
            } else {
                // If the user inputs a choice not on the menu or secret menu, it will say invalid for that input
                con.println("Invalid choice. Please try again.");
                con.readLine();
            }
        }

        con.println("Thanks for playing!");
    }

    // This method will draw the buttons for the main menu
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

    // Main game function
    public static int playGame(Console con, String strP1, String strP2, int intP1Wins, int intP2Wins) {
        // Sets a two-dimensional array for the Connect 4 board
        int[][] intBoard = new int[6][7];
        int intCurrentPlayer = 1;

        // Initialize variables for cheats
        boolean blnP1Cheat = strP1.equalsIgnoreCase("statitan");
        boolean blnP2Cheat = strP2.equalsIgnoreCase("statitan");
        boolean blnP1UsedCheat = false;
        boolean blnP2UsedCheat = false;
        
        // Board drawing dimensions
        int intBoardX = 390;
        int intdiscSize = 60;
        int intgap = 10;
        int intColWidth = intdiscSize + intgap;
        
        int intMouseButtonLastFrame = 0; // To detect a distinct click
        
        // Prints instructions for the user to follow
        String strInstruction = (intCurrentPlayer == 1 ? strP1 : strP2) + ", click a column to drop your piece.";
        
        // Draw board once to display
        drawBoard(con, intBoard, strP1, strP2, intP1Wins, intP2Wins, (intCurrentPlayer == 1 ? strP1 : strP2), strInstruction);
        // Will always run as long the main game function runs
        while (true) {
		int intCol = -1;
		boolean clicked = false;
		int prevHoveredCol = -1;
		
		// This inner loop continuously runs, updating the game state and checking for player's mouse click to drop a piece
		while (!clicked) {
			int mouseX = con.currentMouseX();
			int mouseY = con.currentMouseY();
			int hoveredCol = -1;
			
			// Check if the mouse is within the interactive area above the Connect 4 board
			if (mouseY >= 0 && mouseY < 540 && mouseX >= intBoardX && mouseX <= intBoardX + intColWidth * 7) {
				hoveredCol = (mouseX - intBoardX) / intColWidth;
			}
			if (hoveredCol != prevHoveredCol) {
				// Redraw the entire game board, including the updated arrow position
				drawBoard(con, intBoard, strP1, strP2, intP1Wins, intP2Wins, (intCurrentPlayer == 1 ? strP1 : strP2), strInstruction);
				con.repaint();
				prevHoveredCol = hoveredCol;
			}
			
			// Get the current state of the mouse buttons
			int intMouseButtonNow = con.currentMouseButton();
			if (intMouseButtonNow == 1 && intMouseButtonLastFrame == 0) {
				// If a click occurred, check if it was over a valid column
				if (hoveredCol >= 0 && hoveredCol < 7) {
					intCol = hoveredCol;
					clicked = true;
				} else {
					// If the click was outside a valid column, print a debug message.
					System.out.println("[DEBUG] Click outside valid board area: (" + mouseX + ", " + mouseY + ")");
				}
			}
			// Update intMouseButtonLastFrame
			intMouseButtonLastFrame = intMouseButtonNow;
			con.sleep(30);
		}

            // Attempts to drop a piece into the chosen column
            boolean blnPlaced = dropPiece(intBoard, intCol, intCurrentPlayer);
            if (!blnPlaced) {
                System.out.println("[DEBUG] Column " + (intCol + 1) + " is full. Player " + intCurrentPlayer + " must choose again.");
                continue;
            }

            System.out.println("[DEBUG] Player " + intCurrentPlayer + " placed a disc in column " + (intCol + 1));

            // Checks if the current player has won after dropping the piece.
            if (checkWin(intBoard, intCurrentPlayer)) {
                drawBoard(con, intBoard, strP1, strP2, intP1Wins, intP2Wins, (intCurrentPlayer == 1 ? strP1 : strP2), strInstruction);
                con.setDrawColor(new Color(0, 0, 0, 100));
                con.fillRect(0, 0, 1280, 600);

                con.setDrawColor(Color.WHITE);
                con.fillRect(0, 600, 1280, 120);

                // Winner message
                con.setDrawColor(Color.BLACK);
                String strWinnerName = (intCurrentPlayer == 1) ? strP1 : strP2;
                con.setDrawFont(FONT_WINNING);
                FontMetrics fm = con.getDrawFontMetrics();
                int textWidth = fm.stringWidth(strWinnerName + " Wins!");
                int centerX = (1280 - textWidth) / 2;
                con.drawString(strWinnerName + " Wins!", centerX, 670);
                
                con.repaint();
                con.sleep(1000);
                return intCurrentPlayer;
            }

            // Cheat code activation: If the current player's name matches and the cheat hasn't been used.
            if (intCurrentPlayer == 1 && blnP1Cheat && !blnP1UsedCheat) {
                con.println("[CHEAT] Extra turn activated for " + strP1 + "!");
                blnP1UsedCheat = true;
                con.sleep(1000);
                continue;
            } else if (intCurrentPlayer == 2 && blnP2Cheat && !blnP2UsedCheat) {
                con.println("[CHEAT] Extra turn activated for " + strP2 + "!");
                blnP2UsedCheat = true;
                con.sleep(1000);
                continue;
            }

            // Switches to the other player for the next turn
            intCurrentPlayer = (intCurrentPlayer == 1) ? 2 : 1;
            strInstruction = (intCurrentPlayer == 1 ? strP1 : strP2) + ", click a column to drop your piece.";

        }
    }

    public static boolean dropPiece(int[][] intBoard, int intCol, int intPlayer) {
        // Iterates from the bottom row upwards
        for (int intRow = 5; intRow >= 0; intRow--) {
            if (intBoard[intRow][intCol] == 0) {
                intBoard[intRow][intCol] = intPlayer;
                return true;
            }
        }
        return false;
    }

    public static void drawBoard(Console con, int[][] intBoard, String strP1, String strP2, int intP1Wins, int intP2Wins, String currentPlayerName, String instructionText) {
        con.clear();

        // Defines colors for the background elements of the game board
        Color purple = new Color(112, 58, 255);
        Color yellow = new Color(255, 214, 100);
        con.setDrawColor(purple);
        con.fillRect(0, 0, 1280, 620);
        con.setDrawColor(yellow);
        con.fillRect(0, 620, 1280, 100);

        // Draws the game title at the top, centered
        con.setDrawColor(Color.WHITE);
        printCentered(con, strGameTitle);

        // Defines dimensions and positions for player scorecards
        int intboxX = 80;
        int intboxY = 250;
        int intboxWidth = 120;
        int intboxHeight = 120;
        int intboxX2 = 1280 - intboxWidth - 80;


        // Draws Player 1's scorecard
        con.setDrawColor(Color.WHITE);
        con.fillRoundRect(intboxX, intboxY, intboxWidth, intboxHeight, 20, 20);
        con.setDrawColor(Color.BLACK);
        con.drawRoundRect(intboxX, intboxY, intboxWidth, intboxHeight, 20, 20);
        con.setDrawFont(FONT_NAME);
        drawCenteredString(con, strP1.toUpperCase(), intboxX, intboxY, intboxWidth, intboxY + 30, FONT_NAME, Color.BLACK);
        con.setDrawFont(FONT_SCORE);
        drawCenteredString(con, String.valueOf(intP1Wins), intboxX, intboxY, intboxWidth, intboxY + 75, FONT_SCORE, Color.BLACK);

        // Draws Player 2's scorecard
        con.setDrawColor(Color.WHITE);
        con.fillRoundRect(intboxX2, intboxY, intboxWidth, intboxHeight, 20, 20);
        con.setDrawColor(Color.BLACK);
        con.drawRoundRect(intboxX2, intboxY, intboxWidth, intboxHeight, 20, 20);
        con.setDrawFont(FONT_NAME);
        drawCenteredString(con, strP2.toUpperCase(), intboxX2, intboxY, intboxWidth, intboxY + 30, FONT_NAME, Color.BLACK);
        con.setDrawFont(FONT_SCORE);
        drawCenteredString(con, String.valueOf(intP2Wins), intboxX2, intboxY, intboxWidth, intboxY + 75, FONT_SCORE, Color.BLACK);

        con.println();

        // Defines dimensions for game board discs and spacing
        int intdiscSize = 60;
        int intgap = 10;
        int intColWidth = intdiscSize + intgap;

        // Sets font for column numbers
        con.setDrawFont(FONT_NUMBER);
        FontMetrics fm = con.getDrawFontMetrics();

        // Defines the top-left corner and dimensions of the Connect 4 board drawing area
        int intBoardX = 390;
        int intBoardY = 120;
        int intBoardWidth = 490;
        int intBoardHeight = 420;

        // Draws the main rectangular area of the Connect 4 board using the selected theme colour
        con.setDrawColor(colBoardTheme);
        con.fillRect(intBoardX, intBoardY, intBoardWidth, intBoardHeight);

        // Draws column numbers (1-7) above the board
        for (int intCol = 0; intCol < 7; intCol++) {
            String strcolNumber = String.valueOf(intCol + 1); // Column number as a string
            int inttextWidth = fm.stringWidth(strcolNumber); // Width of the column number string
            // Calculates the center X position for each column number
            int intcenterX = intBoardX + intCol * intColWidth + intdiscSize / 2;
            // Adjusts text X position for centering
            int inttextX = intcenterX + 6 - inttextWidth / 2;
            int inttextY = 90; // Y position for column numbers
            con.setDrawColor(Color.WHITE);
            con.drawString(strcolNumber, inttextX, inttextY);
        }

        // Draw the discs on the board based on the intBoard array's state
        for (int intRow = 0; intRow < 6; intRow++) {
            for (int intCol = 0; intCol < 7; intCol++) {
                // Calculates the X and Y coordinates for each disc
                int intX = 400 + intCol * (intdiscSize + intgap);
                int intY = 130 + intRow * (intdiscSize + intgap);

                // Sets the draw colour based on the disc's state
                if (intBoard[intRow][intCol] == 0) {
                    con.setDrawColor(Color.WHITE);
                } else if (intBoard[intRow][intCol] == 1) {
                    con.setDrawColor(colP1Theme);
                } else {
                    con.setDrawColor(colP2Theme);
                }

                con.fillOval(intX - 5, intY - 5, intdiscSize, intdiscSize);
            }
        }
        // Draw the arrow indicating the hovered column
        int mouseX = con.currentMouseX();
        int mouseY = con.currentMouseY();

        // Check if mouse is over the board area to display the arrow
        if (mouseY >= 0 && mouseY < intBoardY + intBoardHeight && mouseX >= intBoardX && mouseX <= intBoardX + intBoardWidth) {
			// Calculate the hovered column
			int hoveredCol = (mouseX - intBoardX) / intColWidth;
			if (hoveredCol >= 0 && hoveredCol < 7) {
				int arrowX = intBoardX + hoveredCol * intColWidth + (intColWidth / 2);
				int arrowY = 70; // Position the arrow just above the column numbers
				con.setDrawColor(Color.CYAN);
				int[] arrowXPoints = {arrowX, arrowX - 10, arrowX + 10};
				int[] arrowYPoints = {arrowY, arrowY + 20, arrowY + 20};
				con.fillPolygon(arrowXPoints, arrowYPoints, 3); 
			}
		}
		// Display instructions for the current player
		con.setDrawColor(Color.BLACK);
		con.setDrawFont(FONT_INSTRUCTION);
		drawCenteredString(con, instructionText, 0, 620, 1280, 670, FONT_INSTRUCTION, Color.BLACK);
    }

    public static boolean checkWin(int[][] intBoard, int intPlayer) {
        // Horizontal
        for (int intRow = 0; intRow < 6; intRow++) {
            for (int intCol = 0; intCol <= 3; intCol++) {
                if (intBoard[intRow][intCol] == intPlayer &&
                    intBoard[intRow][intCol + 1] == intPlayer &&
                    intBoard[intRow][intCol + 2] == intPlayer &&
                    intBoard[intRow][intCol + 3] == intPlayer) {
                    return true;
                }
            }
        }

        // Vertical
        for (int intCol = 0; intCol < 7; intCol++) {
            for (int intRow = 0; intRow <= 2; intRow++) {
                if (intBoard[intRow][intCol] == intPlayer &&
                    intBoard[intRow + 1][intCol] == intPlayer &&
                    intBoard[intRow + 2][intCol] == intPlayer &&
                    intBoard[intRow + 3][intCol] == intPlayer) {
                    return true;
                }
            }
        }

        // Diagonal down-right
        for (int intRow = 0; intRow <= 2; intRow++) {
            for (int intCol = 0; intCol <= 3; intCol++) {
                if (intBoard[intRow][intCol] == intPlayer &&
                    intBoard[intRow + 1][intCol + 1] == intPlayer &&
                    intBoard[intRow + 2][intCol + 2] == intPlayer &&
                    intBoard[intRow + 3][intCol + 3] == intPlayer) {
                    return true;
                }
            }
        }

        // Diagonal up-right
        for (int intRow = 3; intRow < 6; intRow++) {
            for (int intCol = 0; intCol <= 3; intCol++) {
                if (intBoard[intRow][intCol] == intPlayer &&
                    intBoard[intRow - 1][intCol + 1] == intPlayer &&
                    intBoard[intRow - 2][intCol + 2] == intPlayer &&
                    intBoard[intRow - 3][intCol + 3] == intPlayer) {
                    return true;
                }
            }
        }

        return false;
    }
    public static void viewLeaderboard(Console con, String strP1, int intP1Wins, String strP2, int intP2Wins) {
        // Initializes a 2D array to store leaderboard entries
        String[][] strLeaderboard = new String[100][2];
        int intCount = 0;

        // Writes the current players' names and scores to leaderboard.txt
        TextOutputFile leaderBoardOut = new TextOutputFile("leaderboard.txt", true);
        leaderBoardOut.println(strP1);
        leaderBoardOut.println(Integer.toString(intP1Wins));
        leaderBoardOut.println(strP2);
        leaderBoardOut.println(Integer.toString(intP2Wins));
        leaderBoardOut.close();

        // Reads all entries from leaderboard.txt into the strLeaderboard array
        TextInputFile leaderBoard = new TextInputFile("leaderboard.txt");
        while (!leaderBoard.eof() && intCount < strLeaderboard.length) {
            strLeaderboard[intCount][0] = leaderBoard.readLine();
            strLeaderboard[intCount][1] = leaderBoard.readLine();
            intCount++;
        }
        leaderBoard.close();

        // Sorts the leaderboard in descending order based on win counts using a bubble sort algorithm
        for (int i = 0; i < intCount - 1; i++) {
            for (int j = 0; j < intCount - i - 1; j++) {
                int intWins1 = Integer.parseInt(strLeaderboard[j][1]);
                int intWins2 = Integer.parseInt(strLeaderboard[j + 1][1]);
                // If the current player has fewer wins than the next, swap them
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
        // Prints each leaderboard entry to the console
        for (int i = 0; i < intCount; i++) {
            con.println((i + 1) + ". " + strLeaderboard[i][0] + " - " + strLeaderboard[i][1] + " wins");
        }
        con.println();
        con.println("Press Enter to return to menu.");
        con.readLine();
    }
    public static void createTheme(Console con) {
        con.clear();
        con.setBackgroundColor(new Color(112, 58, 255));
        con.println("Create Your Own Theme");
        con.println("---------------------------");

        con.print("Enter Theme Name: ");
        String strThemeName = con.readLine(); // Gets the theme name

        // Gets RGB for Player 1
        con.println("\nPlayer 1 Color:");
        con.print("  R: ");
        int intP1R = con.readInt();
        con.print("  G: ");
        int intP1G = con.readInt();
        con.print("  B: ");
        int intP1B = con.readInt();

        // Gets RGB for Player 2
        con.println("\nPlayer 2 Color:");
        con.print("  R: ");
        int intP2R = con.readInt();
        con.print("  G: ");
        int intP2G = con.readInt();
        con.print("  B: ");
        int intP2B = con.readInt();

        // Gets RGB for Board Background
        con.println("\nBoard Background Color:");
        con.print("  R: ");
        int intBoardR = con.readInt();
        con.print("  G: ");
        int intBoardG = con.readInt();
        con.print("  B: ");
        int intBoardB = con.readInt();

        con.print("\nEnter Game Title: ");
        String strGameTitle = con.readLine(); // Gets the game title

        // Writes all the custom theme data to owntheme.txt
        TextOutputFile own = new TextOutputFile("owntheme.txt");
        own.println(strThemeName);
        own.println(intP1R);
        own.println(intP1G);
        own.println(intP1B);
        own.println(intP2R);
        own.println(intP2G);
        own.println(intP2B);
        own.println(intBoardR);
        own.println(intBoardG);
        own.println(intBoardB);
        own.println(strGameTitle);
        own.close();
        con.println("\nTheme saved successfully to 'owntheme.txt'!"); // Confirms that the theme is saved
    }

    public static void showSecretJoke(Console con) {
        // Array of strings, each one is a programming joke
        String[] arrJokes = {
            "Why did the Java developer go broke? Because he used up all his cache!",
            "Why do programmers prefer dark mode? Because light attracts bugs!",
            "How many programmers does it take to change a light bulb? None, it's a hardware problem.",
            "I would tell you a UDP joke, but you might not get it.",
            "What's a Java developer’s favorite place to sit? The null pointer!"
        };

        Random rand = new Random();
        int randomIndex = rand.nextInt(arrJokes.length); // generates a random index to select joke

        con.clear();
        con.setBackgroundColor(new Color(0, 0, 0)); // Sets a black background for the joke display

        con.setDrawColor(new Color(0, 255, 0));
        Font jokeFont = con.loadFont("ArialNarrow7-9YJ9n.ttf", 28);
        con.setDrawFont(jokeFont);

        FontMetrics fm = con.getDrawFontMetrics();
        String strjoke = arrJokes[randomIndex];
        int textWidth = fm.stringWidth(strjoke); // Measures the width of the joke text
        int centerX = (1280 - textWidth) / 2; // Calculates X to center the text

        con.drawString(strjoke, centerX, 360);

        con.repaint(); // Ensures the screen is updated

        con.sleep(4000); // Pauses for 4 seconds so the user can read the joke
        con.setBackgroundColor(new Color(112, 58, 255));
    }

    public static void drawCenteredString(Console con, String strText, int intboxX, int intboxY, int intboxWidth, int intyPosition, Font font, Color color) {
        con.setDrawFont(font);
        FontMetrics fm = con.getDrawFontMetrics();
        int inttextWidth = fm.stringWidth(strText);
        // Calculates the X coordinate to center the text within the `intboxWidth`
        int inttextX = intboxX + (intboxWidth - inttextWidth) / 2;
        con.setDrawColor(color);
        con.drawString(strText, inttextX, intyPosition); // Draw string
    }

    public static void printCentered(Console con, String strtext) {
        int intconsoleWidth = 1280;
        int intcharWidth = 12;
        int inttextWidth = strtext.length() * intcharWidth;
        // Calculates the number of spaces needed to center the text
        int intspaces = (intconsoleWidth - inttextWidth) / (2 * intcharWidth);
        // Prints the calculated number of spaces
        for (int i = 0; i < intspaces; i++) {
            con.print(" ");
        }
        con.print(strtext); // Prints the text
    }
}
