import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;


public class Display extends JPanel {
		
	public static final int TILESIZE = 32;
	public static final int MINETOKEN = 9;
	public static int width = 9, height = 9;
	public static int numOfMines = 10;

	public int[][] board = new int[width][height];
	public int[][] revealed = new int[width][height];
	
	public Random random;	
	public Images images;
	
	public boolean leftMousePressed = false;
	public boolean middleMousePressed = false;
	public boolean rightMousePressed = false;
	
	public Display() {
		
		init();
		initBoard();
		addListeners();
	}
	
	public void init() {
		
		this.setPreferredSize(new Dimension((width * TILESIZE), (height * TILESIZE)));		
		random = new Random();		
		images = new Images();
		
	}
	
	public void initBoard() {
		initRevealed();
		placeMines(numOfMines);
		setNumbers();
		System.out.println("Game is setup!");
	}
	
	
	/**
	 * 	Initialises all elements of the revealed array to -1.
	 */
	public void initRevealed() {
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
	
	/**
	 * Places n mines randomly on the grid.
	 * 
	 * @param n	The number of mines to be placed.
	 */
	public void placeMines(int n) {
		
		int minesPlaced = 0;
		
		while(minesPlaced < n) {
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			
			if(board[x][y] == 0) {
				board[x][y] = MINETOKEN;
				minesPlaced++;
			}
		}
		
	}
	
	/**
	 * Counts the number of mines surrounding each tile and allocates
	 * that number to each tile.
	 */
	public void setNumbers() {
		
		for(int col = 0; col < width; col++) {
			for (int row = 0; row < height; row++) {
				if(board[col][row] != MINETOKEN)
					board[col][row] = countNearbyMines(col, row);
			}
		}
		
	}
	
	/**
	 * For a specific tile, checks the surrounding tiles for mines
	 * and returns the number of mines found.
	 * 
	 * @param x		The x position of the specified tile.
	 * @param y		The y position of the specified tile.
	 * @return		The number of mines found.
	 */
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
	
	/**
	 * Changes the tile at location (x, y) in the revealed array
	 * to be the same as the value in the board array then repaint
	 * the board on screen.
	 * 
	 * @param x		The x position of the tile that was clicked on.
	 * @param y		The y position of the tile that was clicked on.
	 */
	public void reveal(int x, int y) {
		
		if (revealed[x][y] == MINETOKEN) {
			System.out.println("You lose!");
			// Reveal bombs & incorrect flags
		} else if (revealed[x][y] == -2 || revealed[x][y] == -3) {
			return;
		} else if (revealed[x][y] == -1) {
			revealed[x][y] = board[x][y];			
		}
		
		repaint();
	}
	
	public void flag(int x, int y) {
		if(revealed[x][y] == -1) {
			revealed[x][y] = -2;
		} else if (revealed[x][y] == -2) {
			revealed[x][y] = -1;
		}
		repaint();
	}
	
	public void paintComponent(Graphics g) {

		g.setFont(new Font("Courier", Font.BOLD, 20));
		
		clear(g);
		paintBoard(g);
	}
	
	/**
	 * Draws an image corresponding to the values of the revealed array.
	 * 
	 * @param g		The Graphics object.
	 */
	public void paintBoard(Graphics g) {
		
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
				case -3:
					// Question mark box.
					break;
				default:
					// Nothing.
				}
			}
		}
	}
	
	/**
	 * Fills the screen with a black rectangle.
	 * 
	 * @param g
	 */
	public void clear(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, width * TILESIZE, height * TILESIZE);
	}
	
	/**
	 * Determines if the position of a click is within the 
	 * bounds of the window.
	 * 
	 * @param x		The x position of the click.
	 * @param y		The y position of the click.
	 * @return		If the coords of the click are within
	 * 				the window, return true. Otherwise 
	 * 				return false.
	 */
	public boolean inWindow(int x, int y) {		
		return ((x >= 0 && x < width) && (y >= 0 && y < height)) ? true : false;
	}
	
	public void addListeners() {
		
		this.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				if(SwingUtilities.isLeftMouseButton(e)) {
					leftMousePressed = true;
				} else if(SwingUtilities.isMiddleMouseButton(e)) {
					middleMousePressed = true;
				} else if(SwingUtilities.isRightMouseButton(e)) {
					rightMousePressed = true;
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				
				int x = e.getX() / TILESIZE;
				int y = e.getY() / TILESIZE;
				
				if(SwingUtilities.isLeftMouseButton(e)) {
					leftMousePressed = false;
					if(inWindow(x, y)) reveal(x, y);
				} else if(SwingUtilities.isMiddleMouseButton(e)) {
					middleMousePressed = false;
				} else if(SwingUtilities.isRightMouseButton(e)) {
					rightMousePressed = false;
					if(inWindow(x, y)) flag(x, y);
				}
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
