import java.util.ArrayList;

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
    private int numDimensions = 3; // This is intended to remind myself that I want to figure out a way to make an N-dimensional maze.
    private int xDimension, yDimension, zDimension;
    private MazeCell2[][][] maze;
    private MazeCell2 start, end, center, current; // "center" may be randomized.... I am considering options.
//    private ArrayList<Shape> shapesInMaze; TODO: implement shape

    /**
     * MazeArray(...)
     * initialize the dimensions of the maze, and initialize the array that stores the MazeCells.
     * TODO: Possibly create an alternate constructor that uses the fillArray(String str)
     * @param x
     * @param y
     * @param z
     */
    public MazeArray(int x, int y, int z) {

        xDimension = x;
        yDimension = y;
        zDimension = z;
        maze = new MazeCell2[xDimension][yDimension][zDimension];
    }

    /**
     * This function fills the array in a cube by traversing entire array. All possible positions will be filled.
     * NOTE: I actually think a better, more efficient way of filling the array that allows for more complicated structures is to use an equation object and fill it from lower to upper bounds...
     */
    public void fillArray() {


        // c for column (the x value),
        // r for the row (y value),
        // l for the layer (z value)
        for (int l = 0; l < zDimension; l++) {
            for (int c = 0; c < xDimension; c++) {
                for (int r = 0; r < yDimension; r++) {
                    maze[c][r][l] = new MazeCell2(numDimensions,c, r, l);
                }
            }
        }


    }

    public void connectArray() {

        // connect the given cell to the cells in the positive x, y, and z directions.
        // Be sure to write MazeCell.makeAdjacent such that it can handle a null pointer,
        // otherwise include conditions for whether the pair of MazeCells will be made adjacent.
        // That last part is not relevant for the entire-array-gets-filled variety of fillArray(),
        // but it will become relevant when
        for (int l = 0; l < zDimension - 1; l++) {
            for (int c = 0; c < xDimension - 1; c++) {
                for (int r = 0; r < yDimension - 1; r++) {
                    if (maze[c][r][l] != null) {
                        maze[c][r][l].makeAdjacent(maze[c + 1][r][l],0); // make adjacent with horizontal
                        maze[c][r][l].makeAdjacent(maze[c][r + 1][l],1); // make adjacent with vertical
                        maze[c][r][l].makeAdjacent(maze[c][r][l + 1],2); // make adjacent with other layer
                        if (c % 5 == l % 5 && r % 3 == l % 5) {
                            maze[c][r][l].getPath(2,1).makeRoute();
                        }
                    }
                }
            }
        }

    }


    /**
     * fills the array in the shape of a baseball field. (really it's a sphere, but.... I wanted
     * the actual sphere function to be named sensibly and this is just a placeholder to test the function-based filling)
     */
    public void fillArrayBaseball(){
        int radius = Math.min(Math.min(xDimension, yDimension), zDimension); // find the limiting radius of the baseball sweep.

        // c for column (the x value),
        // r for the row (y value),
        // l for the layer (z value)
        for (int l = 0; l < zDimension; l++) {
            for (int c = 0; c < xDimension; c++) {
                for (int r = 0; r < yDimension; r++) {
                    if (Math.sqrt(Math.pow(c, 2) + Math.pow(r, 2) + Math.pow(l, 2)) < radius) {
                        maze[c][r][l] = new MazeCell2(numDimensions, c, r, l);
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

