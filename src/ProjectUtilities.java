public class ProjectUtilities {

    public static String repeatChar(char c, int n) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < n; i++) {
            str.append(c);
        }
        return str.toString();
    }
    public static String repeatStringCharacter(String s, int n) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < n; i++) {
            str.append(s);
        }
        return str.toString();
    }
    public static String repeatObjectString(Object obj, int n) {

        StringBuilder str = new StringBuilder();
        for (int i = 0; i < n; i++) {
            str.append(obj);
        }
        return str.toString();
    }
}
