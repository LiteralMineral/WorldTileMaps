import java.util.*;

public class MazeArray {
    /**
     * This class uses a multidimensional array to construct, store, and display a graph of MazeCell instances.
     * It will hopefully eventually contain code for generating different types of mazes (i.e. ones with
     * rooms created before creating the maze portion, mazes where the content changes from layer to layer, etc.
     * It will also hopefully have ways for a user to customize the generation process (such as keeping the
     * various combinations of generation technique accessible and not hidden behind the constructor)
     *
     * The generation process will generally go as follows:
     *      * initialize the information needed to construct the array (length, height, depth, etc., and the array itself)
     *      * fill the array in some method (cube, sphere, cone, random, shoot-from-center technique, user equation)
     *              (The shoot-from-center technique has a proper name but I can't remember it. I'll find it again and put a link here)
     *      * traverse the array, connecting all existing MazeCells
     *      * choose starting and ending points, a center, and other relevant MazeCells.
     *      * generate rooms if desired by user
     *      * call upon a function that generates a maze from a graph. There might be a
     *
     *
     *
     *
     *
     *
     *
     */
    private static Random rand = new Random();
    private int numDimensions = 3; // This is intended to remind myself that I want to figure out a way to make an N-dimensional maze.
    private int xDimension, yDimension, zDimension;
    private int [] dimensions;
    private MazeCell2[][][] maze;
    private MazeCell2 start, end, center, current; // "center" may be randomized.... I am considering options.
//    private ArrayList<Shape> shapesInMaze; TODO: implement shape
    private Stack<MazeCell2> path = new Stack<MazeCell2>();
    public ArrayList<MazeCell2> allCells = new ArrayList<MazeCell2>();
    public ArrayList<Edge> allEdges = new ArrayList<Edge>();
    private Random random = new Random();

    public int extraDimensionalFactor; // TODO: make it so someone can enter in a value for this and it will affect the ratio of extradimensional passages

    public MazeCell2 getMazeCell(int ... coordinates) {
        return maze[coordinates[0]][coordinates[1]][coordinates[2]];
    }

    /**
     * MazeArray(...)
     * initialize the dimensions of the maze, and initialize the array that stores the MazeCells.
     * TODO: Possibly create an alternate constructor that uses the fillArray(String str)
     * @param x
     * @param y
     * @param z
     */
    public MazeArray(int x, int y, int z) {
        dimensions = new int[numDimensions];

        xDimension = x;
        yDimension = y;
        zDimension = z;

        dimensions[0] = xDimension;
        dimensions[1] = yDimension;
        dimensions[2] = zDimension;
        maze = new MazeCell2[xDimension][yDimension][zDimension];

    }

    /**
     * This function fills the array in a cube by traversing entire array. All possible positions will be filled.
     * NOTE: I actually think a better, more efficient way of filling the array that allows for more complicated
     * structures is to use an equation object and fill it from lower to upper bounds...
     *
     * This function should also place the starting node at 0,0,0 and the end at the last created
     */
    public void fillArray() {


        // c for column (the x value),
        // r for the row (y value),
        // l for the layer (z value)
        for (int l = 0; l < zDimension; l++) {
            for (int c = 0; c < xDimension; c++) {
                for (int r = 0; r < yDimension; r++) {
                    maze[c][r][l] = MazeCell2.MazeCell2RandomNumMaxVisits(1, numDimensions,c, l, r);
                    end = maze[c][r][l];
                }
            }
        }
        start = maze[0][0][0];
        current = start;



    }

    /**
     * TODO: create more modes for choosing a starting cell....
     * @param mode
     */
    public void selectStart(String mode) {
        int [] coords = new int[numDimensions];

        switch (mode.toLowerCase()) {
            case "randomfaces":
                int id = rand.nextInt(3);
                coords[id] = 0;
                id = (id + 1) % 3;
                coords[id] = rand.nextInt(dimensions[id]);
                id = (id + 1) % 3;
                coords[id] = rand.nextInt(dimensions[id]);
                break;
        }
        // add other cases....

        // it should fall through if the mode is "default"
    }

    public void selectEnd(String mode) {

    }


    public void resetMazeConnections(){
        for (int l = 0; l < zDimension ; l++) {
            for (int c = 0; c < xDimension; c++) {
                for (int r = 0; r < yDimension; r++) {
                    if (maze[c][r][l] != null) {
                        maze[c][r][l].refillUnvisited();
                    }
                }
            }
        }
    }

    /**
     * TODO: this function currently has an issue where a single-layered maze doesn't get connected properly, and the last layer doesn't get connected properly.
     */
    public void connectArray() {

        // connect the given cell to the cells in the positive x, y, and z directions.
        // Be sure to write MazeCell.makeAdjacent such that it can handle a null pointer,
        // otherwise include conditions for whether the pair of MazeCells will be made adjacent.
        // That last part is not relevant for the entire-array-gets-filled variety of fillArray(),
        // but it will become relevant when only part of the array is filled


        // traversing within the
        for (int l = 0; l < zDimension; l++) {
            for (int c = 0; c < xDimension; c++) {
                for (int r = 0; r < yDimension; r++) {

                    if (maze[c][r][l] != null) {
//                        int targetCoordinates[] = {((c < xDimension - 1) ? c + 1 : c - 1),((r < yDimension - 1) ? r + 1 : r - 1), ((l < zDimension - 1) ? l + 1 : l -1)};
                        if (c < xDimension - 1) {
                            maze[c][r][l].makeAdjacent(maze[c + 1][r][l], 0); // make adjacent with horizontal
                        }

                        if (r < yDimension - 1) {
                            maze[c][r][l].makeAdjacent(maze[c][r + 1][l], 1); // make adjacent with vertical
                        }
                        if (l < zDimension - 1){
                            maze[c][r][l].makeAdjacent(maze[c][r][l + 1], 2); // make adjacent with other layer
                        }
                    }
                }
            }
        }



    }

    public void catalogueCellsAndWalls() {
        for (int c = 0; c < xDimension; c++) {
            for (int r = 0; r < yDimension; r++) {
                for (int l = 0; l < zDimension; l++) {
                    if (maze[c][r][l] != null) {
                        addAdjacentEdgesToWalls(allEdges,maze[c][r][l]); // add the cell's walls (that actually connect with another cell)
                        allCells.add(maze[c][r][l]); // add the cell itself.
                    }
                }
            }
        }
    }


    /**
     * fills the array in the shape of a baseball field. (really it's a sphere, but.... I wanted
     * the actual sphere function to be named sensibly and this is just a placeholder to test the function-based filling)
     */
    public void fillArrayBaseball() {
        int radius = Math.min(Math.min(xDimension, yDimension), zDimension); // find the limiting radius of the baseball sweep.

        // c for column (the x value),
        // r for the row (y value),
        // l for the layer (z value)
        for (int l = 0; l < zDimension; l++) {
            for (int c = 0; c < xDimension; c++) {
                for (int r = 0; r < yDimension; r++) {
                    if (Math.sqrt(Math.pow(c, 2) + Math.pow(r, 2) + Math.pow(l, 2)) < radius) {
                        maze[c][r][l] = MazeCell2.MazeCell2RandomNumMaxVisits(1, 3, c, r, l);
                    }
                }
            }
        }
    }

    /**
     * function to seed array with cells.
     *
     * TODO: write code to fill array in different styles
     * TODO: make it so the fillarray function actually takes in an Equation object that generates the compared value.
     * @param mode
     */
    public void fillArray(String mode) {
        //

    }


//
//    private void fillArrayObject(Solid3d obj) {
//
//    }

    // function to seed array with cells in a cone shape
    private void fillArrayCone() {
        // fill array with a cone based on the size of the x-y plane
        // the general equation of a cone is x^2/a^2 + y^2/b^2  = z^2/c^2. I will use the dimensions for each axis as the a, b, and c values.


    }

    // function to seed array with cells in an ellipsoid shape
//    public void fillArrayEllipsoid(int [] magnitudes, int [] center, int radius) {
//        SphereEquation bottomHalf = new SphereEquation(magnitudes,center, radius);
//        SphereEquation upperHalf = new
//        int [] xValue = {0};
//        int [] xyValues = {0,0};
//        int [] xyzValues = {0,0,0};
//        for (int c = 0; c < xDimension; c++) { // iterate through the columns
//            xValue[0] = c;
//
//            sphere1.solveForNextTermsValue(xValue);
//        }
//
//        // fill array with an ellipsoid based on the size of the x-y plane
//    }


//    public void fillArrayTwoEquations(Equation equation1, Equation equation2) {
//
//        int yLowerBound = 0;
//        int yUpperBound = 0;
//
//        for (int c = 0; c < xDimension; c++) {
//
//            yLowerBound = Math.max(equation1.solveForNextTermsValue(c), 0);
//            yUpperBound = Math.min(equation2.solveForNextTermsValue(c), yDimension);
//
//            for (int r = yLowerBound; r < yUpperBound ; r++) {
//
//                int zLowerBound = Math.max(equation1.solveForNextTermsValue(c, r), 0);
//                int zUpperBound = Math.min(equation2.solveForNextTermsValue(c, r), zDimension);
//
//                for (int l = zLowerBound; l < zUpperBound; l++) {
//                    maze[c][r][l] = new MazeCell2(c, r, l);
//                }
//            }
//
//        }
//    }



    // function to seed array with cells in a random, traversable cluster
    private void fillArrayAntWalk() {
        // fill array with a random cluster of adjacent cells traversed by an ant
    }

    // function to seed array with cells in a random cluster. method to be determined.
    private void fillArrayRandomOther() { // not sure what method to put here yet, but...
        // fill array with a random cluster of adjacent cells. must be traversable.
    }

    // iterate through the shapes arraylist and fill the maze using them.
    private void fillArrayShapes(){


    }

    /**
     * function will take in a string and, using a library of helper functions,
     * will construct a function that will determine whether
     * TODO: This will be done faaaar in the future, but we'll have to either build the helper functions or find a suitable library (I vote for second option)
     * Potentially use one of the options explored in this forum post?: https://stackoverflow.com/questions/4681959/algebra-equation-parser-for-java
     * @param equation
     */


    /**
     * Somehow this will choose a random direction and then clear the closest cell that fits in that line.
     * TODO: research and write the function
     * determines a random degree of rotation in the xy plane, then a degree of rotation from that plane
     * to determine a vector. normalizes that vector, then follows its instructions to traverse the space until
     * it either reaches the edge or it encounters an unseeded location. It proceeds to make that location a cell.
     * This is a helper function for
     */

    private void destroyFromCenter() {
        // will have a size limit equal to the size of a sphere. Maybe. It needs some sort of limit for it to be used
    }


    /**
     * Functions for generating a maze structure!!
     * All of these functions should follow the depth-first search algorithm for generating a maze.
     * The different variations in maze generation should be created by different properties of a cell or path.
     *
     * https://en.wikipedia.org/wiki/Maze_generation_algorithm
     * The iterative algorithm (per Wikipedia):
     *
     * 1. Choose initial cell, mark as visited, push to stack
     * 2. While stack is not empty:
     *      1. pop cell from stack, make it current cell.
     *      2. If current cell has neighbours which have not been visited (as determined by MazeCell2.hasUnvisitedNeighbors())
     *          1. push current cell to the stack
     *          2. choose an *unvisited* neighbor (the precedence will have been predetermined by the cell's behavior)
     *          3. remove wall between current and next cell (make the path between them traversable)
     *          4. mark next cell as visited (add to the "visited" count), push it to stack.
     *
     *
     *
     */

    public void generateMazeDepthFirst() {
        MazeCell2 currentCell = null;
        MazeCell2 nextCell = null;
        // start is already chosen...
//        start = maze[xDimension/2][yDimension/2][zDimension/2];
        start = maze[0][0][0];
        path.clear();
        path.push(start); // choose initial cell
        start.visit(); // mark as visited
        while (!path.isEmpty()) {
//            System.out.println("path was not empty");
            currentCell = path.pop(); // pop cell from stack, make it the current cell. If a cell is in the stack, then we know it has been visited at least once.

            currentCell.visit();
            if (currentCell.getNumNeighboursWithOpenRoutes() > 0) { // if current cell has neighbours with unvisited neighbours

//                System.out.println("Current cell has neighbours with open routes");
                path.push(currentCell); // push current cell to stack

                Edge targetEdge = currentCell.removeNextPath(); // select the cell with the highest precedence, remove it from the eligible paths.
                while (!targetEdge.endPointCanBeVisited()) {
                    targetEdge = currentCell.removeNextPath(); // makes sure the next cell is actually able to be visited
                }
                targetEdge.makeRoute(); // set route to traversable to remove wall

                nextCell = targetEdge.getEndPoint(); // choose next cell
                nextCell.visit(); // mark next cell as visited

                path.push(nextCell); // push next cell to stack
            }
        }
    }





    /*
     * Prim's Algorithm Materials:
     */

    public void addAdjacentEdgesToWalls(ArrayList<Edge> wallList, MazeCell2 cell) { // ensures that the only values put in the list are not null
        Edge [][] walls = cell.getPaths();
        for (int i = 0; i < walls.length; i++) {
            if (walls[i][0] != null && !walls[i][0].isTraversable() && walls[i][0].hasEndPoint()) {
                wallList.add((walls[i][0]));
            }
            if (walls[i][1] != null && !walls[i][1].isTraversable() && walls[i][1].hasEndPoint()) {
                wallList.add((walls[i][1]));
            }
        }
    }

    /*
     * Used the wikipedia description to understand the algorithm and write the implementation
     * https://en.wikipedia.org/wiki/Maze_generation_algorithm
     *
     */
    public void generateMazePrimsAlgorithm() {
        ArrayList<Edge> wallList = new ArrayList<Edge>();
        MazeCell2 currentCell = maze[xDimension/2][yDimension/2][zDimension/2];
        currentCell.visit();
        addAdjacentEdgesToWalls(wallList, currentCell);

        while (!wallList.isEmpty()) {

            Edge randomWall = wallList.remove(random.nextInt(wallList.size()));
            if (!randomWall.getStartPoint().canBeVisited() ^ !randomWall.getEndPoint().canBeVisited()) { // should be the same as !A XOR !B. ^ is XOR.
                currentCell = (randomWall.getStartPoint().canBeVisited() ? randomWall.getEndPoint() : randomWall.getStartPoint()); //
                currentCell.visit(); // mark the cell as having been visited again.
                randomWall.makeRoute(); // TODO: later establish whether the route is traversable both ways.
                addAdjacentEdgesToWalls(wallList, currentCell);

                // the random wall has already been removed.
//                wallList.remove(randomWall);
            }


        }






    }

    /*
     *
     * Per the wikipedia entry on Maze Generation:
     *      "1. Create a list of all walls, and create a set for each cell, each containing just that one cell.
     *      2. For each wall, in some random order:
     *          1. If the cells divided by this wall belong to distinct sets:
     *              1. Remove the current wall *Sarah's note: (and its inverse)* (AND set the walls to be traversable!!)
     *              2. Join the sets of the formerly divided cells."
     * I will use an ArrayList of sets of cells
     *
     *
     * TODO: eventually you should consider getting rid of the directed path design and implementing that later in the generation process
     */
//    public void generateMazeKruskalAlgorithm() {
//        ArrayList<Edge> walls = new ArrayList<Edge>(allEdges); // create list of walls from the catalogue of all walls.
//
//        ArrayList<TreeSet<MazeCell2>> distinctSetsCells = new ArrayList<TreeSet<MazeCell2>>(); // create list of sets so you can search them.
//
//        for (int c = 0; c < xDimension; c++){
//            TreeSet<MazeCell2> newSet = new TreeSet<MazeCell2>();
//            newSet.add(maze[c])
//            distinctSetsCells.add(newSet);
//        }
//        TreeSet<MazeCell2> set1 = new TreeSet<MazeCell2>(allCells); // create set of cells from the catalogue of all cells
//
//
//
//    }

//    public boolean isDisjoint(Set<MazeCell2> set1, Set<MazeCell2> set2) {
//        // while set1 still has cells to process, return false if a cell in set1 is contained in set2. otherwise, return true.
//        Iterator<MazeCell2> itrOne= set1.iterator();
//        MazeCell2 currentCell
//        itrOne.has
//
//
//    }



    // optimize the kruskal algorithm perhaps, by creating a setID that can be used to mark the sets.... test for their disjointedness that way. maybe? not sure.

    /**
     * This currently does not work. I will have to make it work.
     */
    public void generateMazeKruskalAlgorithm2() {
        ArrayList<Edge> walls = new ArrayList<Edge>(allEdges); // create list of walls from the catalogue of all walls.
        Edge currentWall = null;
        System.out.println(this);
        while (!walls.isEmpty()) {
            currentWall = walls.get((int) Math.random() * walls.size()); // choose random wall
            if (currentWall.getEndPoint().getSetId() != currentWall.getStartPoint().getSetId()) { // if they belong to different sets
                // set wall and its inverse to be traversable
                currentWall.setTraversable(true);
                currentWall.getInverseEdge().setTraversable(true);
                // remove wall and its inverse from the list of walls
                walls.remove(currentWall);
                walls.remove(currentWall.getInverseEdge());
                // join the two cells' sets by propagating the new set id from one of the cells to the others cells.
                (currentWall.getEndPoint()).propogateNewSetId(currentWall.getStartPoint().getSetId());
            }
        }
    }


//    public boolean hasCycle() { // to test whether I'm successfully adding cycles when I want to add cycles
//        Stack<MazeCell2> path = new Stack<MazeCell2>(); // initialize stack
//        path.push(maze[random.nextInt(xDimension)][rand.nextInt(yDimension)][rand.nextInt(zDimension)]);
//
//        while (!path.isEmpty()) {
//            // choose the next cell of the
//            path
//        }
//
//    }


    /**
     *
     * @return
     */
    public String toString() {

        StringBuilder output = new StringBuilder();

        String blank = MazeCell2.blankLine;
        for (int l = 0; l < zDimension; l++) { // l for layer
            output.append("Level " + (l+1) + '\n');
            for (int r = 0; r < yDimension; r++) {
                for (int i = 0; i < 3; i++) {
                    for (int c = 0; c < xDimension; c++) {
                        if (maze[c][r][l] == null) {
                            output.append(MazeCell2.blankLine);     // this will be very very useful once we have mazes that
                        }
                        else {
                            output.append((maze[c][r][l]).toString(i));
                        }
                    }
                    output.append('\n');
                }
            }
            output.append("\n\n");
        }
        return output.toString();
    }

}

