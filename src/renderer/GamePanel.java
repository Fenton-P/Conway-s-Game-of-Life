package renderer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.JPanel;

import gameOfLife.Cell;

public class GamePanel extends JPanel {
	private static final long serialVersionUID = -1118307677313558814L;
	
	private ArrayList<Cell> cells;
	private int cellSize = 10;
	
	public GamePanel() {
		setPreferredSize(new Dimension(800, 450));
		setBackground(Color.black);
		
		cells = new ArrayList<>();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D paint = (Graphics2D) g;
		paint.setColor(Color.black);
		paint.fillRect(0,  0,  800,  450);
		
		paint.setColor(Color.white);
		for(Cell cell : cells) {
			paint.fillRect(cell.x * cellSize, cell.y * cellSize, cellSize, cellSize);
		}
	}
	
	public void repaintCells(ArrayList<Cell> cells) {
		this.cells = cells;
		
		repaint();
	}
}
