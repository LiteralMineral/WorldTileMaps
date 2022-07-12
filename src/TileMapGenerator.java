import java.util.ArrayList;
import java.util.Iterator;

public class TileMapGenerator {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Tilemap map = new Tilemap(300, 400, 10);
		map.binarySplitSeed(false);
//		System.out.print(map + "\n\n");
		ArrayList<Tile> seeds = map.getSeeds();
		
		Iterator<Tile> itr = seeds.iterator();
		
		Tile first;
		Tile curr;
		int m = 0;
		while (itr.hasNext()) {
			curr = itr.next();
			map.drunkenOrc(curr.getX(), curr.getY(), 50);
			if (m % 10 == 0) {
//				System.out.println(map + "\n\n");
			}
			m++;
			
		}

//		map.pathWalk();
		
		System.out.println(map);
		
		
		


	}

}
