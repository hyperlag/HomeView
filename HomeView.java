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
            System.out.println("Loading properties: " + args[0]);
            properties.load(Files.newInputStream(Paths.get(args[0])));
        } catch (IOException e) {
            System.err.println("TODO: Handle IO exception #1");
            return;
        }

        String gasTty = properties.getProperty("gasTty");
        int gasBaud = Integer.parseInt(properties.getProperty("gasBaud"));

//        SerialHelper gasMeter = new SerialHelper(properties.getProperty("gasTty"),
//                Integer.parseInt(properties.getProperty("gasBaud")));

        SerialHelper airEx = new SerialHelper(properties.getProperty("airExTty"),
                Integer.parseInt(properties.getProperty("airExBaud")));


//        System.out.println("Starting Air Exchanger");
//        airEx.writeLine("+");
//        try {
//            Thread.sleep(10000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//        System.out.println("Starting Air Exchanger");
//        airEx.writeLine("-");
        GasMeter gasMeter = new GasMeter(gasTty, gasBaud, 100000);
        gasMeter.run();

        try{
            while (true) {
                Thread.sleep(1000);
                System.out.println("Gas CO2: " + gasMeter.getCo2());
            }
        } catch (Exception e) {

        }
    }
}
