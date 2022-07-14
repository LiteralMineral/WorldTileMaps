public class MazeCell {
    private int xLoc, yLoc;
    private boolean visited;
    /*In order to quickly negate directions without a dedicated function, the directions will be indexed as follows:
            0 : North
            1 : East
            2 : Up
            3 : Down
            4 : West
            5 : South
      So that we will have:
      canGo = {canGoNorth,canGoEast,canGoUp,canGoDown,canGoWest,canGoSouth}*/
    private boolean [] canGo = {false, false, false, false, false, false};
    /* The same logic applies to an array of */
    private MazeCell [] adjacent = {null, null, null, null, null, null};

    public MazeCell(int x, int y) {
        xLoc = x;
        yLoc = y;
        visited = false;
    }
    public MazeCell(int x, int y, int directionIndex) {
        xLoc = x;
        yLoc = y;
        visited = false;
    }

    public boolean [] getCanGo() {
        return canGo;
    }

    public boolean getCanGo(int index) {
        return canGo[index];
    }

    public void setCanGo(int index, boolean accessible) {
        canGo[index] = accessible;
    }
    public boolean getVisited() {
        return visited;
    }
    public void setVisited(boolean visit) {
        visited = visit;
    }



    public MazeCell[] getAdjacent() {
        return adjacent;
    }
    public MazeCell getAdjacent(int index) {
        return adjacent[index];
    }

    public void linkCells(MazeCell m) {


    }

//    returns the correct line for printing:
    public String toString(int lineNum) {
        if (lineNum == 0) { // can go north?
            return "X" + (canGo[0] ? ' ' : 'X') + 'X';
        }
        else if (lineNum == 1) { //  can go east? west?
            return "" + (canGo[1] ? ' ' : 'X') + // can go east?
                    (canGo[2] ? 'O' : (canGo[3] ? '#' : ' ')) +  // can go up/down?
                    (canGo[4] ? ' ' : 'X'); // can go west?
        }
        else {
            return "X" + (canGo[5] ? ' ' : 'X') + "X";
        }
    }


}
