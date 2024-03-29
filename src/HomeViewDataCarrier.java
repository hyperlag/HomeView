import java.io.Serializable;

/**
 * Object to hold the state of the connected devices. This is what gets passed around by the server and client.
 */
public class HomeViewDataCarrier implements Serializable {
    private static final long serialVersionUID = 1L;

    //Gas Meter Values
    private int co2 = -1;
    private int tvoc = -1;
    private long lastGasEpoc = -1;

    //Air exchanger state
    private boolean airExOn = false;
    private boolean airExCycling = false;
    private long[] airExCycleOnOffMs = new long[]{-1, -1};

    //Age of the data object
    private long lastUpdateEpoch;

    public HomeViewDataCarrier(){
        lastUpdateEpoch = System.currentTimeMillis();
    }

    /**
     * Copy constructor.
     *
     * @param toCopy HomeViewDataCarrier object to copy.
     */
    public HomeViewDataCarrier(HomeViewDataCarrier toCopy){
        this.co2 = toCopy.getCo2();
        this.tvoc = toCopy.getTvoc();
        this.lastGasEpoc = toCopy.getLastGasEpoc();
        this.airExOn = toCopy.isAirExOn();
        this.airExCycling = toCopy.isAirExCycling();
        this.airExCycleOnOffMs = toCopy.getAirExCycleOnOffMs();;
        this.lastUpdateEpoch = toCopy.getLastUpdateEpoch();
    }

    public HomeViewDataCarrier(GasMeter gasMeter) {
        co2 = gasMeter.getCo2();
        tvoc = gasMeter.getTvoc();
        lastGasEpoc = gasMeter.getLastUpdateTime();
        lastUpdateEpoch = System.currentTimeMillis();
    }

    public HomeViewDataCarrier(AirEx airEx) {
        airExOn = airEx.isOn();
        airExCycling = airEx.isCycling();
        airExCycleOnOffMs = airEx.getCycleTimes();
        lastUpdateEpoch = System.currentTimeMillis();
    }

    public HomeViewDataCarrier(GasMeter gasMeter, AirEx airEx) {
        co2 = gasMeter.getCo2();
        tvoc = gasMeter.getTvoc();
        lastGasEpoc = gasMeter.getLastUpdateTime();
        airExOn = airEx.isOn();
        airExCycling = airEx.isCycling();
        airExCycleOnOffMs = airEx.getCycleTimes();
        lastUpdateEpoch = System.currentTimeMillis();
    }

    public HomeViewDataCarrier(int co2, int tvoc, long lastGasEpoc, boolean airExOn, boolean airExCycling,
                               long[] airExCycleOnOffMs) {
        this.co2 = co2;
        this.tvoc = tvoc;
        this.lastGasEpoc = lastGasEpoc;
        this.airExOn = airExOn;
        this.airExCycling = airExCycling;
        this.airExCycleOnOffMs = airExCycleOnOffMs;
        lastUpdateEpoch = System.currentTimeMillis();
    }

    public int getCo2() {
        return co2;
    }

    public int getTvoc() {
        return tvoc;
    }

    public long getLastGasEpoc() {
        return lastGasEpoc;
    }

    public boolean isAirExOn() {
        return airExOn;
    }

    public boolean isAirExCycling() {
        return airExCycling;
    }

    public long[] getAirExCycleOnOffMs() {
        return airExCycleOnOffMs;
    }

    public void setCo2(int co2) {
        this.co2 = co2;
        lastUpdateEpoch = System.currentTimeMillis();
    }

    public void setTvoc(int tvoc) {
        this.tvoc = tvoc;
        lastUpdateEpoch = System.currentTimeMillis();
    }

    public void setLastGasEpoc(long lastGasEpoc) {
        this.lastGasEpoc = lastGasEpoc;
    }

    public void setAirExOn(boolean airExOn) {
        this.airExOn = airExOn;
    }

    public void setAirExCycling(boolean airExCycling) {
        this.airExCycling = airExCycling;
    }

    public void setAirExCycleOnOffMs(long[] airExCycleOnOffMs) {
        this.airExCycleOnOffMs = airExCycleOnOffMs;
    }

    public long getLastUpdateEpoch() {
        return lastUpdateEpoch;
    }

    public void setLastUpdateEpoch(long lastUpdateEpoch) {
        this.lastUpdateEpoch = lastUpdateEpoch;
    }

}
