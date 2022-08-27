public class MazeGenerator {

    /**
     * * maze generation
     * * making the array filling process more.... editable.
     * @param args
     */
    public static void main(String [] args) {
        int size = 1;
        int numMazes = 1;
        int xDimension = 5 * size;
        int yDimension = 5 * size;
        int zDimension = 3;
        MazeArray[] mazes= new MazeArray [numMazes];

        for (int i = 0; i < numMazes; i++) {
            mazes[i] = new MazeArray(xDimension, yDimension, zDimension);


            mazes[i].fillArray();
//        maze.fillArrayBaseball();


            mazes[i].connectArray();
            mazes[i].catalogueCellsAndWalls();
            mazes[i].resetMazeConnections();


            mazes[i].generateMazeDepthFirst();
//        maze.generateMazeKruskalAlgorithm();
//        maze.generateMazeKruskalAlgorithm2();
//            mazes[i].generateMazePrimsAlgorithm();


            System.out.print(mazes[i]);
        }
//        System.out.println(maze.allEdges.toString());


    }

}
