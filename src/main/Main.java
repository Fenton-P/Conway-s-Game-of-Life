package main;

import gameOfLife.LifeGame;
import shadedRenderer.BeachRenderer;

public class Main {

	public static void main(String[] args) {
		BeachRenderer panel = new BeachRenderer();
		
		MainFrame mainFrame = new MainFrame(panel);
		
		LifeGame game = new LifeGame();
		mainFrame.addGame(game);
		
		panel.add(game);
		panel.start();
	}

}
