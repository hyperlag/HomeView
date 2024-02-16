import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class HomeViewClient {
    public static void main(String[] args) {
        System.out.println("Launching HomeView Client");
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

        int serverPort = Integer.parseInt(properties.getProperty("serverPort"));
        String serverAddr = properties.getProperty("serverAddress");

        HomeViewDesktop desktop = new HomeViewDesktop();

        try{
            while (true) {

                //Server code
                Socket socket =  new Socket(serverAddr, serverPort);
                System.out.println("Connecting to server " + serverAddr + ":" + serverPort);

                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                System.out.println("Streams Setup");

                while (socket.isConnected()) {
                    System.out.println("Reading Object");
                    HomeViewDataCarrier serverView = (HomeViewDataCarrier) ois.readObject();
                    System.out.println("Server Update received from " + serverAddr);
                    System.out.println("CO2: " + serverView.getCo2() + "ppm |  TVOC: " + serverView.getTvoc() );
                    System.out.println("Air Exchanger On: " + serverView.isAirExOn());
                    System.out.println("Air Exchanger Cycling: " + serverView.isAirExCycling());
                    System.out.println("------------------------------------");

                    desktop.consume(serverView);

                    Thread.sleep(2000); //TODO deal with this
                    //Make a copy of the server view them modify it before sending back
                    //TODO: Do some stuff here
                    HomeViewDataCarrier responseView = new HomeViewDataCarrier(serverView);
                    oos.writeObject(responseView);
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
