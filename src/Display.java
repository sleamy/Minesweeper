import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.swing.JPanel;


public class Display extends JPanel {
		
	public static final int TILESIZE = 32;
	public static final int MINETOKEN = 9;
	public static int width = 8, height = 8;
	public static int numOfMines = 10;
	
	// Change these to array of tiles or figure out how to use revealed for flags
	public int[][] board = new int[width][height];
	public int[][] revealed = new int[width][height];
	
	public Random random;
	
	public Images images;
	
	public Display() {
	
		this.setPreferredSize(new Dimension((width * TILESIZE), (height * TILESIZE)));
		
		random = new Random();
		
		images = new Images();
		
		initBoard();
		addListeners();

		System.out.println("Width: " + (width * TILESIZE)+ ", Height: " + (height * TILESIZE));
	}
	
	public void initBoard() {
		setRevealed();
		placeMines();
		setNumbers();
		System.out.println("Game is setup!");
	}
	
	public void setRevealed() {
		for(int col = 0; col < width; col++) {
			for (int row = 0; row < height; row++) {
				revealed[col][row] = -1;
			}
		}
	}
	
	public void fillBoard() {
		for(int col = 0; col < width; col++) {
			for (int row = 0; row < height; row++) {
				if(board[col][row] != MINETOKEN)
					board[col][row] = countNearbyMines(col, row);
			}
		}
	}
	
	public void placeMines() {
		
		int minesPlaced = 0;
		
		while(minesPlaced < numOfMines) {
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			
			if(board[x][y] == 0) {
				board[x][y] = MINETOKEN;
				minesPlaced++;
			}
		}
		
	}
	
	public void setNumbers() {
		
		for(int col = 0; col < width; col++) {
			for (int row = 0; row < height; row++) {
				if(board[col][row] != MINETOKEN)
					board[col][row] = countNearbyMines(col, row);
			}
		}
		
	}
	
	public int countNearbyMines(int x, int y) {
		
		int nearbyMines = 0;
		
		//check top
		if(y > 0) {
			if(board[x][y - 1] == MINETOKEN) nearbyMines++;
		}
		
		//check top-right
		if(y > 0 && x < width - 1) {
			if(board[x + 1][y - 1] == MINETOKEN) nearbyMines++;
		}
		
		//check right
		if(x < width - 1) {
			if(board[x + 1][y] == MINETOKEN) nearbyMines++;
		}
		
		//check bottom-right
		if(x < width -1 && y < height - 1) {
			if(board[x + 1][y + 1] == MINETOKEN) nearbyMines++;
		}
		
		//check bottom
		if(y < height - 1) {
			if(board[x][y + 1] == MINETOKEN) nearbyMines++;
		}
		
		//check bottom-left
		if(x > 0 && y < height - 1) {
			if(board[x - 1][y + 1] == MINETOKEN) nearbyMines++;
		}
		
		//check left
		if(x > 0) {
			if(board[x - 1][y] == MINETOKEN) nearbyMines++;
		}
		
		//check top-left
		if(x > 0 && y > 0) {
			if(board[x - 1][y - 1] == MINETOKEN) nearbyMines++;
		}
		
		return nearbyMines;
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
		System.out.println("Board[x][y] = " + board[x][y] + ", revealed[x][y] = " + revealed[x][y]);
		revealed[x][y] = board[x][y];
		repaint();
	}
	
	public void paintComponent(Graphics g) {
		
		g.fillRect(0, 0, width * TILESIZE, height * TILESIZE);
		g.setFont(new Font("Courier", Font.BOLD, 20));
		
		for(int col = 0; col < width; col++) {
			for (int row = 0; row < height; row++) {
				switch(revealed[col][row]) {
				case MINETOKEN:
					g.drawImage(Images.mine, col * TILESIZE, row * TILESIZE, TILESIZE, TILESIZE, null);
					break;
				case 1:
					g.drawImage(Images.one, col * TILESIZE, row * TILESIZE, TILESIZE, TILESIZE, null);
					break;
				case 2:
					g.drawImage(Images.two, col * TILESIZE, row * TILESIZE, TILESIZE, TILESIZE, null);
					break;
				case 3:
					g.drawImage(Images.three, col * TILESIZE, row * TILESIZE, TILESIZE, TILESIZE, null);
					break;
				case 4:
					g.drawImage(Images.four, col * TILESIZE, row * TILESIZE, TILESIZE, TILESIZE, null);
					break;
				case 5:
					g.drawImage(Images.five, col * TILESIZE, row * TILESIZE, TILESIZE, TILESIZE, null);
					break;
				case 6:
					g.drawImage(Images.six, col * TILESIZE, row * TILESIZE, TILESIZE, TILESIZE, null);
					break;
				case 7:
					g.drawImage(Images.seven, col * TILESIZE, row * TILESIZE, TILESIZE, TILESIZE, null);
					break;
				case 8:
					g.drawImage(Images.eight, col * TILESIZE, row * TILESIZE, TILESIZE, TILESIZE, null);
					break;
				case 0:
					g.drawImage(Images.pressed, col * TILESIZE, row * TILESIZE, TILESIZE, TILESIZE, null);
					break;
				case -1:
					g.drawImage(Images.unpressed, col * TILESIZE, row * TILESIZE, TILESIZE, TILESIZE, null);
					break;
				case -2:
					g.drawImage(Images.flag, col * TILESIZE, row * TILESIZE, TILESIZE, TILESIZE, null);
				default:
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
				System.out.println("X: " + e.getX() + ", Y: " + e.getY());
				reveal(e.getX() / TILESIZE, e.getY() / TILESIZE);
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
