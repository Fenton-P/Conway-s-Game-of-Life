package main;

import gameOfLife.LifeGame;
import shadedRenderer.ShadedPanel;

public class Main {

	public static void main(String[] args) {
		ShadedPanel panel = new ShadedPanel();
		
		MainFrame mainFrame = new MainFrame(panel);
		
		LifeGame game = new LifeGame();
		mainFrame.addGame(game);
		
		panel.add(game);
		panel.start();
	}

}
