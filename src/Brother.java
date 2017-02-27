import java.util.ArrayList;
import java.util.List;

/**
 * Created by ian on 12/15/15.
 */
public class Brother {
    public static double THRESHOLD;
    public static double ERROR;
    private double difficulty;
    private List<Zone> zone;
    private String name;
    private String email;

    public Brother(String name, String email) {
        this.zone = new ArrayList<>();
        this.difficulty = 0.0;
        this.name = name;
        this.email = email;
    }

    public void addZones(List<Zone> zones) {
        for (Zone element: zones) {
            if ((difficulty <= THRESHOLD && (difficulty + element.getDifficulty()) <= (THRESHOLD + ERROR))
                    || (difficulty == 0 && element.getDifficulty() >= THRESHOLD)){
                zone.add(element);
                difficulty += element.getDifficulty();
            } else {
                break;
            }
        }
        for (Zone element: zone) {
            zones.remove(element);
        }
    }

    public void forceAddZone(Zone zone) {
        this.zone.add(zone);
        difficulty += zone.getDifficulty();
    }

    public double getDifficulty() {
        return difficulty;
    }
    public List<Zone> getZone() {
        return zone;
    }
    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        String toReturn = name + ": \n";
        for (Zone element: zone) {
            toReturn += element.getType() + ": " + element.getName() + "\n";
        }
        return toReturn;
    }


}
