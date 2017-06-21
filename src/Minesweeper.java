import java.awt.Dimension;

import javax.swing.JFrame;


public class Minesweeper {

	private final String TITLE = "Minesweepr";
	
	private Display display;
	
	private JFrame frame;
	
	public Minesweeper() {
		
		frame = new JFrame(TITLE);
		display = new Display();
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.add(display);
		frame.setVisible(true);
		frame.pack();
		frame.setLocationRelativeTo(null);
	}
	
}
