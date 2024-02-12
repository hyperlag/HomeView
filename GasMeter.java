public class GasMeter {
    private SerialHelper serialHelper;
    private int co2; //CO2 ppm
    private int tvoc; //TVOC ppb
    private int readTimeout; //ms
    private long lastUpdateTime = 0;

    private boolean alive;
    public GasMeter(String tty, int baud, int readTimeout) {
        this.readTimeout = readTimeout;
        serialHelper = new SerialHelper(tty, baud);
    }

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

    public int getCo2() {
        return co2;
    }

    public int getTvoc(){
        return tvoc;
    }

    public long getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void stop() {
        alive = false;
    }
    private boolean readValues() {
        String line = serialHelper.readLine(readTimeout);
        System.out.println(line); //TODO: Remove this
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
