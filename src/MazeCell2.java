public class MazeCell2 {
    /** This class will have the following information:
     *      * 2D array of adjacent cells, organized by direction/axis and then magnitude/positivity-or-negativity.
     *              When connecting two cells, the axis will be chosen,
     *              then the positive or negative value along that axis.
     *              { { xNeg, yNeg, zNeg }, { xPos, yPos, zPos } }
     *      * 2D array of boolean values denoting whether a traverser can go in that direction
     *              { { xNeg, yNeg, zNeg }, { xPos, yPos, zPos } }
     *      * ArrayList of adjacent visited cells                         NOTE: Sarah is still puzzling over the best way to handle these things....
     *      * ArrayList of adjacent unvisited cells                       NOTE: Sarah is still puzzling over the best way to handle these things....
     *      * boolean representing whether the node has been visited or not
     *      * potentially, in the future, an ArrayList of items to be found on the given tile (if this is used in a video game)
     *
     *
     * It should be noted that there are a couple of issues with the visitedCells list and the unvisitedCells list:
     *      * connecting two cells with a path and then knowing how to update the relevant canGo value is difficult
     *        since the ArrayList of visited and unvisited adjacent cells don't contain the direction in which the
     *        adjacent cell lies --
     *              * POTENTIAL FIX?: use a Path/Edge object that has the axis, magnitude,
     *                traversability, and adjacent MazeCell as data.
     *      * Since this cell may have been reached by a path that includes another cell, there will be some cases
     *        where an adjacent cell has been visited but hasn't been removed from the ArrayList of
     *
     *
     *
     *
     */
    public final static char wallNW = '\u250f'; // north west
    public final static char wallNE = '\u2513'; // north east
    public final static char wallSW = '\u2517'; // south west
    public final static char wallSE = '\u251b'; // south east
    public final static char wallNS = '\u2501'; // north south
    public final static char wallEW = '\u2503'; // east west
    public final static String blankLine = "     ";


    private int [] coordinates; // default length is 3
    private int numDirections; // default length is 3
    private boolean [][] canGo; // default length is [3][2]
    private MazeCell2 [][] adjacentCells; // default length is [3][2]
    private int visits = 0;
    private int maxNumVisits; // default is 1




    public MazeCell2(int [] newCoordinates) {
        coordinates = newCoordinates;
        numDirections = 3;
        canGo = new boolean[numDirections][2];
//        canGo[1][0] = true;  // used to test output
//        canGo[0][0] = true;  // likewise

        adjacentCells = new MazeCell2[numDirections][2];
    }

    public MazeCell2(int directions) {
        numDirections = directions;
        canGo = new boolean[numDirections][2];
        adjacentCells = new MazeCell2[numDirections][2];
    }

    // setters and getters for coordinates
    public int [] getCoordinates() {
        return coordinates;
    }
    public int getNumDirections() {
        return numDirections;
    }




    public int isVisited(){
        return visits;
    }

    public void resetVisits() {
        visits = 0;
    }
    public void setVisited(int vis) {
        visits = vis;
    }

    public void visit() {
        visits++;
    }

    public boolean[][] getCanGo() {
        return canGo;
    }

    public MazeCell2[][] getAdjacentCells() {
        return adjacentCells;
    }

    /**
     * A function to determine the direction and magnitude in which a pair of MazeCells are connected
     * @param otherCell
     * @return array of two ints, representing the axis index and the direction value
     */
    public int [] getDirectionIndex(MazeCell2 otherCell) {
        int [] index = {-1, -1}; // initialize what the value should be if this function fails
        for (int i = 0; i < numDirections; i++) { // cycle through the MazeCell's coordinates to find the difference
            if (this.coordinates[i] != (otherCell.getCoordinates())[i]) {
                index[0] = i; // store the direction the difference was found in
                int moveVal = (otherCell.getCoordinates())[i] - this.coordinates[i];

                if (Math.abs(moveVal) != 1) { // check that the
                    System.err.println("Either same MazeCell or too far away: " + "\n\t" + this.coordinates + "\n\t" + otherCell.getCoordinates());
                }
                else {
                    index[1] = (moveVal > 0 ? 1 : 0); // set the magnitude/direction if there were no errors. if moveVal is positive, set to one, else 0.
                }
            }
        }
        return index; // if the array returned contains a -1, then an error occurred.
    }

    /**
     * for linking up the adjacent cells...
     * @param otherCell
     */
    public void makeAdjacent(MazeCell2 otherCell) {
        if (otherCell != null) {
            int[] index = this.getDirectionIndex(otherCell); // get the {direction id, magnitude id} information.

            if (index[0] < 0 || index[1] < 0) {// if there was an error in getting the direction and magnitude information....
                System.err.println("There was an error with determining a direction.");
            } else {
                int direction = index[0];
                int posNeg = index[1];
                this.adjacentCells[direction][posNeg] = otherCell;
                (otherCell.getAdjacentCells())[direction][(posNeg + 1) % 2] = this;
            }
        }
    }

    /**
     * function which chooses a random cell from the unvisitedAdjacent ArrayList. If the chosen cell is in fact unvisited, then
     * TODO: write the function
     * @return
     */
    public MazeCell2 randomUnvisitedCell() {
        return null;
    }



    /**
     *
     * @param lineNum
     * @return a given line of the string representation of object
     */
    public String toString(int lineNum) {
        // first line
        if (lineNum == 0) { // can go north? (ORIENTED THE WAY A COMPUTER PRINTS)
            return "" + wallNW + (canGo[1][0] ? "   " : "" + wallNS + wallNS + wallNS) + wallNE;
        }
        // second line
        else if (lineNum == 1) { //  can go east? west?
            return "" + (canGo[0][0] ? ' ' : wallEW) + // can go west?
                    ' ' + (canGo[2][0] ? 'O' : (canGo[2][1] ? '#' : ' ')) + ' ' + // can go up/down?
                    (canGo[0][1] ? ' ' : wallEW); // can go east?
        }
        // third line
        else {
            return "" + wallSW + (canGo[1][1] ? "   " : "" + wallNS + wallNS + wallNS) + wallSE;
        }
    }

    /**
     *
     * @return string representation of object
     */
    public String toString() {
        StringBuilder output = new StringBuilder();

        // first line
        output.append("" + wallNW + (canGo[1][0] ? "   " : "" + wallNS + wallNS + wallNS) + wallNE);

        // second line
        output.append("\n" + (canGo[0][0] ? ' ' : wallEW) + // can go west?
                        ' ' + (canGo[2][0] ? 'O' : (canGo[2][1] ? '#' : ' ')) + ' ' + // can go up/down?
                        (canGo[0][1] ? ' ' : wallEW)); // can go east?

        // third line
        output.append("\n" + wallSW + (canGo[1][1] ? "   " : "" + wallNS + wallNS + wallNS) + wallSE + '\n');

        return output.toString();
    }



    /**
     * This function should return true if and only if the object is a mazeCell2 and
     * the coordinates are all equal to the interior MazeCell2's coordinates.
     * @param o
     * @return
     */
    public boolean equals(Object  o) {
        if (o instanceof MazeCell2) {
            int[] otherCoords = ((MazeCell2) o).getCoordinates();
            int limit = Math.min(this.numDirections, ((MazeCell2) o).getNumDirections());
            for (int i = 0; i < limit; i++) {
                if (this.coordinates[i] != otherCoords[i]) {
                    return false;
                }
            }
            return true;

        }
        else {
            return false;

        }
    }



}
