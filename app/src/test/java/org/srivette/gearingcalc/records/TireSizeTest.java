package org.srivette.gearingcalc.records;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class TireSizeTest {
    @Test
    public void testDiameterCompute() {
        TireSize tireSize = TireSize.fromSpecs(205, 70, 15);
        double computedDiameter = tireSize.diameterInches();
        double expectedValue = ((205 / 25.4) * 0.7 * 2) + 15;

        assertThat(computedDiameter).isEqualTo(expectedValue);
    }
}
