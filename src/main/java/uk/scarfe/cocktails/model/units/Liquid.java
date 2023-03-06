package uk.scarfe.cocktails.model.units;

/**
 * A set of enumerated constants used to identify different types of liquid measurements. These
 * are intentionally separated from the weight measurements, as we are not able to reliably
 * convert between the two different units.
 */
public enum Liquid implements Unit {

    LITRES, CENTILITRES, MILLILITRES,

    FLUID_OUNCES;

    @Override
    public Unit getDefaultUnit() {
        return MILLILITRES;
    }

}
