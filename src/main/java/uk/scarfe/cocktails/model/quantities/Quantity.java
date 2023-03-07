package uk.scarfe.cocktails.model.quantities;

import lombok.Getter;
import lombok.ToString;

import java.util.Objects;

/**
 * The Quantity class represents an amount to be used within a cocktail recipe. This allows us to
 * identify how much of a given ingredient we require. Note that different recipes may specify
 * quantities in different measurements (such as metric versus imperial) so we make use of Java's
 * generics to assist in converting between the different unit types. Erasure ensures that the
 * caller cannot accidentally mix unit types.
 *
 * @param <U> the unit type we are quantifying.
 */
@Getter
@ToString
public class Quantity<U extends Unit> {

    private final double amount;
    private final U unit;

    // Private constructor to prevent instantiation outside of static factory methods.
    private Quantity(double amount, U unit) {
        this.amount = amount;
        this.unit = unit;
    }

    public static Quantity<Liquid> of(double amount, Liquid unit) {
        return new Quantity<>(amount, unit);
    }

    public static Quantity<Mass> of(double amount, Mass unit) {
        return new Quantity<>(amount, unit);
    }

    public static Quantity<Count> of(double amount, Count unit) {
        return new Quantity<>(amount, unit);
    }

    public static Quantity<Count> of(double amount) {
        return new Quantity<>(amount, Count.NONE);
    }

    /**
     * Convert a given quantity to a different set of units of the same type (mass / liquid / etc).
     *
     * @param <T>  the unit type we are dealing with (mass, liquid, other).
     * @param from the original quantity value we wish to convert.
     * @param to   the units we wish to convert to.
     */
    public static <T extends Unit> Quantity<T> convert(Quantity<T> from, T to) {
        final double normalisedAmount = from.getAmount() / from.getUnit().getConversionFactor();
        final double newAmount = normalisedAmount * to.getConversionFactor();
        return new Quantity<>(newAmount, to);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Quantity<?> quantity = (Quantity<?>) o;
        double normalised = amount / unit.getConversionFactor();
        double normalisedOther = quantity.amount / quantity.getUnit().getConversionFactor();
        return Double.compare(normalisedOther, normalised) == 0;
    }

    @Override
    public int hashCode() {
        double normalised = amount / unit.getConversionFactor();
        return Objects.hash(normalised);
    }

}
