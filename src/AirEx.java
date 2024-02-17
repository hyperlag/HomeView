public class AirEx {
    private SerialHelper serialHelper;
    private boolean cycling = false;
    private boolean isOn = false;

    private long cycleOnMs = -1;
    private long cycleOffMs = -1;

    //private long[] onOff = new long[]{-1, -1};

    public AirEx(String tty, int baud) {
        serialHelper = new SerialHelper(tty, baud);
        System.out.println("Connected to air exchanger");
    }

    /**
     * Turn on the air exchanger.
     */
    public void on(){
        serialHelper.writeLine("+");
        isOn = true;
        System.out.println("Turning Air Exchanger on");
    }

    /**
     * Turn off the air exchanger.
     */
    public void off(){
        serialHelper.writeLine("-");
        isOn = false;
        System.out.println("Turning Air Exchanger off");
    }

    /**
     * Run the air exchanger for a fixed number of milliseconds.
     *
     * Note: This is blocking.
     *
     * @param ms Time in ms to run exchanger
     */
    public void on(long ms) {
        on();
        sleep(ms);
        off();
    }

    /**
     * Blocking cycle method. Will cycle between on and off.
     * Thread mutex will kill the loop if triggered with stopCycleThread method.
     *
     * @param msOn Milliseconds to remain on.
     * @param msOff Milliseconds to remain off.
     */
    public void cycle(long msOn, long msOff) {
        cycling = true;
        on(msOn);
        if (!cycling) {
            return; // Kill this if being run in a thread and the mutex fired.
        }
        while (sleep(msOff)) {
            if (!cycling) {
                break; //Break out of the loop if the Thread mutex is triggered
            }
            on(msOn);
            if (!cycling) {
                break; //Break out of the loop if the Thread mutex is triggered
            }
        }

    }

    /**
     * Non-blocking. This will kick off a new thread and cycle until stopCycleThread method is called.
     *
     * @param msOn Milliseconds to remain on.
     * @param msOff Milliseconds to remain off.
     */
    public void startCycleThread(long msOn, long msOff) {
        System.out.println("Starting cycle " + msOn + " " + msOff);

        if (msOn < 30000 || msOff < 30000) {
            cycling = false;
            System.err.println("Error 30s minimum cycle time");
            return;
        }
        Thread t = new Thread(new Runnable(){
            @Override
            public void run() {
                cycleOnMs = msOn;
                cycleOffMs = msOff;
                cycle(msOn, msOff);
            }
        });
        t.start();
    }

    /**
     * Stop cycle thread and stops the air exchanger.
     *
     * Note: This will complete the current on/off cycle before killing the thread.
     */
    public void stopCycleThread() {
        System.out.println("Stopping AirEx cycle");
        cycling = false;
        off();
    }

    public boolean isOn() {
        return isOn;
    }

    public boolean isCycling(){
        return cycling;
    }

    public long[] getCycleTimes(){
        return new long[] {cycleOnMs, cycleOffMs};
    }

    private boolean sleep(long ms) {
        try {
            Thread.sleep(ms);
            return true;
        } catch (InterruptedException e) {
            System.err.println("Error# 32158 while pausing airEc" + e.getLocalizedMessage());
            return false;
        }
    }
}
