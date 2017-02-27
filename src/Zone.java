/**
 * Created by ian on 10/26/15.
 */
public class Zone implements Comparable{
    public enum Type{Surface, Window, Dishes, Mail, Outside, Floor, Basement, Bathroom, Dust, Trash, Vacuum}
    private double difficulty;
    private String name;
    private Type type;

    public Zone(double difficulty, String name, Type type) {
        this.difficulty = difficulty;
        this.name = name;
        this.type = type;
    }

    public void setDifficulty(double difficulty) {
        this.difficulty = difficulty;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public double getDifficulty() {
        return difficulty;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    @Override
    public int compareTo(Object other) {
        return type.compareTo(((Zone)other).getType());
    }

    @Override
    public String toString() {
        return "Type: " + type + " Difficulty: " + difficulty + " Description: " + name;
    }
}
