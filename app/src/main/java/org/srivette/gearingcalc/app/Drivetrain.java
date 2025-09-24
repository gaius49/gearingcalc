package org.srivette.gearingcalc.app;

import org.srivette.gearingcalc.records.DiffRatio;
import org.srivette.gearingcalc.records.GearRatio;
import org.srivette.gearingcalc.records.GearSpeedDataPoint;
import org.srivette.gearingcalc.records.TireSize;

import java.util.List;

public class Drivetrain {
    private List<GearRatio> gearRatios;
    private DiffRatio diffRatio;
    private TireSize tireSize;

    public Drivetrain(List<GearRatio> gearRatios, DiffRatio diffRatio, TireSize tireSize) {
        this.gearRatios = gearRatios;
        this.diffRatio = diffRatio;
        this.tireSize = tireSize;
    }

    public List<GearRatio> getGearRatios() {
        return gearRatios;
    }

    public GearSpeedDataPoint getGearSpeedDataPoint(int gearNumber, int rpm) {
        GearRatio gearRatio = gearRatios.get(gearNumber - 1);
        GearSpeedDataPoint gearSpeedDataPoint = new GearSpeedDataPoint(gearRatio, diffRatio, tireSize, rpm);

        return gearSpeedDataPoint;
    }
}
