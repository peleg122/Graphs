package gameClient;

import Server.Fruit;
import utils.Point3D;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;


public class KML_Logger {

    private int level;
    private StringBuilder stringBuilder;

    /**
     * Default constructor
     */
    public KML_Logger() {
    }

    public KML_Logger(int stage) {
        this.level = stage;
        stringBuilder = new StringBuilder();
        kmlStart();
    }


    public void kmlStart() {
        stringBuilder.append(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" +
                        "<kml xmlns=\"http://earth.google.com/kml/2.2\">\r\n" +
                        "  <Document>\r\n" +
                        "    <name>" + "Game stage :" + this.level + "</name>" + "\r\n" +
                        " <Style id=\"node\">\r\n" +
                        "      <IconStyle>\r\n" +
                        "        <Icon>\r\n" +
                        "          <href>http://maps.google.com/mapfiles/kml/pal3/icon35.png</href>\r\n" +
                        "        </Icon>\r\n" +
                        "        <hotSpot x=\"32\" y=\"1\" xunits=\"pixels\" yunits=\"pixels\"/>\r\n" +
                        "      </IconStyle>\r\n" +
                        "    </Style>" +
                        " <Style id=\"fruit-banana\">\r\n" +
                        "      <IconStyle>\r\n" +
                        "        <Icon>\r\n" +
                        "          <href>http://maps.google.com/mapfiles/kml/pal5/icon49.png</href>\r\n" +
                        "        </Icon>\r\n" +
                        "        <hotSpot x=\"32\" y=\"1\" xunits=\"pixels\" yunits=\"pixels\"/>\r\n" +
                        "      </IconStyle>\r\n" +
                        "    </Style>" +
                        " <Style id=\"fruit-apple\">\r\n" +
                        "      <IconStyle>\r\n" +
                        "        <Icon>\r\n" +
                        "          <href>http://maps.google.com/mapfiles/kml/pal5/icon56.png</href>\r\n" +
                        "        </Icon>\r\n" +
                        "        <hotSpot x=\"32\" y=\"1\" xunits=\"pixels\" yunits=\"pixels\"/>\r\n" +
                        "      </IconStyle>\r\n" +
                        "    </Style>" +
                        " <Style id=\"robot\">\r\n" +
                        "      <IconStyle>\r\n" +
                        "        <Icon>\r\n" +
                        "          <href>http://maps.google.com/mapfiles/kml/pal4/icon26.png></href>\r\n" +
                        "        </Icon>\r\n" +
                        "        <hotSpot x=\"32\" y=\"1\" xunits=\"pixels\" yunits=\"pixels\"/>\r\n" +
                        "      </IconStyle>\r\n" +
                        "    </Style>"
        );
    }


    public void PlaceMark(String id, Point3D location) {
        LocalDateTime time = LocalDateTime.now();
        stringBuilder.append(
                "    <Placemark>\r\n" +
                        "      <TimeStamp>\r\n" +
                        "        <when>" + time + "</when>\r\n" +
                        "      </TimeStamp>\r\n" +
                        "      <styleUrl>#" + id + "</styleUrl>\r\n" +
                        "      <Point>\r\n" +
                        "         <coordinates>" + location.x() + "," + location.y() + "</coordinates>\r\n" +
                        "      </Point>\r\n" +
                        "    </Placemark>\r\n"
        );


    }


    public void kmlEndAndSave() {
        stringBuilder.append("  \r\n</Document>\r\n" +
                "</kml>"
        );
        try {
            File file = new File(this.level + ".kml");
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(stringBuilder.toString());
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * add the location of the fruits and robots to the kml file
     * @param robots
     * @param fruits
     */
    public void addRobotsFruits(HashMap<Integer, Robot> robots,
                                HashMap<Point3D, Fruits> fruits) {
        Date date = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat df2 = new SimpleDateFormat("HH:mm:ss");
        String timeStr = df.format(date);
        String timeStr2 = df2.format(date);
        String finalDate = timeStr+"T"+timeStr2+"Z";

        for (Robot robot : robots.values()) {
            stringBuilder.append("<Placemark>\n" + "      <TimeStamp>\n" + "        <when>").append(finalDate).append("</when>\n").append("      </TimeStamp>\n").append("      <styleUrl>#hiker-icon</styleUrl>\n").append("      <Point>\n").append("        <coordinates>").append(robot.getLocation().x()).append(",").append(robot.getLocation().y()).append(",0</coordinates>\n").append("      </Point>\n").append("    </Placemark>\n");
        }
        for (Fruits fruit : fruits.values()) {
            String typer = "#paddle-a";
            if (fruit.getType() == -1){
                typer = "#paddle-b";
            }
            stringBuilder.append("<Placemark>\n" + "      <TimeStamp>\n" + "        <when>").append(finalDate).append("</when>\n").append("      </TimeStamp>\n").append("      <styleUrl>").append(typer).append("</styleUrl>\n").append("      <Point>\n").append("        <coordinates>").append(fruit.getLocation().x()).append(",").append(fruit.getLocation().y()).append(",0</coordinates>\n").append("      </Point>\n").append("    </Placemark>\n");
        }


    }

    /**
     * save the kml file, add the finished format to the file
     * @param file_name
     */
    public void saveToFile(String file_name){

        stringBuilder.append("  </Document>\n" +
                "</kml>");

        try {
            Path path = FileSystems.getDefault().getPath(".");
            File file = new File(path.toString() +"//data//" +  file_name + ".kml");
            FileWriter writer = new FileWriter(file);
            writer.write(String.valueOf(stringBuilder));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
