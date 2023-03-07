package uk.scarfe.cocktails.model.units;

/**
 * A Count is the most basic form of unit. When using the entirety of an ingredient, we can simply
 * specify the number of ingredients to use. As a result, there is no conversion performed between
 * count units, and we simply compare the amounts without the need to normalise to a standard unit.
 *
 * @author Daniel Scarfe
 */
public enum Count implements Unit {

    // FIXME | I don't really like this class as a "catch all". We need to do something better.
    NONE, HANDFUL, TO_TASTE, TEASPOON, TABLESPOON;

    @Override
    public Unit getDefaultUnit() {
        return NONE;
    }

    @Override
    public double getConversionFactor() {
        return 1;
    }

}
