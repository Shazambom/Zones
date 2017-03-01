import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.*;
import com.sendgrid.*;

/**
 * Created by ian on 12/15/15.
 */
public class Main {
    public static String SG_API_KEY = "";
    public static String USERNAME = "";
    public static void main(String[] args) {
        try {
            PrintWriter out = new PrintWriter(new File("output.txt"));
            List<Brother> brothers = initBrothers();
            List<Zone> zones = initZone();
            out.println("Total Zones: " + zones.size());
            double avg = 0;
            for (Zone element : zones) {
                avg += element.getDifficulty();
            }
            if (Brother.THRESHOLD == 0) {
                Brother.THRESHOLD = avg / brothers.size();
                Brother.ERROR = Brother.THRESHOLD / 10;
            }
            out.println("Average difficulty of each zone: " + (avg / zones.size()));
            out.println("Average difficulty per brother: " + (avg / brothers.size()));
            int count = 1;
            while (zones.size() != 0) {
                for (Brother element : brothers) {
                    element.addZones(zones);
                }
                Brother.ERROR += (avg / brothers.size()) / 10;
                if (count % 10 == 0) {
                    Brother.THRESHOLD++;
                }
                count++;
            }
            out.println("Zones left over: " + zones.size());
            for (Brother element : brothers) {
                out.println(element.toString() + element.getDifficulty() + "\n");
            }
            out.close();
            SendGrid sendgrid = new SendGrid(SG_API_KEY);
            for (Brother element : brothers) {
                sendEmail(USERNAME, element.getEmail(), "Zone", element.toString(), sendgrid);
            }
            String outputString = "";
            try (BufferedReader br = new BufferedReader(new FileReader(new File("output.txt")))) {
                String line;
                while ((line = br.readLine()) != null) {
                    outputString += line + "\n";
                }
                br.close();
            }
            sendEmail(USERNAME, "house@techzbt.com", "Zones", outputString, sendgrid);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static List<Zone> initZone() {
        ArrayList<Zone> zones = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader("ZoneInit.txt"));
            String line = reader.readLine();
            boolean found = false;
            while(line != null) {
                if (found) {
                    int first = line.indexOf(',');
                    int second = line.lastIndexOf(',');
                    zones.add(new Zone(Double.parseDouble(line.substring(first + 1, second)),
                            line.substring(second + 1),
                            Zone.Type.valueOf(line.substring(0, first))));
                }
                if (!found && line.contains("ZONELIST:")) {
                    found = true;
                }
                line = reader.readLine();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        zones.sort(Zone::compareTo);
        return zones;
    }

    private static List<Brother> initBrothers() {
        ArrayList<Brother> brothers = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader("BrothersInit.txt"));
            String line = reader.readLine();
            boolean found = false;
            while(line != null) {
                if (line.contains("THRESHOLD =")) {
                    Brother.THRESHOLD = Double.parseDouble(line.substring(11));
                } else if (line.contains("ERROR =")) {
                    Brother.ERROR = Double.parseDouble(line.substring(7));
                }

                if (found) {
                    int index = line.indexOf(',');
                    brothers.add(new Brother(line.substring(0, index), line.substring(index + 1)));
                }


                if (!found && line.contains("BROTHERSLIST:")) {
                    found = true;
                }
                line = reader.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Collections.shuffle(brothers);
        return brothers;
    }

    public static SendGrid.Response sendEmail(String username, String recipient, String subject, String body, SendGrid sendgrid) throws Exception{
        SendGrid.Email email = new SendGrid.Email();
        email.addTo(recipient);
        email.setFrom(username);
        email.setSubject(subject);
        email.setHtml(body);
        return sendgrid.send(email);
    }
}

