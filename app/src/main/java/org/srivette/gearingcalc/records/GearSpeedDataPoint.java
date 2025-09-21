package org.srivette.gearingcalc.records;

public record GearSpeedDataPoint(
        GearRatio gearRatio,
        DiffRatio diffRatio,
        TireSize tireSize,
        int rpm
) {
    public double computeSpeedMph() {
        double driveShaftRpm = rpm / gearRatio().gearRatio();
        double axleRpm = driveShaftRpm / diffRatio.diffRatio();
        double inchesPerMinute = axleRpm * tireSize.circumferenceInches();
        double inchesPerHour = inchesPerMinute * 60;
        double mph = inchesPerHour / (5280 * 12);

        return mph;
    }
}
