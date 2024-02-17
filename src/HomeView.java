import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class HomeView {
    private static ServerSocket server;

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

        String airExTty = properties.getProperty("airExTty");
        int airExBaud = Integer.parseInt(properties.getProperty("airExBaud"));

        int serverPort = Integer.parseInt(properties.getProperty("serverPort"));


//        SerialHelper gasMeter = new SerialHelper(properties.getProperty("gasTty"),
//                Integer.parseInt(properties.getProperty("gasBaud")));

//        SerialHelper airEx = new SerialHelper(properties.getProperty("airExTty"),
//                Integer.parseInt(properties.getProperty("airExBaud")));


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

        AirEx airEx = new AirEx(airExTty, airExBaud);
//        airEx.startCycleThread(60000, 180000);

        try{
            long lastUpdate = 0;
            server = new ServerSocket(serverPort);

            while (true) {
                Thread.sleep(1000);
                long curUpdate = gasMeter.getLastUpdateTime();
                if (lastUpdate < curUpdate) {
                    System.out.println("Gas CO2: " + gasMeter.getCo2());
                    System.out.println("Gas TVOC: " + gasMeter.getTvoc());
                    lastUpdate = curUpdate;
                }

                //Server code
                Socket socket = server.accept();
                System.out.println("New server connection " + server.getInetAddress());


                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                //oos.flush();
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

                //Initialize
                HomeViewDataCarrier viewOut = new HomeViewDataCarrier(gasMeter, airEx);
                oos.writeObject(viewOut);

                Thread.sleep(500); //TODO deal with

                HomeViewDataCarrier viewIn = (HomeViewDataCarrier) ois.readObject();

                System.out.println("View read");
                while (viewIn != null && socket.isConnected()) { //Send a null to disconnect
                    if (viewIn.isAirExOn()) {
                        airEx.on();
                    } else {
                        airEx.off();
                    }

                    if (viewIn.isAirExCycling()) {
                        System.out.println("Server received a cycle command");
                        Thread.sleep(100); //TODO
                        airEx.startCycleThread(viewIn.getAirExCycleOnOffMs()[0], viewIn.getAirExCycleOnOffMs()[1]);
                    } else if (airEx.isCycling()){
                        System.out.println("Server received a STOP cycle command");
                        airEx.stopCycleThread();
                    }

                    Thread.sleep(1000);
                    //TODO: Process incoming object
                    System.out.println("Update read");
                    viewOut = new HomeViewDataCarrier(gasMeter, airEx);
                    System.out.println("View Out " + viewOut.getAirExCycleOnOffMs()[0] + " " + airEx.getCycleTimes()[0]);
                    oos.writeObject(viewOut); //TODO somehow the cycle times always leave here as -1 despite this ^
                    Thread.sleep(100); //TODO
                    viewIn = (HomeViewDataCarrier) ois.readObject();
                    Thread.sleep(100); //TODO deal with this
                }


                oos.close();
                ois.close();
                socket.close();
            }
        } catch (Exception e) {
            System.err.println("Error  #1 " + e.getMessage());
        }
    }

}
