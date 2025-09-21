package org.srivette.gearingcalc.records;

public record TireSize(double diameterInches) {
    public static TireSize fromSpecs(int widthMm, int aspectRatio, double wheelInches) {
        double tireWidthInches = widthMm / 25.4;
        double sizeWallInches = tireWidthInches * (aspectRatio / 100d); // aspect ratios are percentages
        double diameterInches = (2 * sizeWallInches) + wheelInches;

        return new TireSize(diameterInches);
    }

    public double circumferenceInches() {
        return Math.PI * diameterInches;
    }

    public double revsPerMile() {
        double circumference = Math.PI * diameterInches;
        double circumferencesPerMile = (5280 * 12) / circumference;

        return circumferencesPerMile;
    }
}
