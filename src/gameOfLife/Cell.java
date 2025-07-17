package gameOfLife;

public class Cell {
	public int x;
	public int y;
	
	public Cell(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public boolean equals(Object cell) {
		Cell compare = (Cell) cell;
		
		if(compare == null) return this == null;
		
		return compare.x == this.x && compare.y == this.y;
	}
}