import jssc.*;

import java.util.ArrayList;

/**
 * Simple serial communication class based on JSSC.
 */
public class SerialHelper {
    private SerialPort serialPort;
    public SerialHelper(String tty, int baud) {
        System.out.println("Connecting to " + tty + " @" + baud);
        connect(tty, baud);
    }

    /**
     * Read a line from the serial interface.
     *
     * Note: This will keep reading bytes until a newline is reached.
     *
     * @param timeoutMs Timeout to read each byte.
     * @return The string read.
     */
    public String readLine(int timeoutMs)  {
        if (serialPort != null) {
            try {
                ArrayList<String> chars = new ArrayList<String>();
                for (int i=0;i<100;i++) {
                    chars.add(serialPort.readString(1, timeoutMs));
                    if(chars.get(i).equals("\n")){
                        break;
                    }
                }
                return String.join("", chars);
            } catch (SerialPortException | SerialPortTimeoutException e) {
                System.err.println("Error #9374: " + e.getLocalizedMessage());
            }
        } else {
            return null;
        }
        return null;
    }

    /**
     * Writes a string to the serial device.
     *
     * @param stringToWrite The string to write.
     */
    public void writeLine(String stringToWrite) {
        if (serialPort != null) {
            try {
                serialPort.writeString(stringToWrite);
            } catch (SerialPortException e) {
                System.err.println("Error #231: " + e.getLocalizedMessage());
            }
        }
    }

    /**
     * Connect to the serial device.
     *
     * TODO: This is currently hard-coded to use 8 data bits, not stop bits, and no parity. It should be expanded to be
     * generic and accept all of this by parameters
     *
     * @param tty Port to connect to
     * @param baud Baud rate
     */
    private void connect(String tty, int baud) {
        serialPort = new SerialPort(tty);
        try {
            serialPort.openPort();
            //TODO: Make this fed by parameters
            serialPort.setParams(baud, 8, 0, 0);
        }
        catch (SerialPortException ex) {
            System.err.println("Error #8764 " + ex.getLocalizedMessage());
        }

    }

    /**
     * Close the connection to the port.
     */
    public void disconnect(){
        try {
            serialPort.closePort();
        }
        catch (SerialPortException ex) {
            System.err.println("Error #78423 " + ex.getLocalizedMessage());
        }
    }

}
