import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class Tilemap {
	private Tile [][] tileArray;
	private int width, height;
	private int spaceMin;
	private int numSeeds;
	private ArrayList<Tile> seeds;
	private ArrayList<Tile> traversables;
	private Random random = new Random();
	




	public Tilemap(int wid, int hgt, int minSpace) {
		width = wid;
		height = hgt;
		tileArray = new Tile [width][height];
		spaceMin = minSpace;
		seeds = new ArrayList<Tile>();
		traversables = new ArrayList<Tile>();

		numSeeds = 0;
		for (int y = 0; y < height; y++) {			// set each tile to be nontraversable.
			for (int x = 0; x < width; x++) {
				tileArray[x][y] = new Tile(x, y, false, false);
			}
		}

	}

	private void binarySplitSeed(int xLowBound, int xUppBound, int yLowBound, int yUppBound, boolean verticalSplit) {
		// if at or below spaceMin, add the center Tile to the ArrayList of seeds.
		int xWidth = xUppBound - xLowBound;
		int yWidth = yUppBound - yLowBound;
		
		if (yWidth <= spaceMin || xWidth <= spaceMin  ) {  // if either the vertical or horizontal size is too small.
			seeds.add(0, tileArray[xLowBound + random.nextInt(xWidth / 2)][yLowBound + random.nextInt(yWidth / 2)]);
			seeds.get(0).setIsTraversable(true);
			numSeeds++;
		}
		else {		// otherwise, split the space again
			
			int ySplit = yLowBound + random.nextInt(yWidth/2) + (yWidth /4);
			int xSplit = xLowBound + random.nextInt(xWidth/2) + (xWidth /4);
			
			if (verticalSplit) {
				binarySplitSeed(xLowBound, xSplit, yLowBound, yUppBound, !verticalSplit);
				binarySplitSeed(xSplit, xUppBound, yLowBound, yUppBound, !verticalSplit);

			}
			else {
				binarySplitSeed(xLowBound, xUppBound, yLowBound, ySplit, !verticalSplit);
				binarySplitSeed(xLowBound, xUppBound, ySplit, yUppBound, !verticalSplit);
			}

		}
	}
	
	public void clearTiles() {
		for (int y = 0; y < height; y++) {			// set each tile to be nontraversable.
			for (int x = 0; x < width; x++) {
				tileArray[x][y] = new Tile(x, y, false, false);
			}
		}
	}
	public void binarySplitSeed(boolean verticalSplit) {
		binarySplitSeed(2, width, 0, height, verticalSplit);
	}
	
	public int getSpaceMin() {
		return spaceMin;
	}
	
	public ArrayList<Tile> getSeeds() {
		return seeds;
	}
	public void poisonedOrc(int xPos, int yPos, int numSpaces) {
		int nextMove;
		while (numSpaces > 0) {
			nextMove = random.nextInt(4);

			if (yPos <= 1){
				yPos++;
			}
			else if (yPos >= (height - 1)){
				yPos--;
			}
			
			if (xPos <= 1){
				xPos++;
			}
			else if (xPos >= (width - 1)){
				xPos--;
			}

			
			if (nextMove == 0) {
				xPos--;
			}
			else if (xPos == 0 || nextMove == 1) {
				xPos++;
			}
			else if (nextMove == 2) {
				yPos--;
			}
			else {
				yPos++;
			}
			
			if (!(tileArray[xPos][yPos].getTraversable())) {
				tileArray[xPos][yPos].setIsTraversable(true);
				numSpaces--;
			}
		}
		
	}
	

	// The idea for this is that, over time, repeated steps will lean towards that direction
	public void travel(Tile origin, Tile dest) {
		int xPos;
		int yPos;
		
		double shortestDist;
		double nextDist;
		Tile closestTile;
		
		while (origin != dest) {
			xPos = origin.getX();
			yPos = origin.getY();
			shortestDist = dest.euclideanDist(tileArray[xPos][yPos--]); // north tile
			closestTile = tileArray[xPos][yPos--];
			
			nextDist = (dest.euclideanDist(tileArray[xPos][yPos++])); // south tile
			if (nextDist < shortestDist) {
				shortestDist = nextDist;
				closestTile = tileArray[xPos][yPos++];
			}
			nextDist = dest.euclideanDist(tileArray[xPos++][yPos]);  // east tile
			if (nextDist < shortestDist) {
				shortestDist = nextDist;
				closestTile = tileArray[xPos++][yPos];
			}
			nextDist = dest.euclideanDist(tileArray[xPos--][yPos]);  // west tile
			if (nextDist < shortestDist) {
				shortestDist = nextDist;
				closestTile = tileArray[xPos--][yPos];

			}
			origin.setIsTraversable(true);
			origin = closestTile;
			
		}
		
		
	}
	
	public void pathWalk() {
		// randomize the order of the seeds:
		ArrayList<Tile> randomOrder = new ArrayList<Tile>();
		int numSeeds = seeds.size();
		int idx;
		while (numSeeds > 0) {
			idx = random.nextInt(numSeeds);
			randomOrder.add(0, seeds.get(idx));
			numSeeds--;
		}
		
		// link the seeds in their random order
		Iterator<Tile> itr = randomOrder.iterator();
		Tile first = itr.next();
		Tile curr = itr.next();
//		int dist;
		while (itr.hasNext()) {
//			dist = (int) first.euclideanDist(curr);
			travel(first, curr);
			
			first = curr;
			curr = itr.next();
		}
	}
	
	
//	public void delaunayTriangulation() {
//		clearTiles();
//		
//		Iterator<Tile> = seeds.ite
//		
//	}
	
	
	public String toString() {
		String map = "";
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				map += tileArray[x][y].toString();
			}
			map += "\n";
		}
		return map;
	}
	
	

}
