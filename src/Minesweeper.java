import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

public class Minesweeper {

	private final String TITLE = "Minesweeper";

	private Display display;

	private JFrame frame;
	private JMenuBar menuBar;
	private JMenu gameMenu;
	private JMenu displayMenu;

	private JMenuItem newGame, beginner, intermediate, expert, exit, zoomOne, zoomTwo, zoomThree, zoomFour;

	public Minesweeper() {

		frame = new JFrame(TITLE);
		display = new Display();
		createMenuBar();

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setJMenuBar(menuBar);
		frame.add(display);
		frame.setVisible(true);
		frame.pack();
		frame.setLocationRelativeTo(null);

		addListeners();
	}

	public void createMenuBar() {

		menuBar = new JMenuBar();

		// Game Menu
		gameMenu = new JMenu("Game");
		menuBar.add(gameMenu);
		newGame = new JMenuItem("New");
		gameMenu.add(newGame);

		gameMenu.addSeparator();

		beginner = new JMenuItem("Beginner");
		gameMenu.add(beginner);
		intermediate = new JMenuItem("Intermediate");
		gameMenu.add(intermediate);
		expert = new JMenuItem("Expert");
		gameMenu.add(expert);

		gameMenu.addSeparator();

		exit = new JMenuItem("Exit");
		gameMenu.add(exit);

		// Display Menu
		displayMenu = new JMenu("Scale");
		menuBar.add(displayMenu);
		zoomOne = new JMenuItem("100%");
		displayMenu.add(zoomOne);
		zoomTwo = new JMenuItem("200%");
		displayMenu.add(zoomTwo);
		zoomThree = new JMenuItem("300%");
		displayMenu.add(zoomThree);
		zoomFour = new JMenuItem("400%");
		displayMenu.add(zoomFour);

	}

	public void addListeners() {
		newGame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				display.reset();
			}
		});

		beginner.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (display.mode != Display.Mode.BEGINNER) {
					display.setMode(9, 9, 10);
					display.reinitScale();
					display.resetSize();
					frame.pack();
					display.mode = Display.Mode.BEGINNER;
				}
			}
		});

		intermediate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (display.mode != Display.Mode.INTERMEDIATE) {
					display.setMode(16, 16, 40);
					display.reinitScale();
					display.resetSize();
					frame.pack();
					display.mode = Display.Mode.INTERMEDIATE;
				}
			}
		});

		expert.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (display.mode != Display.Mode.EXPERT) {
					display.setMode(30, 16, 99);
					display.reinitScale();
					display.resetSize();
					frame.pack();
					display.mode = Display.Mode.EXPERT;
				}
			}
		});

		zoomOne.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (display.cScale != Display.Scale.ONE) {
					display.setScale(1);
					display.reinitScale();
					display.resetSize();
					frame.pack();
					display.cScale = Display.Scale.ONE;
				}
			}
		});

		zoomTwo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (display.cScale != Display.Scale.TWO) {
					display.setScale(2);
					display.reinitScale();
					display.resetSize();
					frame.pack();
					display.cScale = Display.Scale.TWO;
				}
			}
		});

		zoomThree.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (display.cScale != Display.Scale.THREE) {
					display.setScale(3);
					display.reinitScale();
					display.resetSize();
					frame.pack();
					display.cScale = Display.Scale.THREE;
				}
			}
		});

		zoomFour.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (display.cScale != Display.Scale.FOUR) {
					display.setScale(4);
					display.reinitScale();
					display.resetSize();
					frame.pack();
					display.cScale = Display.Scale.FOUR;
				}
			}
		});

		exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
	}

}
