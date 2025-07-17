package shadedRenderer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import gameOfLife.Cell;
import gameOfLife.LifeGame;

public class ShadedPanel extends JPanel {
	private static final long serialVersionUID = 762324677395901363L;
	
	protected LifeGame lifeGame;
	private int gridSize = 10;
	private int radius = 100;
	private int fps = 20;
	private int cutOff = 0;
	private double brightness = .01;
	private double adderMultiplier = 1.1;
	private int frameCutOff = 0;
	private double updateFrequency = 20;
	private int randomness = 35;
	protected Thread gameThread;
	private BufferedImage currImg;
	private BufferedImage nextImg;
	protected BufferedImage displayImg;
	private int currFrame = 0;
	private int frames = (int) (fps / updateFrequency);
	private Map<Integer, Double> changeMap;
	protected int iterations = 100;
	protected int initialIterations = iterations;
	
	public int adjust(int color) {
		double nc = color + (Math.random() * randomness - randomness / 2.0);
		if(nc > 255) nc = 255;
		if(nc < 0) nc = 0;
		return (int) nc;
	}
	
	public ShadedPanel() {
		setPreferredSize(new Dimension(800, 450));
		setBackground(Color.black);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D paint = (Graphics2D) g;
		paint.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		paint.setColor(Color.black);
		paint.fillRect(0,  0,  800,  450);
		
//		ArrayList<Cell> cells = new ArrayList<>();
//		
//		Object cellProperty = getClientProperty("cells");
//		if(cellProperty == null || !(cellProperty instanceof ArrayList<?>)) return;
//		for(Object o : (ArrayList<?>) cellProperty) if(o instanceof Cell) cells.add((Cell) o);
		
		//BufferedImage frame = getFrame(cells);
		if(displayImg == null) return;
		paint.drawImage(displayImg, 0, 0, null);
	}
	
	private BufferedImage getFrame(ArrayList<Cell> cells) {
		BufferedImage img = new BufferedImage(800, 450, BufferedImage.TYPE_BYTE_GRAY);
		WritableRaster raster = img.getRaster();
		
		for(Cell c : cells) {
			for(int x = c.x * gridSize - radius; x < c.x * gridSize + radius; x++) {
				for(int y = c.y * gridSize - radius; y < c.y * gridSize + radius; y++ ) {
					if(x >= 800 || x < 0 || y >= 450 || y < 0) continue;
					double d = Math.sqrt(Math.pow(x - c.x * gridSize, 2) + Math.pow(y - c.y * gridSize, 2));
					if(d > radius) continue;
					int color = (int) (brightness * 255 * (1 - d / radius));
					color += raster.getSample(x, y, 0);
					color = (int) (color * adderMultiplier);
					if(color > 255) color = 255;
					raster.setSample(x, y, 0, color);
				}
			}
		}
		
		for(int x = 0; x < 800; x++) {
			for(int y = 0; y < 450; y++) {
				int color = raster.getSample(x, y, 0);
				if(color < cutOff) raster.setSample(x, y, 0, 0);
				raster.setSample(x, y, 0, adjust(color));
			}
		}
		
		return img;
	}
	
	public void add(LifeGame game) {
		lifeGame = game;
	}
	
	public void start() {
		gameThread = new Thread(this::looper);
		gameThread.start();
	}
	
	private void update() {
		if(currFrame % frames == 0) {
			currFrame = 0;
			currImg = nextImg == null ? getFrame(lifeGame.getCells()) : nextImg;
			lifeGame.update();
			nextImg = getFrame(lifeGame.getCells());
			displayImg = copyImage(currImg);
			currFrame++;
			setChangeMap();
			SwingUtilities.invokeLater(this::repaint);
			if(--iterations == 0) attemptGameEnd();
			return;
		}
		
		WritableRaster r = displayImg.getRaster();
		for(Map.Entry<Integer, Double> m : changeMap.entrySet()) {
			int k = m.getKey();
			int y = k / 800;
			int x = k % 800;
			
			int currVal = (int) (m.getValue() * currFrame + currImg.getRaster().getSample(x, y, 0));
			if(currVal < frameCutOff) continue;
			r.setSample(x, y, 0, currVal);
		}
		
		currFrame++;
		SwingUtilities.invokeLater(this::repaint);
	}
	
	protected void attemptGameEnd() {
		gameThread = null;
	}
	
	private BufferedImage copyImage(BufferedImage source) {
	    BufferedImage copy = new BufferedImage(
	        source.getWidth(),
	        source.getHeight(),
	        source.getType()
	    );
	    Graphics2D g2d = copy.createGraphics();
	    g2d.drawImage(source, 0, 0, null);
	    g2d.dispose();
	    return copy;
	}
	
	private void setChangeMap() {
		changeMap = new HashMap<>();
		for(int x = 0; x < 800; x++) {
			for(int y = 0; y < 450; y++) {
				int c1 = currImg.getRaster().getSample(x, y, 0);
				int c2 = nextImg.getRaster().getSample(x, y, 0);
				if(c1 == c2) continue;
				
				changeMap.put(y * 800 + x, (c2 - c1) / (double) frames);
			}
		}
	}
	
	private void looper() {
		while(gameThread != null) {
			update();
			
			try {
				//Thread.sleep(1000 / fps);
				Thread.sleep(0);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}