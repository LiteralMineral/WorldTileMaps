
import java.util.*;

public class MazeCell2 {
    /** This class will have the following information:
     *      * 2D array of adjacent cells, organized by direction/axis and then magnitude/positivity-or-negativity.
     *              When connecting two cells, the axis will be chosen,
     *              then the positive or negative value along that axis.
     *              { { xNeg, xPos }, { yNeg, yPos}, {zNeg, zPos} }
     *      * 2D array of boolean values denoting whether a traverser can go in that direction
     *              { { xNeg, xPos }, { yNeg, yPos}, {zNeg, zPos} }
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
    public final static String blankLine = "      ";


    /**
     * Using a priority heap here in order to keep the eligible paths close to the beginning.
     * Plus, I can use either random or ordered behavior (by number of visits
     * The actual class information:
     */
    private int [] coordinates; // default length is 3
    private int numDirections; // default length is 3
    private Path [][] paths; // default length is [3][2]
    private int visits = 0;
    private static int maxNumVisits; // default is 1
    private PriorityQueue<Path> unvisitedHeap;
    private ArrayList<Path> visitorsRoutes = new ArrayList<Path>();



    public MazeCell2( int [] newCoordinates, String mazeType) {

        unvisitedHeap = new PriorityQueue<Path>();
        coordinates = newCoordinates;
        numDirections = 3;
        maxNumVisits = 1;
        paths = new Path[numDirections][2];
        for (int i = 0; i < numDirections; i++) {
            paths[i][0] = new Path(this, null, numDirections, 0);
            paths[i][1] = new Path(this, null, numDirections, 1);
            unvisitedHeap.add(paths[i][0]);
            unvisitedHeap.add(paths[i][1]);


        }

    }

    public MazeCell2(int directions, int ... newCoordinates) {
        coordinates = newCoordinates;
        numDirections = directions;
        paths = new Path[numDirections][2];
        for (int i = 0; i < numDirections; i++) {
            paths[i][0] = new Path(this, null, i, 0);
            paths[i][1] = new Path(this, null, i, 1);
        }
    }

    // setters and getters for coordinates
    public int [] getCoordinates() {
        return coordinates;
    }
    public int getNumDirections() {
        return numDirections;
    }
    public Path[][] getPaths() {
        return paths;
    }

    public Path getPath(int axisNum, int directionNum) {
        return paths[axisNum][directionNum];
    }

    public int getNumVisits(){
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
     * TODO: REWRITE
     */
    public void makeAdjacent(MazeCell2 otherCell) {
        if (otherCell != null) {
            int[] index = this.getDirectionIndex(otherCell); // get the {direction id, magnitude id} information.

            if (index[0] < 0 || index[1] < 0) {// if there was an error in getting the direction and magnitude information....
                System.err.println("There was an error with determining a direction.");
            } else {
                int axis = index[0];
                int direction = index[1];
                this.paths[axis][direction].setEndPoint(otherCell);
                otherCell.getPath(axis, direction).setEndPoint(this);
            }

        }
    }


    public void removeFromEligiblePaths(Path target) { // TODO: consider having this return a boolean value to report that it successfully deleted the path....?
        unvisitedHeap.remove(target);
    }
    /**
     * what it says
     */
    public void eraseMyExistenceFromYourMind() {
        Path next = null;
        while (!visitorsRoutes.isEmpty()){
            next = visitorsRoutes.remove(0);
            MazeCell2 origin = next.getEndPoint();
            origin.removeFromEligiblePaths(next); // delete from the origin's list of eligible paths.

        }
    }

    /**
     * This version of the function assumes that the axis number will be provided, and assumes the paths between cells will "point" in opposite directions.
     * It also assumes that the calling object's path will be pointed in the opposite
     * @param otherCell
     * @param axis
     */
    public void makeAdjacent(MazeCell2 otherCell, int axis) {
        if (otherCell != null) {
            this.paths[axis][1].setEndPoint(otherCell); // this is assuming the direction is positive
            otherCell.getPath(axis, 0).setEndPoint(this);
        }
    }


    /**
     * This version assumes both the axis number and the direction number will be available.
     * @param otherCell
     * @param axis
     */
    public void makeAdjacent(MazeCell2 otherCell, int axis, int direction) {
        if (otherCell != null) {
            this.paths[axis][direction].setEndPoint(otherCell); // this is assuming the direction is positive
            otherCell.getPath(axis, (direction + 1) % 2).setEndPoint(this);
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
            return "" + wallNW + ProjectUtilities.repeatObjectString(paths[1][0], 5) + wallNE;
        }
        // second line
        else if (lineNum == 1) { //  can go east? west?
            return "" + paths[0][0] + // can go west?
                    ' ' + paths[2][0] + paths[2][1] + ' ' + // can go down? can go up?
                    paths[0][1]; // can go east?
        }
        // third line
        else {
            return "" + wallSW + ProjectUtilities.repeatObjectString(paths[1][1], 5) + wallSE;
        }
    }

    /**
     *
     * @return string representation of object
     */
    public String toString() {
        StringBuilder output = new StringBuilder();

        // first line
        output.append("" + wallNW + ProjectUtilities.repeatObjectString(paths[1][0], 6) + wallNE);

        // second line
        output.append("\n" + paths[0][0] + // can go west?
                        ' ' + paths[2][0] + paths[2][1] + ' ' + // can go down? can go up?
                        paths[0][1]); // can go east?

        // third line
        output.append("\n" + wallSW + ProjectUtilities.repeatObjectString(paths[1][1], 6) + wallSE + '\n');

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


class Path implements Comparable<Path> {
    public final static char [][] directionBlockades = {{'\u2503', '\u2503'},{'\u2501', '\u2501'},{'\u25b0', '\u25a4'}}; // xDirection, yDirection, zDirection....
    private static Random random = new Random();

    private MazeCell2 startPoint, endPoint;
    private boolean traversable, locked;
    private int axisNum;
    private int directionNum;
    private final int randomizedValue = random.nextInt(8);
    private static final char lockedDoor = '\u25ae'; // possibly also \u25a0....



//  private Key .....  We will eventually have a Key object that can be acquired and can interact with the locked value.

    public Path(MazeCell2 origin, MazeCell2 destination, int axis, int direction) {
        traversable = false;
        locked = false;
        startPoint = origin;
        endPoint = destination;
        axisNum = axis;
        directionNum = direction;

    }

    public MazeCell2 getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(MazeCell2 startPoint) {
        this.startPoint = startPoint;
    }


    public MazeCell2 getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(MazeCell2 endPoint) {
        this.endPoint = endPoint;
    }

    public int getAxisNum() {
        return axisNum;
    }

    public int getDirectionNum() {
        return directionNum;
    }

    public boolean hasEndPoint() {
        return (endPoint != null);
    }

    public Path getInversePath() {
        if (endPoint != null) {
            return endPoint.getPath(this.axisNum, ((directionNum + 1) % 1));
        }
        return null;
    }


    public boolean isTraversable() {
        return traversable;
    }

    public void setTraversable(boolean traversable) {
        this.traversable = traversable;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public void makeRoute() {
        if (endPoint != null){
            this.setTraversable(true);
            this.getInversePath().setTraversable(true);
        }

    }
    public int getRandomizedValue(){
        return randomizedValue;
    }
    public String toString() {

        if (axisNum == 2) {
            return "" + (traversable ? (locked ? "X" : directionBlockades[axisNum][directionNum]) : " " );
        }
        return "" + (traversable ? (locked ? "X" : " ") : directionBlockades[axisNum][directionNum]);
    }

    public boolean equal(Object o) {
        if (o == null) {
            throw new NullPointerException();
        }
        if(o instanceof Path) {
            Path other = (Path) o;
            return this.axisNum == other.getAxisNum()
                    && this.directionNum == other.getDirectionNum()
                    && this.randomizedValue == other.getRandomizedValue(); // might omit this one....
        }
        throw new ClassCastException();
    }

    /**
     * Compares this object with the specified object for order.  Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.
     *
     * <p>The implementor must ensure
     * {@code sgn(x.compareTo(y)) == -sgn(y.compareTo(x))}
     * for all {@code x} and {@code y}.  (This
     * implies that {@code x.compareTo(y)} must throw an exception iff
     * {@code y.compareTo(x)} throws an exception.)
     *
     * <p>The implementor must also ensure that the relation is transitive:
     * {@code (x.compareTo(y) > 0 && y.compareTo(z) > 0)} implies
     * {@code x.compareTo(z) > 0}.
     *
     * <p>Finally, the implementor must ensure that {@code x.compareTo(y)==0}
     * implies that {@code sgn(x.compareTo(z)) == sgn(y.compareTo(z))}, for
     * all {@code z}.
     *
     * <p>It is strongly recommended, but <i>not</i> strictly required that
     * {@code (x.compareTo(y)==0) == (x.equals(y))}.  Generally speaking, any
     * class that implements the {@code Comparable} interface and violates
     * this condition should clearly indicate this fact.  The recommended
     * language is "Note: this class has a natural ordering that is
     * inconsistent with equals."
     *
     * <p>In the foregoing description, the notation
     * {@code sgn(}<i>expression</i>{@code )} designates the mathematical
     * <i>signum</i> function, which is defined to return one of {@code -1},
     * {@code 0}, or {@code 1} according to whether the value of
     * <i>expression</i> is negative, zero, or positive, respectively.
     *
     * @param o the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     * @throws NullPointerException if the specified object is null
     * @throws ClassCastException   if the specified object's type prevents it
     *                              from being compared to this object.
     */
    @Override
    public int compareTo(Path o) {

        if (o == null) {
            throw new NullPointerException();
        }
        if (o instanceof Path) {
            int path1Visits = this.endPoint.getNumVisits();
            int path2Visits = ((Path) o).getEndPoint().getNumVisits();

            return Integer.compare(path1Visits, path2Visits);
        }
        throw new ClassCastException();

    }
}


class RandomPathComparator implements Comparator<Path> {
    /**
     * Compares its two arguments for order.  Returns a negative integer,
     * zero, or a positive integer as the first argument is less than, equal
     * to, or greater than the second.<p>
     * <p>
     * The implementor must ensure that {@code sgn(compare(x, y)) ==
     * -sgn(compare(y, x))} for all {@code x} and {@code y}.  (This
     * implies that {@code compare(x, y)} must throw an exception if and only
     * if {@code compare(y, x)} throws an exception.)<p>
     * <p>
     * The implementor must also ensure that the relation is transitive:
     * {@code ((compare(x, y)>0) && (compare(y, z)>0))} implies
     * {@code compare(x, z)>0}.<p>
     * <p>
     * Finally, the implementor must ensure that {@code compare(x, y)==0}
     * implies that {@code sgn(compare(x, z))==sgn(compare(y, z))} for all
     * {@code z}.<p>
     * <p>
     * It is generally the case, but <i>not</i> strictly required that
     * {@code (compare(x, y)==0) == (x.equals(y))}.  Generally speaking,
     * any comparator that violates this condition should clearly indicate
     * this fact.  The recommended language is "Note: this comparator
     * imposes orderings that are inconsistent with equals."<p>
     * <p>
     * In the foregoing description, the notation
     * {@code sgn(}<i>expression</i>{@code )} designates the mathematical
     * <i>signum</i> function, which is defined to return one of {@code -1},
     * {@code 0}, or {@code 1} according to whether the value of
     * <i>expression</i> is negative, zero, or positive, respectively.
     *
     * @param o1 the first object to be compared.
     * @param o2 the second object to be compared.
     * @return a negative integer, zero, or a positive integer as the
     * first argument is less than, equal to, or greater than the
     * second.
     * @throws NullPointerException if an argument is null and this
     *                              comparator does not permit null arguments
     * @throws ClassCastException   if the arguments' types prevent them from
     *                              being compared by this comparator.
     */
    @Override
    public int compare(Path o1, Path o2) {
        if (o1 == null || o2 == null) {
            throw new NullPointerException();
        }
        if (o1 instanceof Path && o2 instanceof Path) {
            return Integer.compare(((Path) o1).getRandomizedValue(), ((Path) o2).getRandomizedValue());
        }
        throw new ClassCastException();
    }
}
