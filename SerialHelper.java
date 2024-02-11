import jssc.SerialPortException;
import jssc.SerialPortList;
import jssc.SerialPort;
public class SerialHelper {
    private SerialPort serialPort;
    public SerialHelper(String tty, int baud) {
        System.out.println("Connecting to " + ttyUSB0 + " @" + baud);
        conenct(tty, baud);
    }

    public byte[] read() throws SerialPortException {
        if (serialPort != null) {
            return serialPort.readBytes();
        } else {
            return null;
        }
    }

    private void connect(String tty, int baud) {
        serialPort = new SerialPort(tty);
        try {
            serialPort.openPort();
            serialPort.setParams(baud, 8, 1, 0);
  //          byte[] buffer = serialPort.readBytes(10);//Read 10 bytes from serial port
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
