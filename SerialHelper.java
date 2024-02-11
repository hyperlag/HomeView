import jssc.*;

import java.util.ArrayList;

public class SerialHelper {
    private SerialPort serialPort;
    public SerialHelper(String tty, int baud) {
        System.out.println("Connecting to " + tty + " @" + baud);
        connect(tty, baud);
    }

    public String readLine(int timeoutMs)  {
        if (serialPort != null) {
            try {
                ArrayList<String> chars = new ArrayList<String>();
                System.out.println("Attempting Read");
                for (int i=0;i<100;i++) {
                    chars.add(serialPort.readString(1, timeoutMs));
                    if(chars.get(i).equals("\n")){
                        break;
                    }
                }
                return String.join("", chars);
            } catch (SerialPortException | SerialPortTimeoutException e) {
                throw new RuntimeException(e);
            }
        } else {
            return null;
        }
    }

    public void writeLine(String stringToWrite) {
        if (serialPort != null) {
            try {
                serialPort.writeString(stringToWrite);
            } catch (SerialPortException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void connect(String tty, int baud) {
        serialPort = new SerialPort(tty);
        try {
            serialPort.openPort();
            //TODO: Make this fed by parameters
            serialPort.setParams(baud, 8, 0, 0);
        }
        catch (SerialPortException ex) {
            System.out.println(ex.getLocalizedMessage());
        }

    }

    private void disconnect(){
        try {
            serialPort.closePort();
        }
        catch (SerialPortException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
    }

}
