import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Display extends JPanel {

	public static final int MINETOKEN = 9;
	public static final int FLAGTOKEN = -2;
	public static final int CLICKEDMINE = -3;
	public static final int CROSSTOKEN = -4;

	public static final int SCALE = 2;
	public static final int TILESIZE = 16 * SCALE;
	public static int width = 9, height = 9;
	public static int numOfMines = 10;
	public static int flagsRemaining = numOfMines;

	public int[][] board = new int[width][height];
	public int[][] revealed = new int[width][height];

	public Random random;
	public Images images;

	public boolean gameOver = false;
	public boolean leftMousePressed = false;
	public boolean middleMousePressed = false;
	public boolean rightMousePressed = false;

	public Display() {

		this.setFocusable(true);
		
		init();
		setupBoard();
		addListeners();
	}

	public void init() {
		this.setPreferredSize(new Dimension((width * TILESIZE), (height * TILESIZE)));
		random = new Random();
		images = new Images();
	}

	public void setupBoard() {
		initBoard();
		initRevealed();
		placeMines(numOfMines);
		setNumbers();
		System.out.println("Game is setup!");
	}
	
	public void reset() {
		setupBoard();
		gameOver = false;
		repaint();
		System.out.println("Game reset!");
	}
	
	public void initBoard() {
		for (int col = 0; col < width; col++) {
			for (int row = 0; row < height; row++) {
				board[col][row] = 0;
			}
		}
	}

	/**
	 * Initialises all elements of the revealed array to -1.
	 */
	public void initRevealed() {
		for (int col = 0; col < width; col++) {
			for (int row = 0; row < height; row++) {
				revealed[col][row] = -1;
			}
		}
	}

	/**
	 * Sets each tile to have a number corresponding to the 
	 * number of bombs surrounding each tile.
	 */
	public void fillBoard() {
		for (int col = 0; col < width; col++) {
			for (int row = 0; row < height; row++) {
				if (board[col][row] != MINETOKEN)
					board[col][row] = countNearbyMines(col, row);
			}
		}
	}

	/**
	 * Places n mines randomly on the grid.
	 * 
	 * @param n
	 *            The number of mines to be placed.
	 */
	public void placeMines(int n) {

		int minesPlaced = 0;

		while (minesPlaced < n) {
			int x = random.nextInt(width);
			int y = random.nextInt(height);

			if (board[x][y] == 0) {
				board[x][y] = MINETOKEN;
				minesPlaced++;
			}
		}

	}

	/**
	 * Counts the number of mines surrounding each tile and allocates that
	 * number to each tile.
	 */
	public void setNumbers() {

		for (int col = 0; col < width; col++) {
			for (int row = 0; row < height; row++) {
				if (board[col][row] != MINETOKEN)
					board[col][row] = countNearbyMines(col, row);
			}
		}

	}

	/**
	 * For a specific tile, checks the surrounding tiles for mines and returns
	 * the number of mines found.
	 * 
	 * @param x
	 *            The x position of the specified tile.
	 * @param y
	 *            The y position of the specified tile.
	 * @return The number of mines found.
	 */
	public int countNearbyMines(int x, int y) {

		int nearbyMines = 0;

		// check top
		if (y > 0) {
			if (board[x][y - 1] == MINETOKEN)
				nearbyMines++;
		}

		// check top-right
		if (y > 0 && x < width - 1) {
			if (board[x + 1][y - 1] == MINETOKEN)
				nearbyMines++;
		}

		// check right
		if (x < width - 1) {
			if (board[x + 1][y] == MINETOKEN)
				nearbyMines++;
		}

		// check bottom-right
		if (x < width - 1 && y < height - 1) {
			if (board[x + 1][y + 1] == MINETOKEN)
				nearbyMines++;
		}

		// check bottom
		if (y < height - 1) {
			if (board[x][y + 1] == MINETOKEN)
				nearbyMines++;
		}

		// check bottom-left
		if (x > 0 && y < height - 1) {
			if (board[x - 1][y + 1] == MINETOKEN)
				nearbyMines++;
		}

		// check left
		if (x > 0) {
			if (board[x - 1][y] == MINETOKEN)
				nearbyMines++;
		}

		// check top-left
		if (x > 0 && y > 0) {
			if (board[x - 1][y - 1] == MINETOKEN)
				nearbyMines++;
		}

		return nearbyMines;
	}

	/**
	 * A recursive function that takes a blank and then reveals
	 * all blanks surrounding it. This function is called on each
	 * blank surrounding the initial blank tile until all blanks
	 * are surrounded by number tiles.
	 * 
	 * @param x		The x position of the initial tile.
	 * @param y		The y position of the initial tile.
	 */
	public void revealBlank(int x, int y) {

		revealed[x][y] = board[x][y];
		
		// check top
		if (y > 0) {
			if (board[x][y - 1] == 0 && revealed[x][y - 1] != 0) {
				revealBlank(x, y - 1);
			} else if(board[x][y - 1] > 0 && revealed[x][y - 1] == -1) {
				revealed[x][y - 1] = board[x][y - 1];
			}
		}

		// check top-right
		if (y > 0 && x < width - 1) {
			if (board[x + 1][y - 1] == 0 && revealed[x + 1][y - 1] != 0) {
				revealBlank(x + 1, y - 1);
			} else if(board[x + 1][y - 1] > 0 && revealed[x + 1][y - 1] == -1) {
				revealed[x + 1][y - 1] = board[x + 1][y - 1];
			}
		}

		// check right
		if (x < width - 1) {
			if (board[x + 1][y] == 0 && revealed[x + 1][y] != 0) {
				revealBlank(x + 1, y);
			} else if(board[x + 1][y] > 0 && revealed[x + 1][y] == -1) {
				revealed[x + 1][y] = board[x + 1][y];
			}
		}

		// check bottom-right
		if (x < width - 1 && y < height - 1) {
			if (board[x + 1][y + 1] == 0 && revealed[x + 1][y + 1] != 0) {
				revealBlank(x + 1, y + 1);
			} else if(board[x + 1][y + 1] > 0 && revealed[x + 1][y + 1] == -1) {
				revealed[x + 1][y + 1] = board[x + 1][y + 1];
			}
		}

		// check bottom
		if (y < height - 1) {
			if (board[x][y + 1] == 0 && revealed[x][y + 1] != 0) {
				revealBlank(x, y + 1);
			} else if(board[x][y + 1] > 0 && revealed[x][y + 1] == -1) {
				revealed[x][y + 1] = board[x][y + 1];
			}
		}

		// check bottom-left
		if (x > 0 && y < height - 1) {
			if (board[x - 1][y + 1] == 0 && revealed[x - 1][y + 1] != 0) {
				revealBlank(x - 1, y + 1);
			} else if(board[x - 1][y + 1] > 0 && revealed[x - 1][y + 1] == -1) {
				revealed[x - 1][y + 1] = board[x - 1][y + 1];
			}
		}

		// check left
		if (x > 0) {
			if (board[x - 1][y] == 0 && revealed[x - 1][y] != 0) {
				revealBlank(x - 1, y);
			} else if(board[x - 1][y] > 0 && revealed[x - 1][y] == -1) {
				revealed[x - 1][y] = board[x - 1][y];
			}
		}

		// check top-left
		if (x > 0 && y > 0) {
			if (board[x - 1][y - 1] == 0 && revealed[x - 1][y - 1] != 0) {
				revealBlank(x - 1, y - 1);
			} else if(board[x - 1][y - 1] > 0 && revealed[x - 1][y - 1] == -1) {
				revealed[x - 1][y - 1] = board[x - 1][y - 1];
			}
		}

	}

	public void displayBoardInConsole() {

		for (int col = 0; col < width; col++) {
			for (int row = 0; row < height; row++) {
				System.out.print(board[col][row]);
			}
			System.out.println();
		}

	}

	public void showMines() {
		for (int col = 0; col < width; col++) {
			for (int row = 0; row < height; row++) {
				if (board[col][row] == MINETOKEN) {
					if (revealed[col][row] != FLAGTOKEN) {
						revealed[col][row] = MINETOKEN;
					}
				}
			}
		}
	}

	public void showWrongFlags() {
		for (int col = 0; col < width; col++) {
			for (int row = 0; row < height; row++) {
				if (revealed[col][row] == FLAGTOKEN && board[col][row] != MINETOKEN) {
					revealed[col][row] = CROSSTOKEN;
				}
			}
		}
	}

	/**
	 * Changes the tile at location (x, y) in the revealed array to be the same
	 * as the value in the board array then repaint the board on screen.
	 * 
	 * @param x
	 *            The x position of the tile that was clicked on.
	 * @param y
	 *            The y position of the tile that was clicked on.
	 */
	public void reveal(int x, int y) {

		if (board[x][y] == MINETOKEN && revealed[x][y] != FLAGTOKEN) {
			gameOver();
			showMines();
			showWrongFlags();
			revealed[x][y] = CLICKEDMINE;
		} else if (revealed[x][y] == FLAGTOKEN) {
			return;
		} else if (revealed[x][y] == -1) {
			if (board[x][y] == 0) {
				revealBlank(x, y);
			} else {
				revealed[x][y] = board[x][y];
			}

		}

		repaint();
	}

	public void flag(int x, int y) {
		if (revealed[x][y] == -1) {
			revealed[x][y] = FLAGTOKEN;
			flagsRemaining--;
		} else if (revealed[x][y] == FLAGTOKEN) {
			revealed[x][y] = -1;
			flagsRemaining++;
		}
		repaint();
	}

	public void gameOver() {
		gameOver = true;
		System.out.println("You lose!");
	}

	public void paintComponent(Graphics g) {

		g.setFont(new Font("Courier", Font.BOLD, 20));

		clear(g);
		paintBoard(g);
	}

	/**
	 * Draws an image corresponding to the values of the revealed array.
	 * 
	 * @param g
	 *            The Graphics object.
	 */
	public void paintBoard(Graphics g) {

		for (int col = 0; col < width; col++) {
			for (int row = 0; row < height; row++) {
				switch (revealed[col][row]) {
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
				case FLAGTOKEN:
					g.drawImage(Images.flag, col * TILESIZE, row * TILESIZE, TILESIZE, TILESIZE, null);
					break;
				case CLICKEDMINE:
					g.drawImage(Images.clickedMine, col * TILESIZE, row * TILESIZE, TILESIZE, TILESIZE, null);
					break;
				case CROSSTOKEN:
					g.drawImage(Images.cross, col * TILESIZE, row * TILESIZE, TILESIZE, TILESIZE, null);
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
	 * Determines if the position of a click is within the bounds of the window.
	 * 
	 * @param x
	 *            The x position of the click.
	 * @param y
	 *            The y position of the click.
	 * @return If the coords of the click are within the window, return true.
	 *         Otherwise return false.
	 */
	public boolean inWindow(int x, int y) {
		return ((x >= 0 && x < width) && (y >= 0 && y < height)) ? true : false;
	}

	public void addListeners() {
		
		this.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_R) {
					reset();
				}
			}
			
		});		
		
		this.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {

			}

			@Override
			public void mousePressed(MouseEvent e) {
				if (!gameOver) {
					if (SwingUtilities.isLeftMouseButton(e)) {
						leftMousePressed = true;
					} else if (SwingUtilities.isMiddleMouseButton(e)) {
						middleMousePressed = true;
					} else if (SwingUtilities.isRightMouseButton(e)) {
						rightMousePressed = true;
					}
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {

				if (!gameOver) {
					int x = e.getX() / TILESIZE;
					int y = e.getY() / TILESIZE;

					if (SwingUtilities.isLeftMouseButton(e)) {
						leftMousePressed = false;
						if (inWindow(x, y))
							reveal(x, y);
					} else if (SwingUtilities.isMiddleMouseButton(e)) {
						middleMousePressed = false;
					} else if (SwingUtilities.isRightMouseButton(e)) {
						rightMousePressed = false;
						if (inWindow(x, y))
							flag(x, y);
					}
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
