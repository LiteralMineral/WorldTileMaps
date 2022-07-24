public class MazeGenerator {
    public static void main(String [] args) {
        MazeArray maze = new MazeArray(25, 25, 25);

        maze.fillArrayBaseball();

        System.out.print(maze);
    }

}
