import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Set;
import javax.swing.*;

/**
 * Test GUI for the remote HomeView client.
 */
public class HomeViewDesktop {

    //Global swing objects for updating.
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
    private ChartPanel chartPanel;

    private long inputEpoch = 0;

    // Running history
    private LinkedHashMap<Long,HomeViewDataCarrier> history;



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
        axOverL.setForeground(Color.RED);

        axOnB = new JButton("Ax On");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        panel.add(axOnB,c);
        axOnB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                current.setAirExCycling(false);
                current.setAirExOn(true);
            }
        } );
        axOnB.setBackground(Color.RED);

        axOffB = new JButton("Ax Off");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 1;
        panel.add(axOffB,c);
        axOffB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                current.setAirExCycling(false);
                current.setAirExOn(false);
            }
        } );
        axOffB.setBackground(Color.RED);

        axOnL = new JLabel("ON");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 3;
        panel.add(axOnL,c);
        axOnL.setForeground(Color.RED);

        axOffL = new JLabel("OFF");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 3;
        panel.add(axOffL,c);
        axOffL.setForeground(Color.RED);

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
        axLoopB.setBackground(Color.RED);

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

        chartPanel = new ChartPanel(createChart(), false);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 10;
        c.gridwidth = 3;
        panel.add(chartPanel,c);
        chartPanel.setFillZoomRectangle(true);
        chartPanel.setMouseWheelEnabled(true);
        chartPanel.setBackground(Color.darkGray);
        chartPanel.setPreferredSize(new java.awt.Dimension(750, 500));
        chartPanel.setVisible(true);

        frame.setContentPane(panel);
        //frame.add(panel);


        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        frame.setSize(775, 475);
        System.out.println("Starting GUI... This may take a long time");
        frame.setVisible(true);

        frame.pack();

        System.out.println("GUI created");

    }

    /**
     * Updates the GUI with a new HomeViewDataCarrier object.
     *
     * @param data State object to use in the update.
     */
    public void consume (HomeViewDataCarrier data) {
        current = data;
        update();
    }

    /**
     * Adds a list of previous data entries.
     *
     * @param history LinkedHashMap of history items, keyed on epoch.
     */
    public void consumeHistory(LinkedHashMap<Long,HomeViewDataCarrier> history) {
        this.history = history;
//        TODO: Clean this up
//        Long lastKey = history.keySet().iterator().next();
//        Iterator iterator = history.keySet().iterator();
//        while (iterator.hasNext()) {
//            lastKey = (Long) iterator.next();
//        }
    }

    public HomeViewDataCarrier getData() {
        return current;
    }

    /**
    * Update the UI.
     *
     * TODO: This is very inefficient. Clean this up.
    */
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


        //This is terrible. Fix this
        GridBagConstraints c = new GridBagConstraints();

        chartPanel.setFillZoomRectangle(true);
        chartPanel.setMouseWheelEnabled(true);
        chartPanel.setBackground(Color.darkGray);
        chartPanel.setPreferredSize(new java.awt.Dimension(750, 500));
        chartPanel.setVisible(true);
        chartPanel.setChart(createChart());

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 10;
        c.gridwidth = 3;

        panel.add(chartPanel,c);

        panel.repaint();
    }

    private JFreeChart createChart() {
        TimeSeries co2set = new TimeSeries("CO2 ppm");
        TimeSeries tvocSet = new TimeSeries("TVOC ppb*100");
        if (history != null) {
            co2set = new TimeSeries("CO2");
            Set<Long> keys = history.keySet();
            for (Long key : keys) {
                co2set.add(new Second(new Date(history.get(key).getLastUpdateEpoch())), history.get(key).getCo2());
                tvocSet.add(new Second(new Date(history.get(key).getLastUpdateEpoch())), history.get(key).getTvoc()*100);
            }
        }

        TimeSeriesCollection dataset = new TimeSeriesCollection();

        dataset.addSeries(co2set);
        dataset.addSeries(tvocSet);


        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                "Air Quality CO2",
                "Time",
                "Gas Levels",
                dataset);

        chart.setBackgroundPaint(Color.BLACK);

        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.darkGray);
        plot.setDomainGridlinePaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.WHITE);
        plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
        plot.setDomainCrosshairVisible(true);
        plot.setRangeCrosshairVisible(true);

        XYItemRenderer r = plot.getRenderer();
        if (r instanceof XYLineAndShapeRenderer) {
            XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) r;
            renderer.setDefaultShapesVisible(true);
            renderer.setDefaultShapesFilled(true);
            renderer.setDrawSeriesLineAsPath(true);
        }

        DateAxis axis = (DateAxis) plot.getDomainAxis();
        axis.setDateFormatOverride(new SimpleDateFormat("ss"));

        return chart;

    }
}
