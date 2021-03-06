import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class Images {

	public static BufferedImage unpressed;
	public static BufferedImage pressed;
	public static BufferedImage mine;
	public static BufferedImage flag;
	public static BufferedImage cross;
	public static BufferedImage clickedMine;
	public static BufferedImage one;
	public static BufferedImage two;
	public static BufferedImage three;
	public static BufferedImage four;
	public static BufferedImage five;
	public static BufferedImage six;
	public static BufferedImage seven;
	public static BufferedImage eight;
	public static BufferedImage smileyUnpressed;
	public static BufferedImage smileyPressed;
	public static BufferedImage smileyClicked;
	public static BufferedImage smileyWin;
	public static BufferedImage smileyDead;	
	
	public Images() {
		
		loadImages();
		
	}
	
	public void loadImages() {
		
		try {
			
			// Reading images from file directory
			/*
			unpressed = ImageIO.read(new File("res/blank_128.png"));
			pressed = ImageIO.read(new File("res/pressed_128.png"));
			mine = ImageIO.read(new File("res/mine_128.png"));
			flag = ImageIO.read(new File("res/flag_128.png"));
			cross = ImageIO.read(new File("res/cross_32.png"));
			clickedMine = ImageIO.read(new File("res/clicked_mine_32.png"));
			one = ImageIO.read(new File("res/one_128.png"));
			two = ImageIO.read(new File("res/two_128.png"));
			three = ImageIO.read(new File("res/three_128.png"));
			four = ImageIO.read(new File("res/four_128.png"));
			five = ImageIO.read(new File("res/five_128.png"));
			six = ImageIO.read(new File("res/six_128.png"));
			seven = ImageIO.read(new File("res/seven_128.png"));
			eight = ImageIO.read(new File("res/eight_128.png"));
			smileyUnpressed = ImageIO.read(new File("res/smiley_52.png"));
			smileyPressed = ImageIO.read(new File("res/smiley_pressed_52.png"));
			smileyClicked = ImageIO.read(new File("res/smiley_click_52.png"));
			smileyWin = ImageIO.read(new File("res/smiley_win_52.png"));
			smileyDead = ImageIO.read(new File("res/smiley_dead_52.png"));
			*/
			
			// Reading images from .jar
			unpressed = ImageIO.read(getClass().getResourceAsStream("blank_128.png"));
			pressed = ImageIO.read(getClass().getResourceAsStream("pressed_128.png"));
			mine = ImageIO.read(getClass().getResourceAsStream("mine_128.png"));
			flag = ImageIO.read(getClass().getResourceAsStream("flag_128.png"));
			cross = ImageIO.read(getClass().getResourceAsStream("cross_32.png"));
			clickedMine = ImageIO.read(getClass().getResourceAsStream("clicked_mine_32.png"));
			one = ImageIO.read(getClass().getResourceAsStream("one_128.png"));
			two = ImageIO.read(getClass().getResourceAsStream("two_128.png"));
			three = ImageIO.read(getClass().getResourceAsStream("three_128.png"));
			four = ImageIO.read(getClass().getResourceAsStream("four_128.png"));
			five = ImageIO.read(getClass().getResourceAsStream("five_128.png"));
			six = ImageIO.read(getClass().getResourceAsStream("six_128.png"));
			seven = ImageIO.read(getClass().getResourceAsStream("seven_128.png"));
			eight = ImageIO.read(getClass().getResourceAsStream("eight_128.png"));
			smileyUnpressed = ImageIO.read(getClass().getResourceAsStream("smiley_52.png"));
			smileyPressed = ImageIO.read(getClass().getResourceAsStream("smiley_pressed_52.png"));
			smileyClicked = ImageIO.read(getClass().getResourceAsStream("smiley_click_52.png"));
			smileyWin = ImageIO.read(getClass().getResourceAsStream("smiley_win_52.png"));
			smileyDead = ImageIO.read(getClass().getResourceAsStream("smiley_dead_52.png"));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
