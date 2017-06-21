import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;

import javax.swing.JPanel;


public class Display extends JPanel {
		
	public static final int TILESIZE = 40;
	public static final int BOMBTOKEN = 9;
	public static int width = 20, height = 8;
	public static int numOfBombs = 10;
	public int[][] board = new int[width][height];
	public int[][] revealed = new int[width][height];
	
	public Random random;
	
	public Display() {
	
		this.setPreferredSize(new Dimension((width * TILESIZE) + 1, (height * TILESIZE) + 1));
		
		random = new Random();
		
		initBoard();
		addListeners();
		
	}
	
	public void initBoard() {
		placeBombs();
		setNumbers();
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
				if(board[col][row] != BOMBTOKEN)
					board[col][row] = countNearbyBombs(col, row);
			}
		}
		
	}
	
	public int countNearbyBombs(int x, int y) {
		
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
				System.out.print(board[col][row]);
			}
			System.out.println();
		}
		
	}
	
	public void reveal(int x, int y) {
		revealed[x][y] = board[x][y];
		repaint();
	}
	
	public void paintComponent(Graphics g) {
		
		g.fillRect(0, 0, width * TILESIZE, height * TILESIZE);
		
		for(int col = 0; col < width; col++) {
			for (int row = 0; row < height; row++) {
				if(revealed[col][row] == BOMBTOKEN) {
					g.setColor(Color.RED);
					g.fillRect(col * TILESIZE, row * TILESIZE, TILESIZE, TILESIZE);
					g.setColor(Color.BLACK);
					g.drawRect(col * TILESIZE, row * TILESIZE, TILESIZE, TILESIZE);
				} else {
					g.setColor(Color.LIGHT_GRAY);
					g.fillRect(col * TILESIZE, row * TILESIZE, TILESIZE, TILESIZE);
					g.setColor(Color.BLACK);
					g.drawRect(col * TILESIZE, row * TILESIZE, TILESIZE, TILESIZE);
					//g.drawString(Integer.toString(board[col][row]), (col * TILESIZE) + 17, (row * TILESIZE) + 25);
				}
			}
		}
	}
	
	public void addListeners() {
		
		this.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				reveal(e.getX() / 40, e.getY() / 40);
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				
			}
		
		});
	}
	
}
