package renderer;

import gameOfLife.LifeGame;

public class Main {

	public static void main(String[] args) {
		MainFrame mainFrame = new MainFrame();
		
		LifeGame game = new LifeGame();
		mainFrame.addGame(game);
		
		game.start();
	}

}
