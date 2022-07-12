import java.util.Random;

public class Tile {
	private int xLoc, yLoc;
	private int size;
	private boolean isTraverse;
	private boolean isDoor;
	private boolean isSeed;
	
	public Tile() {
		isTraverse = false;
		isDoor = false;
	}
	// public Tile(boolean trav, boolean dor)
	public Tile(int x, int y, boolean trav, boolean dor) {
		xLoc = x;
		yLoc = y;
		isTraverse = trav;
		isDoor = dor;
		size = 30;
	}
	
	public int getX() {
		return xLoc;
	}
	
	public int getY() {
		return yLoc;
	}
	public void setSize(int s) {
		size = s;
	}
	
	public void setSeed(boolean val) {
		isSeed = val;
	}
	
	public int getSize() {
		return size;
	}
	
	// sets the given tile to be traversable. 
	// it returns true if the new state is different from the old state
	public boolean setIsTraversable(boolean val) {
		boolean old = isTraverse;
		isTraverse = val;
		return (old != isTraverse); // alternatively, return an exclusive-or ??		
	}
	
	public boolean getTraversable() {
		return isTraverse;
	}
	
	public double euclideanDist(Tile t) {
		int xDist = this.xLoc - t.getX();
		int yDist = this.yLoc - t.getY();
		
		return (int) Math.sqrt(xDist * xDist + yDist * yDist);
		
		
	}
	
	// taxiDist
	public int taxiDist(Tile t) {
		return Math.abs(xLoc - t.getX()) + Math.abs(yLoc - t.getY());		
	}
	
	public boolean equals(Tile t) {
		return (this.xLoc == t.getX() && this.yLoc == t.getY());		
	}
	public String toString() {
		return (isTraverse ? (isDoor ? "D" : "  " ) : "XX");
	}
	
}
