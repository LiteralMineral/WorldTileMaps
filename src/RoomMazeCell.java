/**
 * This class extends MazeCell2. It adds behavior so that it recursively alerts
 * neighbouring RoomMazeCell instances that the room has been entered,
 */
public class RoomMazeCell extends MazeCell2{
    /**
     * Instance Fields:
     */


    /**
     *
     * @param directions
     * @param maxVisits
     * @param newCoordinates
     */
    public RoomMazeCell(int directions, int maxVisits, int... newCoordinates) {
        super(false, directions, maxVisits, newCoordinates);
    }


}
