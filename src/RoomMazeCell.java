/**
 * This class extends MazeCell2. It adds behavior so that it recursively alerts
 * neighbouring RoomMazeCell instances that the room has been entered,
 */
public class RoomMazeCell extends MazeCell2{
    /**
     * Instance Fields:
     *      This object should know whether the room it's part of has been visited, and should be able to send that message on to adjacent cells in the room.
     *
     */



    /**
     *
     * @param directions
     * @param maxVisits
     * @param newCoordinates
     */
    public RoomMazeCell(int directions, int maxVisits, int... newCoordinates) {
        super(true, directions, maxVisits, newCoordinates);

    }


}
