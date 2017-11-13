import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Display extends JPanel {

	private static final long serialVersionUID = 1L;

	public int scale = 1;
	
	// Values for non-number tiles
	public static final int MINETOKEN = 9;
	public static final int FLAGTOKEN = -2;
	public static final int CLICKEDMINE = -3;
	public static final int CROSSTOKEN = -4;

	public int padding = 2 * scale;
	public int borderSize = 10 * scale;
	public int topHeight = 50 * scale;
	public int tileSize = 16 * scale;
	public int boxWidth = 40 * scale;
	
	public int width = 9;
	public int height = 9;
	public int numOfMines = 10;
	public int flagsRemaining = numOfMines;
	
	// Values for the smiley button
	public int smileySize = 26 * scale;
	public int smileyTopLeftX;
	public int smileyTopLeftY;
	public int smileyBottomRightX;
	public int smileyBottomRightY;
	
	public int secondsPassed;
	
	public BufferedImage smileyIcon;

	public DecimalFormat df = new DecimalFormat("000");

	public int[][] board;
	public int[][] revealed;

	public Random random;
	public Images images;

	public boolean gameOver = false;
	public boolean leftMousePressed = false;
	public boolean middleMousePressed = false;
	public boolean rightMousePressed = false;
	
	public Timer timer;
	public TimerTask task;
	public boolean timerStarted = false;
	
	public Mode mode = Mode.BEGINNER;
	public Scale cScale = Scale.ONE;
	
	public enum Mode {
		BEGINNER, INTERMEDIATE, EXPERT
	}
	
	public enum Scale {
		ONE, TWO, THREE, FOUR
	}

	public Display() {

		this.setFocusable(true);

		init();
		setupBoard();
		addListeners();
	}
	

	public void init() {
		this.setPreferredSize(new Dimension((width * tileSize + (borderSize * 2)), (height * tileSize) + (topHeight + borderSize)));
		random = new Random();
		images = new Images();
		timer = new Timer();
	}
	
	public void reinitScale() {
		padding = 2 * scale;
		borderSize = 10 * scale;
		topHeight = 50 * scale;
		tileSize = 16 * scale;
		boxWidth = 40 * scale;
		initSmiley();
	}
	
	public void initSmiley() {
		smileySize = 26 * scale;
		smileyTopLeftX = borderSize + ((width * tileSize) / 2) - (smileySize / 2);
		smileyTopLeftY = borderSize + padding;
		smileyIcon = Images.smileyUnpressed;
	}
	
	/**
	 * Function used for resizing window on change of difficulty or scale.
	 */
	public void resetSize() {
		this.setPreferredSize(new Dimension((width * tileSize + (borderSize * 2)), (height * tileSize) + (topHeight + borderSize)));
	}

	/**
	 * Initialised the board array to contain mines and the number of mines
	 * surrouding each tile and sets the revealed array to have all values 
	 * set to -1.
	 */
	public void setupBoard() {
		initBoard();
		initRevealed();
		placeMines(numOfMines);
		setNumbers();
		initSmiley();
	}

	/**
	 * Resets the game.
	 */
	public void reset() {
		setupBoard();
		flagsRemaining = numOfMines;
		secondsPassed = 0;
		if(timerStarted) {
			timer.cancel();
			timer.purge();
			timerStarted = false;
		}
		gameOver = false;
		repaint();
	}
	
	/**
	 * Changes the values for the number of tiles of the game
	 * and the number of mines to be placed in the board then
	 * resets the game.
	 * 
	 * @param width		The number of tiles in a row.
	 * @param height	The number of tiles in a column.
	 * @param mines		The number of mines placed on the board.
	 */
	public void setMode(int width, int height, int mines) {
		this.width = width;
		this.height = height;
		this.numOfMines = mines;
		reset();
	}
	
	public void setScale(int scale) {
		this.scale = scale;
		repaint();
	}

	/**
	 * Initialises all values of the board array to be 0.
	 */
	public void initBoard() {
		board = new int[width][height];
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
		revealed = new int[width][height];
		for (int col = 0; col < width; col++) {
			for (int row = 0; row < height; row++) {
				revealed[col][row] = -1;
			}
		}
	}

	/**
	 * Sets each tile to have a number corresponding to the number of bombs
	 * surrounding each tile.
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
	 * For a specific tile, checks the surrounding tiles for flags and returns
	 * the number of flags found.
	 * 
	 * @param x
	 *            The x position of the specified tile.
	 * @param y
	 *            The y position of the specified tile.
	 * @return The number of mines found.
	 */
	public int countNearbyFlags(int x, int y) {

		int nearbyFlags = 0;

		// check top
		if (y > 0) {
			if (revealed[x][y - 1] == FLAGTOKEN)
				nearbyFlags++;
		}

		// check top-right
		if (y > 0 && x < width - 1) {
			if (revealed[x + 1][y - 1] == FLAGTOKEN)
				nearbyFlags++;
		}

		// check right
		if (x < width - 1) {
			if (revealed[x + 1][y] == FLAGTOKEN)
				nearbyFlags++;
		}

		// check bottom-right
		if (x < width - 1 && y < height - 1) {
			if (revealed[x + 1][y + 1] == FLAGTOKEN)
				nearbyFlags++;
		}

		// check bottom
		if (y < height - 1) {
			if (revealed[x][y + 1] == FLAGTOKEN)
				nearbyFlags++;
		}

		// check bottom-left
		if (x > 0 && y < height - 1) {
			if (revealed[x - 1][y + 1] == FLAGTOKEN)
				nearbyFlags++;
		}

		// check left
		if (x > 0) {
			if (revealed[x - 1][y] == FLAGTOKEN)
				nearbyFlags++;
		}

		// check top-left
		if (x > 0 && y > 0) {
			if (revealed[x - 1][y - 1] == FLAGTOKEN)
				nearbyFlags++;
		}

		return nearbyFlags;
	}

	/**
	 * A recursive function that takes a blank and then reveals all blanks
	 * surrounding it. This function is called on each blank surrounding the
	 * initial blank tile until all blanks are surrounded by number tiles.
	 * 
	 * @param x
	 *            The x position of the initial tile.
	 * @param y
	 *            The y position of the initial tile.
	 */
	public void revealBlank(int x, int y) {

		revealed[x][y] = board[x][y];

		// check top
		if (y > 0) {
			if (board[x][y - 1] == 0 && revealed[x][y - 1] != 0) {
				revealBlank(x, y - 1);
			} else if (board[x][y - 1] > 0 && revealed[x][y - 1] == -1) {
				revealed[x][y - 1] = board[x][y - 1];
			}
		}

		// check top-right
		if (y > 0 && x < width - 1) {
			if (board[x + 1][y - 1] == 0 && revealed[x + 1][y - 1] != 0) {
				revealBlank(x + 1, y - 1);
			} else if (board[x + 1][y - 1] > 0 && revealed[x + 1][y - 1] == -1) {
				revealed[x + 1][y - 1] = board[x + 1][y - 1];
			}
		}

		// check right
		if (x < width - 1) {
			if (board[x + 1][y] == 0 && revealed[x + 1][y] != 0) {
				revealBlank(x + 1, y);
			} else if (board[x + 1][y] > 0 && revealed[x + 1][y] == -1) {
				revealed[x + 1][y] = board[x + 1][y];
			}
		}

		// check bottom-right
		if (x < width - 1 && y < height - 1) {
			if (board[x + 1][y + 1] == 0 && revealed[x + 1][y + 1] != 0) {
				revealBlank(x + 1, y + 1);
			} else if (board[x + 1][y + 1] > 0 && revealed[x + 1][y + 1] == -1) {
				revealed[x + 1][y + 1] = board[x + 1][y + 1];
			}
		}

		// check bottom
		if (y < height - 1) {
			if (board[x][y + 1] == 0 && revealed[x][y + 1] != 0) {
				revealBlank(x, y + 1);
			} else if (board[x][y + 1] > 0 && revealed[x][y + 1] == -1) {
				revealed[x][y + 1] = board[x][y + 1];
			}
		}

		// check bottom-left
		if (x > 0 && y < height - 1) {
			if (board[x - 1][y + 1] == 0 && revealed[x - 1][y + 1] != 0) {
				revealBlank(x - 1, y + 1);
			} else if (board[x - 1][y + 1] > 0 && revealed[x - 1][y + 1] == -1) {
				revealed[x - 1][y + 1] = board[x - 1][y + 1];
			}
		}

		// check left
		if (x > 0) {
			if (board[x - 1][y] == 0 && revealed[x - 1][y] != 0) {
				revealBlank(x - 1, y);
			} else if (board[x - 1][y] > 0 && revealed[x - 1][y] == -1) {
				revealed[x - 1][y] = board[x - 1][y];
			}
		}

		// check top-left
		if (x > 0 && y > 0) {
			if (board[x - 1][y - 1] == 0 && revealed[x - 1][y - 1] != 0) {
				revealBlank(x - 1, y - 1);
			} else if (board[x - 1][y - 1] > 0 && revealed[x - 1][y - 1] == -1) {
				revealed[x - 1][y - 1] = board[x - 1][y - 1];
			}
		}

	}

	/**
	 * Determines if all the mines surrounding a tile have been flagged.
	 * 
	 * @param x		The x position of the tile.
	 * @param y		The y position of the tile.
	 * @return		True if all surrounding mines have been flagged, false
	 * 				otherwise.
	 */
	public boolean allBombsFound(int x, int y) {

		// check top
		if (y > 0) {
			if (board[x][y - 1] == MINETOKEN && revealed[x][y - 1] != FLAGTOKEN) {
				return false;
			}
		}

		// check top-right
		if (y > 0 && x < width - 1) {
			if (board[x + 1][y - 1] == MINETOKEN && revealed[x + 1][y - 1] != FLAGTOKEN) {
				return false;
			}
		}

		// check right
		if (x < width - 1) {
			if (board[x + 1][y] == MINETOKEN && revealed[x + 1][y] != FLAGTOKEN) {
				return false;
			}
		}

		// check bottom-right
		if (x < width - 1 && y < height - 1) {
			if (board[x + 1][y + 1] == MINETOKEN && revealed[x + 1][y + 1] != FLAGTOKEN) {
				return false;
			}
		}

		// check bottom
		if (y < height - 1) {
			if (board[x][y + 1] == MINETOKEN && revealed[x][y + 1] != FLAGTOKEN) {
				return false;
			}
		}

		// check bottom-left
		if (x > 0 && y < height - 1) {
			if (board[x - 1][y + 1] == MINETOKEN && revealed[x - 1][y + 1] != FLAGTOKEN) {
				return false;
			}
		}

		// check left
		if (x > 0) {
			if (board[x - 1][y] == MINETOKEN && revealed[x - 1][y] != FLAGTOKEN) {
				return false;
			}
		}

		// check top-left
		if (x > 0 && y > 0) {
			if (board[x - 1][y - 1] == MINETOKEN && revealed[x - 1][y - 1] != FLAGTOKEN) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Reveals all blank tiles surrounding a tile. 
	 * 
	 * @param x		The x position of the tile.
	 * @param y		The y position of the tile.
	 */
	public void revealSurrounding(int x, int y) {
		
		// check top
		if (y > 0) {
			if (revealed[x][y - 1] == -1)
				reveal(x, y - 1);
		}

		// check top-right
		if (y > 0 && x < width - 1) {
			if (revealed[x + 1][y - 1] == -1)
				reveal(x + 1, y - 1);
		}

		// check right
		if (x < width - 1) {
			if (revealed[x + 1][y] == -1)
				reveal(x + 1, y);
		}

		// check bottom-right
		if (x < width - 1 && y < height - 1) {
			if (revealed[x + 1][y + 1] == -1)
				reveal(x + 1, y + 1);
		}

		// check bottom
		if (y < height - 1) {
			if (revealed[x][y + 1] == -1)
				reveal(x, y + 1);
		}

		// check bottom-left
		if (x > 0 && y < height - 1) {
			if (revealed[x - 1][y + 1] == -1)
				reveal(x - 1, y + 1);
		}

		// check left
		if (x > 0) {
			if (revealed[x - 1][y] == -1)
				reveal(x - 1, y);
		}

		// check top-left
		if (x > 0 && y > 0) {
			if (revealed[x - 1][y - 1] == -1)
				reveal(x - 1, y - 1);
		}
		repaint();
	}
	
	/**
	 * Determines if all mines in the game board have been flagged.
	 * 
	 * @return		True if all mines in the board have been flagged,
	 * 				false otherwise.
	 */
	public boolean allMinesFlagged() {
		
		for (int col = 0; col < width; col++) {
			for (int row = 0; row < height; row++) {
				if(board[col][row] == MINETOKEN && revealed[col][row] != FLAGTOKEN) return false;
			}
		}
		return true;
	}
	
	/**
	 * Determines if all tiles in the game board have been revealed.
	 * 
	 * @return		True if all tiles in the board have been revealed,
	 * 				false otherwise.
	 */
	public boolean allTilesRevealed() {
		
		for (int col = 0; col < width; col++) {
			for (int row = 0; row < height; row++) {
				if(board[col][row] != MINETOKEN && revealed[col][row] != board[col][row]) return false;
			}
		}
		return true;
	}
	
	/**
	 * Sets all mines to be flagged for when a player has revealed
	 * all non-mine tiles but has not flagged all mines.
	 */
	public void flagAllMines() {
		for (int col = 0; col < width; col++) {
			for (int row = 0; row < height; row++) {
				if(board[col][row] == MINETOKEN) {
					revealed[col][row] = FLAGTOKEN;
				}
			}
		}
		flagsRemaining = 0;
	}
	
	/**
	 * Sets all tiles in the game board to be revealed for when a player
	 * has flagged all mines but has not reveal all non-mine tiles.
	 */
	public void revealAllTiles() {
		for (int col = 0; col < width; col++) {
			for (int row = 0; row < height; row++) {
				if(board[col][row] != MINETOKEN) {
					revealed[col][row] = board[col][row];
				}
			}
		}
	}

	/**
	 * Prints the board in the console.
	 */
	public void displayBoardInConsole() {

		for (int col = 0; col < width; col++) {
			for (int row = 0; row < height; row++) {
				System.out.print(board[col][row]);
			}
			System.out.println();
		}

	}

	/**
	 * Reveals all mines that have not been flagged.
	 * Used for when a player clicks on a mine and loses.
	 */
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

	/**
	 * Changes all flags that are not mine tiles to be crossed out mines.
	 */
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
			revealed[x][y] = CLICKEDMINE;
		} else if (revealed[x][y] == FLAGTOKEN) {
			return;
		} else if (revealed[x][y] == -1) {
			if (board[x][y] == 0) {
				revealBlank(x, y);
			} else {
				revealed[x][y] = board[x][y];
			}
			if(allTilesRevealed()) {
				showWin();
			}
		}
		repaint();
	}

	/**
	 * Changes a blank tile to be a flagged tile.
	 * 
	 * @param x		The x position of the tile.
	 * @param y		The y position of the tile.
	 */
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

	/**
	 * Reveals all surrounding tiles that have not be revealed
	 * only if all bombs surrounding the tile have been flagged.
	 * 
	 * @param x		The x position of the tile.
	 * @param y		The y position of the tile.
	 */
	public void complete(int x, int y) {
		if (revealed[x][y] > 0 && allBombsFound(x, y)) {
			revealSurrounding(x, y);
			repaint();
		} else {
			if(countNearbyFlags(x, y) >= countNearbyMines(x, y) && !allBombsFound(x, y)) {
				gameOver();
			}
		}
	}

	/**
	 * Shows the game over screen by revealing all unflagged mines,
	 * revealing all incorrectly flagged tiles and changing the 
	 * smiley button to a cross-eyed smiley button.
	 */
	public void gameOver() {
		gameOver = true;
		showMines();
		showWrongFlags();
		smileyIcon = Images.smileyDead;
		if(timerStarted) {
			timer.cancel();
			timer.purge();
			timerStarted = false;
		}
		repaint();
	}
	
	/**
	 * Shows the winning screen by changing the smiley button to
	 * a smiley with glasses and stopping the timer.
	 */
	public void showWin() {
		gameOver = true;
		flagAllMines();
		smileyIcon = Images.smileyWin;
		if(timerStarted) {
			timer.cancel();
			timer.purge();
			timerStarted = false;
		}
		repaint();
		System.out.println("You won!");
	}

	/*
	 * EVERYTHING BELOW HERE IS FOR DRAWING
	 */

	public void paintComponent(Graphics g) {

		g.setFont(new Font("Courier", Font.BOLD, 20));

		clear(g);
		paintTop(g);
		paintBoard(g);
		drawBorders(g);
	}

	/**
	 * Fills the screen with a grey rectangle.
	 * 
	 * @param g
	 */
	public void clear(Graphics g) {
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, width * tileSize + (borderSize * 2), height * tileSize + (topHeight + borderSize));
	}

	public void drawBorders(Graphics g) {

		drawTopBorder(g);
		drawMainBorder(g);
		drawApplicationBorder(g);
	}

	public void drawTopBorder(Graphics g) {
		g.setColor(Color.GRAY);
		g.fillRect(borderSize - padding, borderSize, width * tileSize + padding, padding);
		g.fillRect(borderSize - padding, borderSize, padding, topHeight - (borderSize * 2));
		g.setColor(Color.WHITE);
		g.fillRect(borderSize, borderSize + (topHeight - (borderSize * 2) - padding), width * tileSize + padding, padding);
		g.fillRect(borderSize + (width * tileSize), borderSize, padding, topHeight - (borderSize * 2));
	}

	public void drawMainBorder(Graphics g) {
		g.setColor(Color.GRAY);
		g.fillRect(borderSize - padding, topHeight - padding, width * tileSize + padding, padding);
		g.fillRect(borderSize - padding, topHeight - padding, padding, height * tileSize + (padding * 2));
		g.setColor(Color.WHITE);
		g.fillRect(borderSize, topHeight + (tileSize * height), width * tileSize + padding, padding);
		g.fillRect(borderSize + (width * tileSize), topHeight - padding, padding, height * tileSize + padding);
	}

	public void drawApplicationBorder(Graphics g) {
		g.setColor(Color.GRAY);
		g.fillRect(0 + padding, this.getHeight() - padding, this.getWidth(), padding);
		g.fillRect(this.getWidth() - padding, 0, padding, this.getHeight());
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, this.getWidth(), padding);
		g.fillRect(0, 0, padding, this.getHeight());
	}

	public void paintTop(Graphics g) {
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(borderSize, borderSize, width * tileSize, topHeight - (borderSize * 2));

		g.setFont(new Font("Arial", Font.BOLD, 21 * scale));
		drawFlagsBox(g);
		drawSmiley(g);
		drawTimerBox(g);
	}

	public void drawFlagsBox(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(borderSize + (borderSize / 2) - padding, borderSize + (borderSize / 2), 40 * scale, topHeight - (borderSize * 2) - (borderSize / 2) - padding * 3);
		g.setColor(Color.RED);
		g.drawString(df.format(flagsRemaining), borderSize + (borderSize / 2), borderSize + (borderSize / 2) + (topHeight - (borderSize * 2) - (borderSize / 2) - padding * 3) - padding);
	}
	
	public void drawSmiley(Graphics g) {
		
		g.drawImage(smileyIcon, smileyTopLeftX, smileyTopLeftY, smileySize, smileySize, null);
	}

	public void drawTimerBox(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(this.getWidth() - borderSize - padding * 2, borderSize + (borderSize / 2), -boxWidth, topHeight - (borderSize * 2) - (borderSize / 2) - padding * 3);
		g.setColor(Color.RED);
		g.drawString(df.format(secondsPassed), this.getWidth() - borderSize - padding * 2 - ((boxWidth - (padding))), borderSize + (borderSize / 2) + (topHeight - (borderSize * 2) - (borderSize / 2) - padding * 3) - padding);
	
	}

	public void drawResetButton(Graphics g) {
		g.setColor(Color.BLACK);
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
					g.drawImage(Images.mine, col * tileSize + borderSize, row * tileSize + topHeight, tileSize, tileSize, null);
					break;
				case 1:
					g.drawImage(Images.one, col * tileSize + borderSize, row * tileSize + topHeight, tileSize, tileSize, null);
					break;
				case 2:
					g.drawImage(Images.two, col * tileSize + borderSize, row * tileSize + topHeight, tileSize, tileSize, null);
					break;
				case 3:
					g.drawImage(Images.three, col * tileSize + borderSize, row * tileSize + topHeight, tileSize, tileSize, null);
					break;
				case 4:
					g.drawImage(Images.four, col * tileSize + borderSize, row * tileSize + topHeight, tileSize, tileSize, null);
					break;
				case 5:
					g.drawImage(Images.five, col * tileSize + borderSize, row * tileSize + topHeight, tileSize, tileSize, null);
					break;
				case 6:
					g.drawImage(Images.six, col * tileSize + borderSize, row * tileSize + topHeight, tileSize, tileSize, null);
					break;
				case 7:
					g.drawImage(Images.seven, col * tileSize + borderSize, row * tileSize + topHeight, tileSize, tileSize, null);
					break;
				case 8:
					g.drawImage(Images.eight, col * tileSize + borderSize, row * tileSize + topHeight, tileSize, tileSize, null);
					break;
				case 0:
					g.drawImage(Images.pressed, col * tileSize + borderSize, row * tileSize + topHeight, tileSize, tileSize, null);
					break;
				case -1:
					g.drawImage(Images.unpressed, col * tileSize + borderSize, row * tileSize + topHeight, tileSize, tileSize, null);
					break;
				case FLAGTOKEN:
					g.drawImage(Images.flag, col * tileSize + borderSize, row * tileSize + topHeight, tileSize, tileSize, null);
					break;
				case CLICKEDMINE:
					g.drawImage(Images.clickedMine, col * tileSize + borderSize, row * tileSize + topHeight, tileSize, tileSize, null);
					break;
				case CROSSTOKEN:
					g.drawImage(Images.cross, col * tileSize + borderSize, row * tileSize + topHeight, tileSize, tileSize, null);
					break;
				default:
					// Nothing.
				}
			}
		}
	}

	/**
	 * Determines if the position of a click is within the bounds of the window.
	 * 
	 * @param x   	The x position of the click.
	 * 
	 * @param y 	The y position of the click.
	 * 
	 * @return 		If the coords of the click are within the window, return true.
	 *         		Otherwise return false.
	 */

	public boolean inWindow(int x, int y) {
		return ((x >= 0 && x < width) && (y >= 0 && y < height)) ? true : false;
	}
	
	
	/**
	 * Determines if the (x, y) coordinates are within the bounds of the smiley
	 * button.
	 * 
	 * @param x		The x position of the mouse pointer.
	 * @param y		The y position of the mouse pointer.
	 * @return		True if the x and y positions are within the bounds of the
	 * 				smiley button, false otherwise.
	 */
	public boolean onSmiley(int x, int y) {
		return (x > smileyTopLeftX && x < smileyTopLeftX + smileySize && y > smileyTopLeftY && y < smileyTopLeftY + smileySize);
	}

	public void addListeners() {

		this.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {

			}

			@Override
			public void mousePressed(MouseEvent e) {
				
				// If mouse is pressed on the smiley button change the icon
				if(onSmiley(e.getX(), e.getY())) {
					smileyIcon = Images.smileyPressed;
					repaint();
				}
				
				if (!gameOver) {					
					
					if (SwingUtilities.isLeftMouseButton(e)) {
						leftMousePressed = true;
						if(!onSmiley(e.getX(), e.getY())) {
							smileyIcon = Images.smileyClicked;		// If tile is clicked change the smiley button to the clicked icon
							repaint();
						}
					} else if (SwingUtilities.isMiddleMouseButton(e)) {
						middleMousePressed = true;
						smileyIcon = Images.smileyClicked;			// If tile is clicked change the smiley button to the clicked icon
						repaint();
					} else if (SwingUtilities.isRightMouseButton(e)) {
						rightMousePressed = true;
					}
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {		
				
				// If the mouse is released on the smiley button, reset the game.
				if(onSmiley(e.getX(), e.getY())) {
					reset();
				} else if (!gameOver){
					smileyIcon = Images.smileyUnpressed;	// Change the smiley icon to the normal smiley icon
				}
				
				if (!gameOver && (e.getY() > 49 * scale) && e.getX() > borderSize) {
					int x = (e.getX() - borderSize) / tileSize;
					int y = (e.getY() - topHeight) / tileSize;
					
					// Starts the timer if it is not already running.
					if(!timerStarted) {
						task = new TimerTask() {
							@Override
							public void run() {
								secondsPassed++;
								repaint();
							}
							
						};
						timer = new Timer();
						timer.scheduleAtFixedRate(task, 1000, 1000);
						timerStarted = true;
					}
					
					if (SwingUtilities.isLeftMouseButton(e)) {
						leftMousePressed = false;
						if (inWindow(x, y))
							reveal(x, y);
					} else if (SwingUtilities.isMiddleMouseButton(e)) {
						middleMousePressed = false;
						complete(x, y);
					} else if (SwingUtilities.isRightMouseButton(e)) {
						rightMousePressed = false;
						if (inWindow(x, y))
							flag(x, y);
						if(allMinesFlagged()) {
							revealAllTiles();
							showWin();
						}
					}
				}
				repaint();
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
