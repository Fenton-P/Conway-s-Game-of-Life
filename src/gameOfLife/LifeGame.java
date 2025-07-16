package gameOfLife;

import java.util.ArrayList;

import javax.swing.SwingUtilities;

public class LifeGame {
	private ArrayList<Cell> cells;
	private Thread gameThread;
	private long DELAY = 100;
	private Runnable updateGUI;
	
	public LifeGame() {
		cells = new ArrayList<>();
		
		loadDefaultStartState();
	}
	
	public void loadDefaultStartState() {
		int startingCells = 50;
		int spreadX = 20;
		int offsetX = 30;
		int spreadY = 14;
		int offsetY = 12;
		
		if(startingCells > (spreadX - 1) * (spreadY - 1)) startingCells = (spreadX - 1) * (spreadY - 1);
		
		for(int i = 0; i < startingCells; i++) {
			int x = (int) (Math.random() * spreadX) + offsetX;
			int y = (int) (Math.random() * spreadY) + offsetY;
			
			if(cells.contains(new Cell(x, y))) startingCells++;
			else cells.add(new Cell(x, y));
		}
	}
	
	public void start() {
		gameThread = new Thread(this::looper);
		gameThread.start();
	}
	
	public void stop() {
		gameThread = null;
	}
	
	public void setGUIUpdate(Runnable updater) {
		updateGUI = updater;
	}
	
	public void update() {
		ArrayList<Cell> newCells = new ArrayList<>();
		
		for(Cell cell : cells) {
			newCells.addAll(getNeighbors(cell));
		}
		
		ArrayList<Cell> prevCells = cells;
		cells = new ArrayList<>();
		
		for(int i = 0; i < newCells.size(); i++) {
			Cell currCell = newCells.get(i);
			int count = 1;
			
			for(int j = i + 1; j < newCells.size(); j++) {
				if(newCells.get(j).equals(currCell)) {
					count++;
					newCells.remove(j);
					j--;
				}
			}
			
			if(count == 3 || count == 2 && prevCells.contains(currCell)) cells.add(currCell);
		}
		
		if(updateGUI == null) return;
		SwingUtilities.invokeLater(updateGUI);
	}
	
	public ArrayList<Cell> getCells() {
		return cells;
	}
	
	private void looper() {
		while(gameThread != null) {
			update();
			
			try {
				Thread.sleep(DELAY);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private ArrayList<Cell> getNeighbors(Cell cell) {
		ArrayList<Cell> cells = new ArrayList<>();
		
		cells.add(new Cell(cell.x + 1, cell.y));
		cells.add(new Cell(cell.x - 1, cell.y));
		cells.add(new Cell(cell.x, cell.y + 1));
		cells.add(new Cell(cell.x, cell.y - 1));
		cells.add(new Cell(cell.x + 1, cell.y + 1));
		cells.add(new Cell(cell.x - 1, cell.y + 1));
		cells.add(new Cell(cell.x - 1, cell.y - 1));
		cells.add(new Cell(cell.x + 1, cell.y - 1));
		
		return cells;
	}
}
