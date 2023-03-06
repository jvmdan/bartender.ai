package uk.scarfe.cocktails.model.units;

/**
 * A set of enumerated constants used to identify different types of mass measurements. These
 * are intentionally separated from the liquid measurements, as we are not able to reliably
 * convert between the two different units.
 */
public enum Mass implements Unit {

    GRAMMES, KILOGRAMMES, OUNCES;

    @Override
    public Unit getDefaultUnit() {
        return GRAMMES;
    }

}
