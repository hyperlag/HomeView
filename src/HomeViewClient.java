import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

/**
 * MVC remote client for the HomeView service.
 *
 * TODO: This is a mess of race conditions. Fix this all up and remove the sleeps.
 */
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
        int maxClientHistory = Integer.parseInt(properties.getProperty("maxClientHistory"));

        HomeViewDesktop desktop = new HomeViewDesktop();

        try{
            while (true) {

                //Overriding the LinkedHashMap removeEldestEntry method to limit the size of the history to maxClientHistory entries.
                LinkedHashMap<Long,HomeViewDataCarrier> history = new LinkedHashMap<>(){
                    @Override
                    protected boolean removeEldestEntry(Map.Entry<Long, HomeViewDataCarrier> eldest)
                    {
                        return this.size() > maxClientHistory; //Max entries
                    }
                };

                //Server code
                Socket socket =  new Socket(serverAddr, serverPort);
                System.out.println("Connecting to server " + serverAddr + ":" + serverPort);

                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                System.out.println("Streams Setup");

                while (socket.isConnected()) {
                    System.out.println("Reading Object");
                    HomeViewDataCarrier serverView = new HomeViewDataCarrier((HomeViewDataCarrier) ois.readObject());
                    if (serverView != null && !history.containsKey(serverView.getLastUpdateEpoch())) {
                        System.out.println("Debug New Key " + serverView.getLastUpdateEpoch());
                        history.put(serverView.getLastUpdateEpoch(), serverView);
                    }
                    System.out.println("Server Update received from " + serverAddr);
                    System.out.println("CO2: " + serverView.getCo2() + "ppm |  TVOC: " + serverView.getTvoc() +"ppb");
                    System.out.println("Air Exchanger On: " + serverView.isAirExOn());
                    System.out.println("Air Exchanger Cycling: " + serverView.isAirExCycling());
                    //TODO: Figure out why cycle times are always -1 despite what is in the server object
                    System.out.println("Cycle On: " + serverView.getAirExCycleOnOffMs()[0]);
                    System.out.println("Cycle Off: " + serverView.getAirExCycleOnOffMs()[1]);
                    System.out.println("------------------------------------");

                    desktop.consumeHistory(history);
                    desktop.consume(serverView);


                    Thread.sleep(2000); //TODO deal with this 2000
                    //Make a copy of the server view them modify it before sending back
                    //TODO: Do some stuff here
                    HomeViewDataCarrier responseView = desktop.getData();

                    System.out.println("Sending cycle: " + responseView.isAirExCycling());

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
