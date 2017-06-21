import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Random;

import javax.swing.JPanel;


public class Display extends JPanel {

	public static final int TILESIZE = 40;
	public static final int BOMBTOKEN = 9;
	public static int width = 8, height = 8;
	public static int numOfBombs = 10;
	public int[][] board = new int[width][height];
	
	public Random random;
	
	public Display() {
	
		this.setPreferredSize(new Dimension((width * TILESIZE) + 1, (height * TILESIZE) + 1));
		System.out.println("height: " + height * TILESIZE + ", width: " + width * TILESIZE);
		
		random = new Random();
		
		initBoard();
	}
	
	public void initBoard() {
		placeBombs();
		setNumbers();
		displayBoardInConsole();
	}
	
	public void placeBombs() {
		
		int bombsPlaced = 0;
		
		while(bombsPlaced < numOfBombs) {
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			
			if(board[x][y] == 0) {
				board[x][y] = BOMBTOKEN;
				bombsPlaced++;
			}
		}
		
		
	}
	
	public void setNumbers() {
		
		for(int col = 0; col < width; col++) {
			for (int row = 0; row < height; row++) {
				System.out.println("Row: " + row + ", Col: " + col);
				if(board[col][row] != BOMBTOKEN)
					board[col][row] = countNearbyBombs(col, row);
			}
		}
		
	}
	
	public int countNearbyBombs(int x, int y) {
		
		System.out.println(x + ", " + y);
		
		int nearbyBombs = 0;
		
		//check top
		if(y > 0) {
			if(board[x][y - 1] == BOMBTOKEN) nearbyBombs++;
		}
		
		//check top-right
		if(y > 0 && x < width - 1) {
			if(board[x + 1][y - 1] == BOMBTOKEN) nearbyBombs++;
		}
		
		//check right
		if(x < width - 1) {
			if(board[x + 1][y] == BOMBTOKEN) nearbyBombs++;
		}
		
		//check bottom-right
		if(x < width -1 && y < height - 1) {
			if(board[x + 1][y + 1] == BOMBTOKEN) nearbyBombs++;
		}
		
		//check bottom
		if(y < height - 1) {
			if(board[x][y + 1] == BOMBTOKEN) nearbyBombs++;
		}
		
		//check bottom-left
		if(x > 0 && y < height - 1) {
			if(board[x - 1][y + 1] == BOMBTOKEN) nearbyBombs++;
		}
		
		//check left
		if(x > 0) {
			if(board[x - 1][y] == BOMBTOKEN) nearbyBombs++;
		}
		
		//check top-left
		if(x > 0 && y > 0) {
			if(board[x - 1][y - 1] == BOMBTOKEN) nearbyBombs++;
		}
		
		return nearbyBombs;
	}
	
	public void displayBoardInConsole() {
		
		for(int col = 0; col < width; col++) {
			for (int row = 0; row < height; row++) {
				System.out.print(board[row][col]);
			}
			System.out.println();
		}
		
	}
	
	public void paintComponent(Graphics g) {
		
		g.fillRect(0, 0, width * TILESIZE, height * TILESIZE);
		
		for(int col = 0; col < width; col++) {
			for (int row = 0; row < height; row++) {
				if(board[row][col] == BOMBTOKEN) {
					g.setColor(Color.RED);
					g.fillRect(row * TILESIZE, col * TILESIZE, TILESIZE, TILESIZE);
					g.setColor(Color.BLACK);
					g.drawRect(row * TILESIZE, col * TILESIZE, TILESIZE, TILESIZE);
				} else {
					g.setColor(Color.LIGHT_GRAY);
					g.fillRect(row * TILESIZE, col * TILESIZE, TILESIZE, TILESIZE);
					g.setColor(Color.BLACK);
					g.drawRect(row * TILESIZE, col * TILESIZE, TILESIZE, TILESIZE);
					g.drawString(Integer.toString(board[row][col]), (row * TILESIZE) + 17, (col * TILESIZE) + 25);
				}
			}
		}
		
		
	}
	
}
