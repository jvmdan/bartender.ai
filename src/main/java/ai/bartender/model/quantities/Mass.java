package ai.bartender.model.quantities;

import lombok.RequiredArgsConstructor;

/**
 * A set of enumerated constants used to identify different types of mass measurements. These
 * are intentionally separated from the liquid measurements, as we are not able to reliably
 * convert between the two different units.
 */
@RequiredArgsConstructor
public enum Mass implements Unit {

    GRAMMES(1),
    KILOGRAMMES(0.001),
    OUNCES(0.035274);

    private final double conversionFactor;

    @Override
    public Unit getDefaultUnit() {
        return GRAMMES;
    }

    @Override
    public double getConversionFactor() {
        return this.conversionFactor;
    }

}
