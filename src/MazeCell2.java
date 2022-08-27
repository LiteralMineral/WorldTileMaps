
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

    /*
     * block version
     */
    public final static char wallNW = '\u2588'; // '\u250f'; // north west
    public final static char wallNE = '\u2588'; // '\u2513'; // north east
    public final static char wallSW = '\u2588'; // '\u2517'; // south west
    public final static char wallSE = '\u2588'; // '\u251b'; // south east
    public final static char wallNS = '\u2588'; // '\u2501'; // north south
    public final static char wallEW = '\u2588'; // '\u2503'; // east west
    public final static String blankLine = "      ";

    /*
     * box version
     */
//    public final static char wallNW = '\u250f'; // north west
//    public final static char wallNE = '\u2513'; // north east
//    public final static char wallSW = '\u2517'; // south west
//    public final static char wallSE = '\u251b'; // south east
//    public final static char wallNS = '\u2501'; // north south
//    public final static char wallEW = '\u2503'; // east west


    /**
     * Using a priority heap here in order to keep the eligible paths close to the beginning.
     * Plus, I can use either random or ordered behavior (by number of visits
     *
     *
     * The actual class information:
     */
    private static int defaultMaxNumVisits = 1;
    private static Random random = new Random();
    private int [] coordinates; // default length is 3
    private int numDirections; // default length is 3
    private Edge[][] edges; // default length is [3][2]
    private int visits = 0;
    private static int setIDTemplate = 0; // this is to be used for identifying sets when it comes to kruskal's algorithm
    private int setId;
    private int maxNumVisits; // default is 1... potentially allow for it to be within a range
    private PriorityQueue<Edge> randomlySortedPaths = new PriorityQueue<Edge>(new RandomPathComparator());
//    private PriorityQueue<Edge> pathsSortedByNumVisits;
    private ArrayList<Edge> visitorsRoutes = new ArrayList<Edge>();
    private int numUnvisitedNeighbours = 0;



    public MazeCell2(boolean randomChoosing, int directions, int maxVisits, int ... newCoordinates) {

        setId = setIDTemplate++;
        if (randomChoosing) {
            Comparator<Edge> sorter = (new RandomPathComparator());
            randomlySortedPaths = new PriorityQueue<Edge>(sorter);
        }
        else {
            randomlySortedPaths = new PriorityQueue<Edge>();
        }
        coordinates = newCoordinates;
        numDirections = newCoordinates.length;
        maxNumVisits = maxVisits;
        edges = new Edge[numDirections][2];
        for (int i = 0; i < numDirections; i++) {
            edges[i][0] = new Edge(this, null, i, 0);
            edges[i][1] = new Edge(this, null, i, 1);
//            randomlySortedPaths.add(paths[i][0]);
//            randomlySortedPaths.add(paths[i][1]);
        }

    }

    public static MazeCell2 MazeCell2DefaultNumDimensions(int maxVisits, int ... newCoordinates) {
        return new MazeCell2(false, 3, maxVisits, newCoordinates);
    }

    public static MazeCell2 MazeCell2RandomNumMaxVisits(int maxVisitsLower, int maxVisitsUpper, int ... newCoordinates) {

        // here the randomness is provided by the cell having a random number of possible visits. They should be sorted by number of visits allowed
        return new MazeCell2(true, 3, ( 1 + random.nextInt(maxVisitsUpper)), newCoordinates);
    }

    public static MazeCell2 MazeCell2RandomlySortedPaths(int maxNumVisits, int ... newCoordinates) {
        // here the randomness is provided by the adjacent paths being sorted randomly.
        return new MazeCell2(true, 3,maxNumVisits , newCoordinates);

    }



    /**
     * Gets the number of neighbours that can be visited.
     * @return
     */
    public int getNumNeighboursWithOpenRoutes() {
        int result = 0;
        for (int i = 0; i < numDirections; i++) {

            if (edges[i][0] != null && edges[i][0].endPointCanBeVisited()) {
                result++;
            }
            if (edges[i][1] != null && edges[i][1].endPointCanBeVisited()) {
                result++;
            }
        }
        return result;
    }
    // setters and getters for coordinates
    /**
     * TODO: document
     * @return
     */
    public int [] getCoordinates() {
        return coordinates;
    }



    /**
     * TODO: document
     * @return
     */
    public int getNumDirections() {
        return numDirections;
    }

    public int getMaxNumVisits() {
        return maxNumVisits;
    }
    public int getSetId(){
        return setId;
    }

    public void setSetId(int newId) {
        setId = newId;
    }

    public void propogateNewSetId(int newSetId) {
        propogateNewSetId(setId, newSetId);

    }

    /**
     * This does not currently work. fix it or find another way!
     * @param oldSetId
     * @param newSetId
     */
    private void propogateNewSetId(int oldSetId, int newSetId){
        this.setId = newSetId;


        for (int i = 0; i < numDirections; i++) {
            if (getPath(i, 0).hasEndPoint()) {
                if (oldSetId == getPath(i,0).getEndPoint().getSetId()) {
                    (getPath(i, 0).getEndPoint()).propogateNewSetId(oldSetId, newSetId);
                }
            }
            if (getPath(i, 1).hasEndPoint()) {
                if (oldSetId == getPath(i,1).getEndPoint().getSetId()) {
                    (getPath(i, 1).getEndPoint()).propogateNewSetId(oldSetId, newSetId);
                }
            }
        }


    }
    /**
     * TODO: document
     * @return
     */
    public Edge[][] getPaths() {
        return edges;
    }


    /**
     * TODO: document
     * @return
     */
    public Edge getPath(int axisNum, int directionNum) {
        return edges[axisNum][directionNum];
    }

    /**
     * TODO: document
     * @return
     */
    public int getNumVisits(){
        return visits;
    }

    public void addPathtoUnvisitedHeap(Edge p) {
        randomlySortedPaths.add(p);
    }


    /**
     * TODO: document
     * @return
     */
    public void resetVisits() {
        visits = 0;
    }

    /**
     * TODO: document
     * @return
     */
    public void setVisited(int vis) {
        visits = vis;
    }


    /**
     * TODO: document
     * @return
     */
    public void visit() {
        visits++;
        if (!this.canBeVisited()) {
//            this.eraseMyExistenceFromYourMind(); // when the cell's maximum visit count has been reached, it should clear itself from its neighbour's eligible paths.
        }
    }

    /**
     * clears the unvisited edges heap and fills it
     * TODO: document
     * @return
     */

    public void refillUnvisited() { // clear and refill the unvisited heap, only with paths that lead somewhere
        randomlySortedPaths.clear();
        for (int i = 0; i < numDirections; i++) {
            if (edges[i][0].getEndPoint() != null) {
                randomlySortedPaths.add(edges[i][0]);
            }
            if (edges[i][1].getEndPoint() != null) {
                randomlySortedPaths.add(edges[i][1]);
            }
        }
    }


    /**
     *
     * determines whether the mazecell is alright to add to the current path.
     * @return
     */
    public boolean canBeVisited() {
        return (visits < maxNumVisits);
    }

    /**
     * TODO: document
     * @return
     */
    public boolean hasUnivisitedNeighbors() {
        return !randomlySortedPaths.isEmpty();
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
                this.edges[axis][direction].setEndPoint(otherCell); // now that it has an endpoint it can be added to the heap.
                randomlySortedPaths.add(this.edges[axis][direction]);
                otherCell.getPath(axis, direction).setEndPoint(this);
            }

        }
    }



    /**
     * Takes in the target path and removes it from the set of eligible, unvisited paths.
     * @param target
     */
    public void removeFromEligiblePaths(Edge target) { // TODO: consider having this return a boolean value to report that it successfully deleted the path....?
        randomlySortedPaths.remove(target);
    }
    /**
     * what it says
     */
    public void eraseMyExistenceFromYourMind() {
        Edge next = null;
//        while (!visitorsRoutes.isEmpty()){
//            next = visitorsRoutes.remove(0);
//            MazeCell2 origin = next.getEndPoint();
//            origin.removeFromEligiblePaths(next); // delete from the origin's list of eligible paths.

//        }
    }

    /**
     * This version of the function assumes that the axis number will be provided, and assumes the paths between cells will "point" in opposite directions.
     * It also assumes that the calling object's path will be pointed in the opposite
     * @param otherCell
     * @param axis
     */
    public void makeAdjacent(MazeCell2 otherCell, int axis) {
        if (otherCell != null) {
            this.edges[axis][1].setEndPoint(otherCell); // this is assuming the direction is positive
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
            this.edges[axis][direction].setEndPoint(otherCell); // this is assuming the direction is positive
            otherCell.getPath(axis, (direction + 1) % 2).setEndPoint(this);
        }
    }




    /**
     * function which chooses a random (by sorting randomly valued data fields) path from the PriorityQueue that has the next cell to be traversed.
     * If the chosen path is in fact unvisited, then it removes the path and returns the cell at the path's endpoint.
     * TODO: write the function
     * @return
     */
    public Edge removeNextPath() {
        return randomlySortedPaths.remove();
    }



    /**
     *
     * @param lineNum
     * @return a given line of the string representation of object
     */
    public String toString(int lineNum) {
        // first line
        if (lineNum == 0) { // can go north? (ORIENTED THE WAY A COMPUTER PRINTS)
            return "" + wallNW + ProjectUtilities.repeatObjectString(edges[1][0], 2) + wallNE; // repeat should be 4
        }
        // second line
        else if (lineNum == 1) { //  can go east? west?
            return "" + edges[0][0] + // can go west?
                    "" + edges[2][0] + edges[2][1] + "" + // can go down? can go up? // the spaces should ordinarily be single spaces on either side
                    edges[0][1]; // can go east?
        }
        // third line
        else {
            return "" + wallSW + ProjectUtilities.repeatObjectString(edges[1][1], 2) + wallSE;
        }
    }

    /**
     *
     * @return string representation of object
     */
    public String toString() {
        StringBuilder output = new StringBuilder();

        // first line
        output.append("" + wallNW + ProjectUtilities.repeatObjectString(edges[1][0], 4) + wallNE);

        // second line
        output.append("\n" + edges[0][0] + // can go west?
                        " " + edges[2][0] + edges[2][1] + " " + // can go down? can go up?
                        edges[0][1]); // can go east?

        // third line
        output.append("\n" + wallSW + ProjectUtilities.repeatObjectString(edges[1][1], 4) + wallSE + '\n');

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


class Edge implements Comparable<Edge> {
//    public final static char [][] directionBlockades = {{'\u2503', '\u2503'},{'\u2501', '\u2501'},{'\u25b0', '\u25a4'}}; // xDirection, yDirection, zDirection....
//    public final static char [][] directionBlockades = {{'\u2503', '\u2503'},{'\u2501', '\u2501'},{'\u25bc', '\u25b2'}}; // xDirection, yDirection, zDirection....
public final static char [][] directionBlockades = {{'\u2588', '\u2588'},{'\u2588', '\u2588'},{'\u25bc', '\u25b2'}}; // xDirection, yDirection, zDirection....


//    public final static char [][] directionBlockades = {{'|', '|'},{'_', '_'},{'O', '#'}}; // xDirection, yDirection, zDirection....

    private static Random random = new Random();


    private MazeCell2 startPoint, endPoint;
    private int [] startPointLoc, endPointLoc;
    private boolean traversable, locked;
    private int axisNum;
    private int directionNum;

    private int randomizedValue;
    private static final char lockedDoor = '\u25ae'; // possibly also \u25a0....



//  private Key .....  We will eventually have a Key object that can be acquired and can interact with the locked value.

    public Edge(MazeCell2 origin, MazeCell2 destination, int axis, int direction) {
        traversable = false;
        locked = false;
        startPoint = origin;
        endPoint = destination;
        axisNum = axis;
        directionNum = direction;
        int randomnessMultiple = 10;
        randomizedValue = random.nextInt( (directionNum < 3 ? 2 : directionNum) * randomnessMultiple) + (directionNum < 3 ? 0 : randomnessMultiple * 2); // skew the likelihood of 3rd and 4th dimensions being taken

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

    public Edge getInverseEdge() {
        if (endPoint != null) {
            return endPoint.getPath(this.axisNum, ((directionNum + 1) % 2));
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
            this.getInverseEdge().setTraversable(true);
        }

    }

    /**
     * returns true if the endpoint exists and can be visited
     * @return
     */
    public boolean endPointCanBeVisited() {
        if (endPoint != null) {
            return endPoint.canBeVisited();
        }
        return false;
    }

    /**
     * returns the randomized value that allows the edges to be sorted randomly by the comparator
     * @return
     */
    public int getRandomizedValue(){
        return randomizedValue;
    }
    public String toString() {

//        return "" + axisNum;
        if (axisNum < 2) {
            return "" + (traversable ? (locked ? "X" : " ") : directionBlockades[axisNum][directionNum]);

        }
        else {
            return "" + (traversable ? (locked ? "X" : directionBlockades[axisNum][directionNum]) : " ");
        }
    }

    public boolean equal(Object o) {
        if (o == null) {
            throw new NullPointerException();
        }
        if(o instanceof Edge) {
            Edge other = (Edge) o;
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
    public int compareTo(Edge o) {

        if (o == null) {
            throw new NullPointerException();
        }
        if (o instanceof Edge) {

            int path1Visits = this.endPoint.getNumVisits();
            int path2Visits = ((Edge) o).getEndPoint().getNumVisits();

            return Integer.compare(path1Visits, path2Visits);
        }
        throw new ClassCastException();

    }
}


class RandomPathComparator implements Comparator<Edge> {
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
    public int compare(Edge o1, Edge o2) {
        if (o1 == null || o2 == null) {
            throw new NullPointerException();
        }
        if (o1 instanceof Edge && o2 instanceof Edge) {
            return Integer.compare(((Edge) o1).getRandomizedValue(), ((Edge) o2).getRandomizedValue());
        }
        throw new ClassCastException();
    }
}
