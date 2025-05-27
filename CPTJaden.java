import arc.*;

public class CPTJaden {
	
	public static void main (String[] args) {
		Console con = new Console();

        // Create a 6x7 board
        int[][] board = new int[6][7];
        con.println("Connect 4");
        
        // Print column numbers
        for (int intCol = 1; intCol <= 7; intCol++) {
            con.print(" " + intCol + " ");
        }
        con.println();
        
        for (int intRow = 0; intRow < 6; intRow++) {
            for (int intCol = 0; intCol < 7; intCol++) {
                con.print("[ ]");
            }
            con.println();
        }
    }
}
