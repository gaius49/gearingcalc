package org.srivette.gearingcalc.app;


import org.srivette.gearingcalc.records.GearSpeedDataPoint;

import javax.swing.JPanel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class GraphPanel extends JPanel {
    private int padding = 25;
    private int labelPadding = 25;
    private Color lineColor = new Color(44, 102, 230, 180);
    private Color pointColor = new Color(100, 100, 100, 180);
    private Color gridColor = new Color(200, 200, 200, 200);
    private static final Stroke GRAPH_STROKE = new BasicStroke(2f);
    private int pointWidth = 4;

    private List<GearSpeedDataPoint> gearSpeedDataPointList;

    public GraphPanel(List<GearSpeedDataPoint> gearSpeedDataPointList) {
        this.gearSpeedDataPointList = gearSpeedDataPointList;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        double xScale = ((double) getWidth() - (2 * padding) - labelPadding) / getMaxRpm();
        double yScale = ((double) getHeight() - 2 * padding - labelPadding) / (getMaxSpeed() - getMinSpeed());

        List<Point> graphPoints = new ArrayList<>();
        for (GearSpeedDataPoint gearSpeedDataPoint : getGearSpeedDataPointList()) {
            double rpmFraction = ((double) gearSpeedDataPoint.rpm() * xScale) + labelPadding + padding;
            int x1 = Math.toIntExact(Math.round(rpmFraction));

            double speedMph = gearSpeedDataPoint.computeSpeedMph();
            double maxSpeed = getMaxSpeed();
            double speedFraction = (maxSpeed - (speedMph)) * yScale + padding;
            int y1 = Math.toIntExact(Math.round(speedFraction));
            graphPoints.add(new Point(x1, y1));
        }

        // draw white background
        g2.setColor(Color.WHITE);
        g2.fillRect(padding + labelPadding, padding, getWidth() - (2 * padding) - labelPadding, getHeight() - 2 * padding - labelPadding);
        g2.setColor(Color.BLACK);

        // create hatch marks and grid lines for y axis.
        // draw 0 mph line first
        {
            int x0 = padding + labelPadding;
            int x1 = pointWidth + padding + labelPadding;
            int y0 = getHeight() - ((0 * (getHeight() - padding * 2 - labelPadding)) / getNumRpms() + padding + labelPadding);
            int y1 = y0;

            g2.setColor(gridColor);
            g2.drawLine(padding + labelPadding + 1 + pointWidth, y0, getWidth() - padding, y1);
            g2.setColor(Color.BLACK);
            String yLabel = ((int) ((getMinSpeed() + (getMaxSpeed() - getMinSpeed()) * ((0 * 1.0) / getNumRpms())) * 100)) / 100.0 + "";
            FontMetrics metrics = g2.getFontMetrics();
            int labelWidth = metrics.stringWidth(yLabel);
            g2.drawString(yLabel, x0 - labelWidth - 5, y0 + (metrics.getHeight() / 2) - 3);
            g2.drawLine(x0, y0, x1, y1);
        }
        for (int i = 0; i < getNumRpms() + 1; i++) {
            int x0 = padding + labelPadding;
            int x1 = pointWidth + padding + labelPadding;
            int y0 = getHeight() - ((i * (getHeight() - padding * 2 - labelPadding)) / getNumRpms() + padding + labelPadding);
            int y1 = y0;

            g2.setColor(gridColor);
            g2.drawLine(padding + labelPadding + 1 + pointWidth, y0, getWidth() - padding, y1);
            g2.setColor(Color.BLACK);
            String yLabel = ((int) ((getMinSpeed() + (getMaxSpeed() - getMinSpeed()) * ((i * 1.0) / getNumRpms())))) / 100.0 + "a";
            FontMetrics metrics = g2.getFontMetrics();
            int labelWidth = metrics.stringWidth(yLabel);
            g2.drawString(yLabel, x0 - labelWidth - 5, y0 + (metrics.getHeight() / 2) - 3);
            g2.drawLine(x0, y0, x1, y1);
        }

        // and for x axis
        for (int i = 0; i < gearSpeedDataPointList.size(); i++) {
            int x0 = i * (getWidth() - padding * 2 - labelPadding) / (gearSpeedDataPointList.size() - 1) + padding + labelPadding;
            int x1 = x0;
            int y0 = getHeight() - padding - labelPadding;
            int y1 = y0 - pointWidth;

            g2.setColor(gridColor);
            g2.drawLine(x0, getHeight() - padding - labelPadding - 1 - pointWidth, x1, padding);
            g2.setColor(Color.BLACK);
            String xLabel = gearSpeedDataPointList.get(i).rpm() + "";
            FontMetrics metrics = g2.getFontMetrics();
            int labelWidth = metrics.stringWidth(xLabel);
            g2.drawString(xLabel, x0 - labelWidth / 2, y0 + metrics.getHeight() + 3);
            g2.drawLine(x0, y0, x1, y1);
        }

        // create x and y axes
        g2.drawLine(padding + labelPadding, getHeight() - padding - labelPadding, padding + labelPadding, padding);
        g2.drawLine(padding + labelPadding, getHeight() - padding - labelPadding, getWidth() - padding, getHeight() - padding - labelPadding);

        Stroke oldStroke = g2.getStroke();
        g2.setColor(lineColor);
        g2.setStroke(GRAPH_STROKE);
        for (int i = 0; i < graphPoints.size() - 1; i++) {
            int x1 = graphPoints.get(i).x;
            int y1 = graphPoints.get(i).y;
            int x2 = graphPoints.get(i + 1).x;
            int y2 = graphPoints.get(i + 1).y;
            g2.drawLine(x1, y1, x2, y2);
        }

        g2.setStroke(oldStroke);
        g2.setColor(pointColor);
        for (int i = 0; i < graphPoints.size(); i++) {
            int x = graphPoints.get(i).x - pointWidth / 2;
            int y = graphPoints.get(i).y - pointWidth / 2;
            int ovalW = pointWidth;
            int ovalH = pointWidth;
            g2.fillOval(x, y, ovalW, ovalH);
        }
    }

    private int getMaxRpm() {
        int maxRpm = 0;
        for (GearSpeedDataPoint gearSpeedDataPoint : getGearSpeedDataPointList()) {
            maxRpm = Math.max(maxRpm, gearSpeedDataPoint.rpm());
        }

        return maxRpm;
    }

    private double getMinSpeed() {
        return 0d;
    }

    private double getMaxSpeed() {
        double maxSpeed = Double.MIN_VALUE;
        for (GearSpeedDataPoint gearSpeedDataPoint : getGearSpeedDataPointList()) {
            maxSpeed = Math.max(maxSpeed, gearSpeedDataPoint.computeSpeedMph());
        }

        return maxSpeed;
    }

    private int getNumRpms() {
        Set<Integer> rpms = getGearSpeedDataPointList().stream().map(gearSpeedDataPoint -> gearSpeedDataPoint.rpm()).collect(Collectors.toSet());

        return rpms.size();
    }

    private List<GearSpeedDataPoint> getGearSpeedDataPointList() {
        return gearSpeedDataPointList;
    }
}
