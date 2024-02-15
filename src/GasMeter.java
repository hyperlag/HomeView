/**
 * Class to manage my custom air quality sensor.
 */
public class GasMeter {
    private SerialHelper serialHelper;
    private int co2; //CO2 ppm
    private int tvoc; //TVOC ppb
    private int readTimeout; //ms
    private long lastUpdateTime = 0;

    private boolean alive;

    /**
     * Create a new instance of GasMeter.
     *
     * @param tty Port to connect to it on.
     * @param baud Baud rate.
     * @param readTimeout Read timeout. This needs to be pretty high for my device.
     */
    public GasMeter(String tty, int baud, int readTimeout) {
        this.readTimeout = readTimeout;
        serialHelper = new SerialHelper(tty, baud);
    }

    /**
     * Kick off a thread that will keep the object updated with fresh data until the thread is killed.
     */
    public void run() {
        alive = true;
        Thread t = new Thread(new Runnable(){
            @Override
            public void run() {
                while (alive) {
                    readValues();
                }
            }
        });
        t.start();
    }

    /**
     * Get the latest CO2 reading in PPM.
     *
     * @return latest CO2 reading.
     */
    public int getCo2() {
        return co2;
    }

    /**
     * Get the latest TVOC readings in PPB.
     *
     * @return latest PPB reading.
     */
    public int getTvoc(){
        return tvoc;
    }

    /**
     * Get the system time (epoch) of the last update.
     *
     * @return Last update epoch.
     */
    public long getLastUpdateTime() {
        return lastUpdateTime;
    }

    /**
     * Kill the update thread.
     */
    public void stop() {
        alive = false;
    }

    /**
     * Reads current values from the device.
     *
     * @return true if the read worked.
     */
    private boolean readValues() {
        String line = serialHelper.readLine(readTimeout);
        //Try upt to 4 times to get a valid read. This gets rid of the initialization noise.
        for (int i = 0; i < 4; i++) {
            if (line.contains("CO2")) {
                co2 = Integer.parseInt(line.split(",")[0].split(" ")[1].split("ppm")[0]);
                tvoc = Integer.parseInt(line.split(",")[1].split(" ")[2].split("ppb")[0]);
                lastUpdateTime = System.currentTimeMillis();
                return true;
            }
        }
        return false;
    }

}
