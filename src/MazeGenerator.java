public class MazeGenerator {

    /**
     * * maze generation
     * * making the array filling process more.... editable.
     * @param args
     */
    public static void main(String [] args) {
        int size = 4;
        int xDimension = 3 * size;
        int yDimension = 2 * size;
        int zDimension = 2;
        MazeArray maze = new MazeArray(xDimension, yDimension, zDimension);


        maze.fillArray();
//        maze.fillArrayBaseball();
        maze.connectArray();
        maze.catalogueCellsAndWalls();
        maze.resetMazeConnections();
        maze.generateMazeDepthFirst();
//        maze.generateMazeKruskalAlgorithm2();



        System.out.print(maze);
        System.out.println(maze.allEdges.toString());


    }

}
