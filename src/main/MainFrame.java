package main;

import javax.swing.JFrame;
import javax.swing.JPanel;

import gameOfLife.LifeGame;

public class MainFrame extends JFrame {
	private static final long serialVersionUID = 1324246334740150831L;
	private LifeGame game;
	private JPanel gamePanel;

	public MainFrame(JPanel gp) {
		super("Conway's Game of Life");
		
		gamePanel = gp;
		add(gamePanel);
		pack();
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void addGame(LifeGame game) {
		this.game = game;
		game.setGUIUpdate(this::onFrameUpdate);
	}
	
	public void onFrameUpdate() {
		if(game == null) return;
		
		gamePanel.putClientProperty("cells", game.getCells());
		gamePanel.repaint();
	}
}
