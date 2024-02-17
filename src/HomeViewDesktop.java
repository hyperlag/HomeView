import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.*;
import javax.swing.text.JTextComponent;

public class HomeViewDesktop {
    private HomeViewDataCarrier current;
    private JLabel co2L;
    private JLabel tvocL;

    private JPanel panel;

    private JLabel axOnL;

    private JLabel axOffL;
    private JTextField axLoopOn;
    private JTextField axLoopOff;
    private JButton axLoopB;
    private JButton axOnB;
    private JButton axOffB;
    private JLabel axOverL;

    private long inputEpoch = 0;



    public HomeViewDesktop(){


        JFrame frame = new JFrame("HomeView");

        panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        panel.setBackground(Color.BLACK);
        GridBagConstraints c = new GridBagConstraints();

        axOverL = new JLabel("Override");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        panel.add(axOverL,c);

        axOnB = new JButton("Ax On");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        panel.add(axOnB,c);
        axOnB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                current.setAirExOn(true);
            }
        } );

        axOffB = new JButton("Ax Off");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 1;
        panel.add(axOffB,c);
        axOffB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                current.setAirExOn(false);
            }
        } );

        axOnL = new JLabel("ON");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 3;
        panel.add(axOnL,c);

        axOffL = new JLabel("OFF");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 3;
        panel.add(axOffL,c);

        axLoopOn = new JTextField(7);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 4;
        axLoopOn.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                inputEpoch = System.currentTimeMillis();
            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        panel.add(axLoopOn,c);

        axLoopOff = new JTextField(7);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 4;
        axLoopOff.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                inputEpoch = System.currentTimeMillis();
            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        panel.add(axLoopOff,c);

        axLoopB = new JButton("Ax Loop");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 5;
        panel.add(axLoopB,c);
        axLoopB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                current.setAirExCycling(true);
                try {
                    System.out.println("Setting up an ax loop");
                    current.setAirExCycleOnOffMs(new long[]{Long.parseLong(axLoopOn.getText()) * 1000, Long.parseLong(axLoopOff.getText()) * 1000});
                } catch (Exception ex) { //TODO: Change this to catch just the long format exception
                    //TODO Ignore for now. The update will remove this
                    System.err.println("Error #543897789 " + ex.getMessage());
                }
            }
        } );

        JLabel AirQualityL = new JLabel("Air Quality");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 7;
        AirQualityL.setForeground(Color.GREEN);
        panel.add(AirQualityL,c);

        co2L = new JLabel("CO2: -1 ppm");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 8;
        co2L.setForeground(Color.GREEN);
        panel.add(co2L,c);

        tvocL = new JLabel("TVOC: -1 ppb");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 9;
        tvocL.setForeground(Color.GREEN);
        panel.add(tvocL,c);




        frame.setContentPane(panel);
        //frame.add(panel);


        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        frame.setSize(200, 220);
        System.out.println("Starting GUI... This may take a long time");
        frame.setVisible(true);

        System.out.println("GUI created");

    }

    public void consume (HomeViewDataCarrier data) {
        current = data;
        update();
    }

    public HomeViewDataCarrier getData() {
        return current;
    }

    public void update() {
        co2L.setText("CO2: " + current.getCo2() + "ppm");
        tvocL.setText("TVOC: " + current.getTvoc() + "ppb");

        if (current.isAirExOn() && axOnL != null && axOffL != null && axOnB != null && axOffB != null) {
            System.out.println("AxOn Update");
            axOnL.setForeground(Color.GREEN);
            axOffL.setForeground(Color.RED);
            axOnB.setBackground(Color.GREEN);
            axOffB.setBackground(Color.RED);
        } else if (axOnL != null && axOffL != null && axOnB != null && axOffB != null) {
            axOnL.setForeground(Color.RED);
            axOffL.setForeground(Color.GREEN);
            axOnB.setBackground(Color.RED);
            axOffB.setBackground(Color.GREEN);
        }

        //If user is typing give them 30 seconds to be done
        if (axLoopOn != null && axLoopOff != null && (System.currentTimeMillis() - inputEpoch) > 30000) {
            axLoopOn.setText(current.getAirExCycleOnOffMs()[0]/1000+"");
            axLoopOff.setText(current.getAirExCycleOnOffMs()[1]/1000+"");
        }

        if (current.isAirExCycling() && axLoopB != null && axOverL != null) {
            axLoopB.setBackground(Color.GREEN);
            axOverL.setForeground(Color.RED);
        } else if ( axLoopB != null && axOverL != null){
            axLoopB.setBackground(Color.RED);
            axOverL.setForeground(Color.GREEN);
        }

        panel.repaint();
    }
}
