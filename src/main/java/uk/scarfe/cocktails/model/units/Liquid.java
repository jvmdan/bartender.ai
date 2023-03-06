package uk.scarfe.cocktails.model.units;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * A set of enumerated constants used to identify different types of liquid measurements. These
 * are intentionally separated from the weight measurements, as we are not able to reliably
 * convert between the two different units.
 */
@RequiredArgsConstructor
public enum Liquid implements Unit {

    MILLILITRES(1),
    CENTILITRES(0.1),
    LITRES(0.001);

    private final double conversionFactor;

    @Override
    public Unit getDefaultUnit() {
        return MILLILITRES;
    }

    @Override
    public double getConversionFactor() {
        return this.conversionFactor;
    }

}
