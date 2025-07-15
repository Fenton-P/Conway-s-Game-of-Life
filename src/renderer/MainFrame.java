package renderer;

import javax.swing.JFrame;

import gameOfLife.LifeGame;

public class MainFrame extends JFrame {
	private static final long serialVersionUID = 1324246334740150831L;
	private LifeGame game;
	private GamePanel gamePanel;

	public MainFrame() {
		super("Conway's Game of Life");
		
		gamePanel = new GamePanel();
		add(gamePanel);
		pack();
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
	}
	
	public void addGame(LifeGame game) {
		this.game = game;
		game.setGUIUpdate(this::onFrameUpdate);
	}
	
	public void onFrameUpdate() {
		if(game == null) return;
		
		gamePanel.repaintCells(game.getCells());
	}
}
