import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class HomeView {

    //Gas = ttyUSB1
    //AirEx = /dev/ttyUSB0
    //Arguments = properties file
    public static void main(String[] args) {
        System.out.println("Launching HomeView");
        if (args.length != 1) {
            System.err.println("Error: Must supply properties file");
            return;
        }

        Properties properties = new Properties();
        try {
            properties.load(Files.newInputStream(Paths.get("args[0]")));
        } catch (IOException e) {
            System.err.println("TODO: Handle IO exception #1");
            return;
        }

        SerialHelper gasMeter = new SerialHelper(properties.getProperty("gasTty"),
                Integer.parseInt(properties.getProperty("gasBaud")));
    }
}
