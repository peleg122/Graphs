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
                        "          <href>http://maps.google.com/mapfiles/kml/pushpin/ylw-pushpin.png</href>\r\n" +
                        "        </Icon>\r\n" +
                        "        <hotSpot x=\"32\" y=\"1\" xunits=\"pixels\" yunits=\"pixels\"/>\r\n" +
                        "      </IconStyle>\r\n" +
                        "    </Style>" +
                        " <Style id=\"banana\">\r\n" +
                        "      <IconStyle>\r\n" +
                        "        <Icon>\r\n" +
                        "          <href>http://maps.google.com/mapfiles/kml/shapes/placemark_circle.png</href>\r\n" +
                        "        </Icon>\r\n" +
                        "        <hotSpot x=\"32\" y=\"1\" xunits=\"pixels\" yunits=\"pixels\"/>\r\n" +
                        "      </IconStyle>\r\n" +
                        "    </Style>" +
                        " <Style id=\"apple\">\r\n" +
                        "      <IconStyle>\r\n" +
                        "        <Icon>\r\n" +
                        "          <href>http://maps.google.com/mapfiles/kml/shapes/placemark_square.png</href>\r\n" +
                        "        </Icon>\r\n" +
                        "        <hotSpot x=\"32\" y=\"1\" xunits=\"pixels\" yunits=\"pixels\"/>\r\n" +
                        "      </IconStyle>\r\n" +
                        "    </Style>" +
                        " <Style id=\"robot\">\r\n" +
                        "      <IconStyle>\r\n" +
                        "        <Icon>\r\n" +
                        "          <href>http://maps.google.com/mapfiles/kml/shapes/heliport.png</href>\r\n" +
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
    public void PlaceMarkRobot(String id,String name, Point3D location) {
        LocalDateTime time = LocalDateTime.now();
        stringBuilder.append(
                "    <Placemark>\r\n" +
                        "<name>"+name+"</name>"+
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

    public void PlaceMarkPath(String id, Point3D location1, Point3D location2) {
        stringBuilder.append(
                "    <Placemark>\r\n" +
                        "<name>"+id +"</name>\r\n"+
                        "      <LineString>\r\n"+
                        "         <coordinates>"
                        + location1.x() + "," + location1.y() + "0 " + location2.x() + "," + location2.y() + "," + "0" +
                        "         </coordinates>\r\n" +
                        "      </LineString>\r\n"+
                        "    </Placemark>\r\n"
        );
    }

    //<Placemark>
    //		<name>PathTest</name>
    //		<styleUrl>#m_ylw-pushpin</styleUrl>
    //		<LineString>
    //			<tessellate>1</tessellate>
    //			<coordinates>
    //				35.19527616884682,32.10356846993754,0 35.1998796272457,32.10479130235084,0
    //			</coordinates>
    //		</LineString>
    //	</Placemark>





    public String kmlEndAndSave() {
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
        return stringBuilder.toString();
    }

    /**
     * save the kml file, add the finished format to the file
     *
     * @param file_name
     */
    public void saveToFile(String file_name) {

        stringBuilder.append("  </Document>\n" +
                "</kml>");

        try {
            Path path = FileSystems.getDefault().getPath(".");
            File file = new File(path.toString() + "//data//" + file_name + ".kml");
            FileWriter writer = new FileWriter(file);
            writer.write(String.valueOf(stringBuilder));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
