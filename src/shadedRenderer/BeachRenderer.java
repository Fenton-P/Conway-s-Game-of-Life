package shadedRenderer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.awt.image.WritableRaster;

public class BeachRenderer extends ShadedPanel {
	private static final long serialVersionUID = 723473713174343793L;
	
	private int waterCutOff = 80;
	private int sandCutOff = 130;
	private int dirtCutOff = 250;
	private int stoneCutOff = 255;
	private int waterColor = 4095990;
	private int sandColor = 16768601;
	private int dirtColor = 8247896;
	private int stoneColor = 13553358;
	private int smoothness = 5;
	
	public BeachRenderer() {
		super();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D paint = (Graphics2D) g;
		paint.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		paint.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

		paint.setColor(Color.black);
		paint.fillRect(0,  0,  800,  450);
		
//		ArrayList<Cell> cells = new ArrayList<>();
//		
//		Object cellProperty = getClientProperty("cells");
//		if(cellProperty == null || !(cellProperty instanceof ArrayList<?>)) return;
//		for(Object o : (ArrayList<?>) cellProperty) if(o instanceof Cell) cells.add((Cell) o);
		
		//BufferedImage frame = getFrame(cells);
		if(displayImg == null) return;
		BufferedImage finalImage = applyGaussianBlur(displayImg);
		
		for(int i = 0;i<smoothness;i++) {
			finalImage = applyGaussianBlur(finalImage);
		}
		paint.drawImage(getAlteredImage(finalImage), 0, 0, null);
	}
	
	private BufferedImage getAlteredImage(BufferedImage displayImg) {
		BufferedImage i = new BufferedImage(800, 450, BufferedImage.TYPE_INT_RGB);
		
		WritableRaster r = displayImg.getRaster();
		for(int x = 0; x < 800; x++) {
			for(int y = 0; y < 450; y++) {
				int c = r.getSample(x, y, 0);
				
				int nc = 0;
				
				if(c <= waterCutOff) nc = waterColor;
				else if(c <= sandCutOff) nc = sandColor;
				else if(c <= dirtCutOff) nc = dirtColor;
				else if(c <= stoneCutOff) nc = stoneColor;
				
				i.setRGB(x, y, nc);
			}
		}
		
		return i;
	}
	
	public BufferedImage applyGaussianBlur(BufferedImage src) {
	    float[] matrix = {
	        1f/16f, 2f/16f, 1f/16f,
	        2f/16f, 4f/16f, 2f/16f,
	        1f/16f, 2f/16f, 1f/16f
	    };
	    Kernel kernel = new Kernel(3, 3, matrix);
	    ConvolveOp op = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
	    return op.filter(src, null);
	}
}
