import java.awt.*;
import javax.swing.*;
import javax.swing.text.JTextComponent;

public class HomeViewDesktop {
    private HomeViewDataCarrier current;
    private JLabel co2L;
    private JLabel tvocL;

    private JPanel panel;

    private JLabel axOnL;

    private JLabel axOffL;

    public HomeViewDesktop(){


        JFrame frame = new JFrame("HomeView");

        panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        JLabel AxOnTimeL = new JLabel("Override");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        panel.add(AxOnTimeL,c);

        JButton axOnB = new JButton("Ax On");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        panel.add(axOnB,c);

        JButton axOffB = new JButton("Ax Off");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 1;
        panel.add(axOffB,c);

        JLabel axLoopL = new JLabel("Ax Loop");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 2;
        panel.add(axLoopL,c);

        axOnL = new JLabel("ON");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 3;
        panel.add(axOnL,c);

        JLabel axOffL = new JLabel("OFF");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 3;
        panel.add(axOffL,c);

        JTextField axLoopOn = new JTextField(7);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 4;
        panel.add(axLoopOn,c);

        JTextField axLoopOff = new JTextField(7);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 4;
        panel.add(axLoopOff,c);

        JLabel AirQualityL = new JLabel("Air Quality");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 5;
        panel.add(AirQualityL,c);

        co2L = new JLabel("CO2: -1 ppm");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 6;
        panel.add(co2L,c);

        tvocL = new JLabel("TVOC: -1 ppb");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 7;
        panel.add(tvocL,c);






//        JButton axOn = new JButton("AxOn");
//        panel.add(axOn);
//        JButton axOff = new JButton("AxOff");
//        panel.add(axOn);

//        JLabel AxOnTimeL = new JLabel("AxOn Cycle Time: ");
//        panel.add(AxOnTimeL);
//        JTextField AxOnTimeTf = new JTextField(6);
//        panel.add(AxOnTimeTf);
//
//        JLabel AxOffTimeL = new JLabel("AxOff Cycle Time: ");
//        panel.add(AxOffTimeL);
//        JTextField AxOffTimeTf = new JTextField(6);
//        panel.add(AxOffTimeTf);



        frame.setContentPane(panel);
        //frame.add(panel);


        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        System.out.println("Starting GUI... This may take a long time");
        frame.setVisible(true);

        System.out.println("GUI created");

    }

    public void consume (HomeViewDataCarrier data) {
        current = data;
        update();
    }

    public void update() {
        co2L.setText("CO2: " + current.getCo2() + "ppm");
        tvocL.setText("TVOC: " + current.getTvoc() + "ppb");

        if(current.isAirExOn()) {
            axOnL.setForeground(Color.GREEN);
            axOffL.setForeground(Color.BLACK);
        } else {
            axOnL.setForeground(Color.BLACK);
            axOffL.setForeground(Color.GREEN);
        }

        panel.repaint();
    }
}
